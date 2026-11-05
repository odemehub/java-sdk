package com.odemehub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odemehub.exception.AuthenticationException;
import com.odemehub.exception.ForbiddenException;
import com.odemehub.exception.NotFoundException;
import com.odemehub.exception.RateLimitException;
import com.odemehub.exception.SignatureException;
import com.odemehub.exception.TransportException;
import com.odemehub.exception.UnexpectedResponseException;
import com.odemehub.exception.ValidationException;
import com.odemehub.request.Message;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * How a message travels to the gateway and its answer is read back, the same
 * for a team's gateway ({@link Client}) and for a partner's own endpoints
 * ({@link PartnerClient}): signed with the secret of whoever sends it, its
 * answer checked against the same secret, a refusal turned into the exception
 * its status names. Only the address differs, and the caller says it.
 */
final class Transport {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpClient http;
    private final Signature signature;
    private final String apiKey;
    private final Duration timeout;

    Transport(HttpClient http, Signature signature, String apiKey, Duration timeout) {
        this.http = http;
        this.signature = signature;
        this.apiKey = apiKey;
        this.timeout = timeout;
    }

    /**
     * Sign what is being asked for, hand it to the gateway and read the answer
     * back. The body is signed exactly as it is sent, byte for byte, together
     * with the moment, the method and the path, so it is written once and used
     * for both.
     *
     * @param path The path the message goes to, as it is signed: with the leading slash.
     * @param url  The full address it goes to.
     */
    JsonNode send(Message message, String path, String url) {
        String method = message.method();
        byte[] body;

        try {
            body = JSON.writeValueAsBytes(message.toBody());
        } catch (JsonProcessingException exception) {
            throw new UnexpectedResponseException("İstek gövdesi JSON olarak yazılamadı: " + exception.getMessage(), 0);
        }

        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url))
            .timeout(timeout)
            .header(Options.API_KEY_HEADER, apiKey)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body));

        signature.headers(method, path, body).forEach(request::header);

        HttpResponse<byte[]> response;

        try {
            response = http.send(request.build(), HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException exception) {
            String reason = exception.getMessage() != null ? exception.getMessage() : exception.getClass().getSimpleName();

            throw new TransportException("Ödeme geçidine ulaşılamadı: " + reason, exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new TransportException("Ödeme geçidine giden istek yarıda kesildi.", exception);
        }

        return read(response, method, path);
    }

    /**
     * Read the answer. An outcome is answered with 200 and signed, however the
     * payment itself turned out: a payment the provider declined is an
     * outcome like any other and comes back rather than being thrown. The
     * signature is checked over the method and the path of the request, the
     * answer's own moment and its body.
     *
     * <p>Anything else is a refusal — the request never became a payment — and
     * the status says which kind. The gateway signs some of those too, but a
     * signature does not make a refusal an outcome, so the status is read
     * first.
     */
    private JsonNode read(HttpResponse<byte[]> response, String method, String path) {
        int status = response.statusCode();
        String payload = new String(response.body(), StandardCharsets.UTF_8);

        if (status == 200) {
            boolean verified = signature.verify(
                method,
                path,
                response.body(),
                header(response, Signature.TIMESTAMP_HEADER),
                header(response, Signature.HEADER)
            );

            if (!verified) {
                throw new SignatureException("Yanıtın imzası doğrulanamadı; yanıt ödeme geçidinden gelmemiş olabilir.");
            }

            return decode(payload, status);
        }

        JsonNode body = parse(payload);
        JsonNode result = body == null ? null : body.path("result");
        String message = refusalMessage(body, result);

        switch (status) {
            case 401:
                throw new AuthenticationException(message);
            case 403:
                throw new ForbiddenException(message);
            case 404:
                throw new NotFoundException(message);
            case 422:
                throw new ValidationException(message, refusalErrors(body, result));
            case 429:
                throw new RateLimitException(message, retryAfter(response));
            default:
                throw new UnexpectedResponseException(message, status);
        }
    }

    /**
     * Read a body the signature has already vouched for.
     */
    static JsonNode decode(String payload, int status) {
        JsonNode body = parse(payload);

        if (body == null) {
            throw new UnexpectedResponseException("Ödeme geçidi " + status + " durumuyla okunamayan bir yanıt döndü.", status);
        }

        return body;
    }

    /**
     * A header, or null when the answer did not carry it.
     */
    private static String header(HttpResponse<byte[]> response, String name) {
        return response.headers().firstValue(name).filter(value -> !value.isEmpty()).orElse(null);
    }

    /**
     * How long the gateway asked to wait before trying again, in seconds.
     */
    private static Integer retryAfter(HttpResponse<byte[]> response) {
        String value = header(response, "Retry-After");

        return value != null && value.matches("[0-9]{1,9}") ? Integer.valueOf(value) : null;
    }

    /**
     * What a refusal says. The gateway answers in the one shape it answers
     * everything in, so what went wrong is found under {@code result}.
     */
    private static String refusalMessage(JsonNode body, JsonNode result) {
        if (result != null && result.path("message").isTextual()) {
            return result.path("message").textValue();
        }

        if (body != null && body.path("message").isTextual()) {
            return body.path("message").textValue();
        }

        return "Ödeme geçidi isteği reddetti.";
    }

    /**
     * Which fields a refusal is about, each with the reasons it was refused.
     */
    private static Map<String, List<String>> refusalErrors(JsonNode body, JsonNode result) {
        JsonNode errors = result != null && result.path("errors").isObject()
            ? result.path("errors")
            : body != null ? body.path("errors") : null;
        Map<String, List<String>> fields = new LinkedHashMap<>();

        if (errors != null && errors.isObject()) {
            errors.properties().forEach(field -> {
                List<String> reasons = new ArrayList<>();
                field.getValue().elements().forEachRemaining(reason -> reasons.add(reason.asText()));
                fields.put(field.getKey(), List.copyOf(reasons));
            });
        }

        return fields;
    }

    private static JsonNode parse(String payload) {
        try {
            JsonNode body = JSON.readTree(payload);

            return body != null && body.isObject() ? body : null;
        } catch (JsonProcessingException exception) {
            return null;
        }
    }
}
