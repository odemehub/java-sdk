package com.odemehub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odemehub.exception.AuthenticationException;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The gateway, as the merchant's application talks to it. Every request
 * leaves signed with the team's secret and every answer is checked against
 * it, so both sides can tell the other is really who it says it is.
 *
 * <p>A client holds no state beyond its options, so one can be shared across
 * threads for the life of the application.
 */
public final class Client {

    private static final ObjectMapper JSON = new ObjectMapper();

    private final Options options;
    private final HttpClient http;
    private final Signature signature;

    public Client(Options options) {
        this(options, HttpClient.newBuilder().connectTimeout(options.getTimeout()).build());
    }

    /**
     * @param http The HTTP client the requests go through, for a merchant that
     *             already configures one (a proxy, an executor).
     */
    public Client(Options options, HttpClient http) {
        this.options = options;
        this.http = http;
        this.signature = new Signature(options.getApiSecret());
    }

    /**
     * Start a payment the customer confirms with their bank. A successful
     * answer is not a settled payment: the customer is still to be sent to the
     * address it comes back with.
     */
    public com.odemehub.response.SecurePayment securePayment(com.odemehub.request.SecurePayment payment) {
        return com.odemehub.response.SecurePayment.fromBody(send(payment));
    }

    /**
     * Charge a payment straight to the card. A successful answer is a settled
     * payment.
     */
    public com.odemehub.response.RegularPayment regularPayment(com.odemehub.request.RegularPayment payment) {
        return com.odemehub.response.RegularPayment.fromBody(send(payment));
    }

    /**
     * Open an order to be paid on the gateway's own page, and get back the
     * address to send the customer to.
     */
    public com.odemehub.response.OrderPayment orderPayment(com.odemehub.request.OrderPayment orderPayment) {
        return com.odemehub.response.OrderPayment.fromBody(send(orderPayment));
    }

    /**
     * Open a subscription. The customer is sent to the address it comes back
     * with and pays there, and the periods after that are taken from the card
     * they pay with.
     */
    public com.odemehub.response.Subscription subscriptionPayment(com.odemehub.request.SubscriptionPayment subscription) {
        return com.odemehub.response.Subscription.fromBody(send(subscription));
    }

    /**
     * Give money back out of a payment the provider has settled, whole or in
     * part. A refund that names no amount gives back everything the payment
     * has left in it.
     */
    public com.odemehub.response.GiveBack refundPayment(com.odemehub.request.RefundPayment refund) {
        return com.odemehub.response.GiveBack.fromBody(send(refund));
    }

    /**
     * Take back the whole of a payment the provider has not settled yet.
     * Anything less than the whole of it goes back as a refund instead.
     */
    public com.odemehub.response.GiveBack cancelPayment(com.odemehub.request.CancelPayment cancel) {
        return com.odemehub.response.GiveBack.fromBody(send(cancel));
    }

    /**
     * How a payment went. A customer sent to their bank comes back to the
     * merchant with the payment's token and a hint at how it went; the hint is
     * worth nothing on its own, and this call says what really became of it.
     */
    public com.odemehub.response.Payment retrievePayment(com.odemehub.request.RetrievePayment payment) {
        return com.odemehub.response.Payment.fromBody(send(payment));
    }

    /**
     * Ask what the gateway's provider knows about a card by the head of its
     * number, and how an amount may be paid off on it. Nothing is charged and
     * nothing is written down.
     */
    public com.odemehub.response.Bin retrieveBin(com.odemehub.request.RetrieveBin retrieveBin) {
        return com.odemehub.response.Bin.fromBody(send(retrieveBin));
    }

    /**
     * Save a product in the merchant's catalogue at the gateway, or change the
     * one already saved under the same key on the same channel. Order lines
     * and subscriptions name products by key.
     */
    public com.odemehub.response.Product saveProduct(com.odemehub.request.SaveProduct product) {
        return com.odemehub.response.Product.fromBody(send(product));
    }

    /**
     * Where a subscription stands: what it is for, the period it is on and
     * whether that period has been paid for.
     */
    public com.odemehub.response.Subscription retrieveSubscription(com.odemehub.request.RetrieveSubscription subscription) {
        return com.odemehub.response.Subscription.fromBody(send(subscription));
    }

    /**
     * Call a subscription off. Nothing is given back: the customer keeps the
     * days they already paid for and is served to the end of them, and nothing
     * is charged after that.
     */
    public com.odemehub.response.Subscription cancelSubscription(com.odemehub.request.CancelSubscription subscription) {
        return com.odemehub.response.Subscription.fromBody(send(subscription));
    }

    /**
     * Keep a card for a customer without making a payment on it.
     */
    public com.odemehub.response.KeptCard saveCard(com.odemehub.request.SaveCard saveCard) {
        return com.odemehub.response.KeptCard.fromBody(send(saveCard));
    }

    /**
     * The cards a customer let the merchant keep, the default one first.
     */
    public com.odemehub.response.KeptCards savedCards(com.odemehub.request.SavedCards savedCards) {
        return com.odemehub.response.KeptCards.fromBody(send(savedCards));
    }

    /**
     * Make one of a customer's kept cards the one they pay with unless they
     * say otherwise.
     */
    public com.odemehub.response.KeptCard defaultSavedCard(com.odemehub.request.DefaultSavedCard defaultSavedCard) {
        return com.odemehub.response.KeptCard.fromBody(send(defaultSavedCard));
    }

    /**
     * Let go of one of a customer's kept cards, at the provider and here.
     */
    public com.odemehub.response.KeptCard deleteSavedCard(com.odemehub.request.DeleteSavedCard deleteSavedCard) {
        return com.odemehub.response.KeptCard.fromBody(send(deleteSavedCard));
    }

    /**
     * Read the word the gateway sent about a subscription: posted to the
     * address the subscription was opened with, as plain JSON signed in the
     * {@code X-Signature} header. Hand it the body exactly as it arrived, byte
     * for byte, together with the header; nothing in it is to be believed
     * until the signature holds.
     *
     * @throws SignatureException when the signature does not hold.
     */
    public com.odemehub.response.SubscriptionWebhook subscriptionWebhook(byte[] payload, String signature) {
        if (!this.signature.verify(payload, signature)) {
            throw new SignatureException("Bildirimin imzası doğrulanamadı; bildirim ödeme geçidinden gelmemiş olabilir.");
        }

        return com.odemehub.response.SubscriptionWebhook.fromBody(decode(new String(payload, StandardCharsets.UTF_8), 0));
    }

    public com.odemehub.response.SubscriptionWebhook subscriptionWebhook(String payload, String signature) {
        return subscriptionWebhook(payload.getBytes(StandardCharsets.UTF_8), signature);
    }

    /**
     * Sign what is being asked for, hand it to the gateway and read the answer
     * back. The body is signed exactly as it is sent, byte for byte, so it is
     * written once and used for both.
     */
    private JsonNode send(Message message) {
        byte[] body;

        try {
            body = JSON.writeValueAsBytes(message.toBody(options.getChannelToken()));
        } catch (JsonProcessingException exception) {
            throw new UnexpectedResponseException("İstek gövdesi JSON olarak yazılamadı: " + exception.getMessage(), 0);
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(options.url(message.path())))
            .timeout(options.getTimeout())
            .header(Options.API_KEY_HEADER, options.getApiKey())
            .header(Signature.HEADER, signature.sign(body))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();

        HttpResponse<byte[]> response;

        try {
            response = http.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (IOException exception) {
            String reason = exception.getMessage() != null ? exception.getMessage() : exception.getClass().getSimpleName();

            throw new TransportException("Ödeme geçidine ulaşılamadı: " + reason, exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new TransportException("Ödeme geçidine giden istek yarıda kesildi.", exception);
        }

        return read(response);
    }

    /**
     * Read the answer. An outcome is answered with 200 and signed, however the
     * payment itself turned out: a payment the provider declined is an
     * outcome like any other and comes back rather than being thrown.
     *
     * <p>Anything else is a refusal — the request never became a payment — and
     * the status says which kind. The gateway signs some of those too, but a
     * signature does not make a refusal an outcome, so the status is read
     * first.
     */
    private JsonNode read(HttpResponse<byte[]> response) {
        int status = response.statusCode();
        String payload = new String(response.body(), StandardCharsets.UTF_8);

        if (status == 200) {
            String answerSignature = response.headers().firstValue(Signature.HEADER).filter(value -> !value.isEmpty()).orElse(null);

            if (!signature.verify(response.body(), answerSignature)) {
                throw new SignatureException("Yanıtın imzası doğrulanamadı; yanıt ödeme geçidinden gelmemiş olabilir.");
            }

            return decode(payload, status);
        }

        JsonNode body = parse(payload);
        JsonNode result = body == null ? null : body.path("result");
        String message = refusalMessage(body, result);

        if (status == 401) {
            throw new AuthenticationException(message);
        }

        if (status == 422) {
            throw new ValidationException(message, refusalErrors(body, result));
        }

        throw new UnexpectedResponseException(message, status);
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

    /**
     * Read a body the signature has already vouched for.
     */
    private static JsonNode decode(String payload, int status) {
        JsonNode body = parse(payload);

        if (body == null) {
            throw new UnexpectedResponseException("Ödeme geçidi " + status + " durumuyla okunamayan bir yanıt döndü.", status);
        }

        return body;
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
