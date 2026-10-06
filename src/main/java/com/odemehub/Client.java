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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The gateway, as the merchant's application talks to it. Every request
 * leaves signed with the team's secret and every answer is checked against
 * it, so both sides can tell the other is really who it says it is.
 *
 * <p>There is one method per endpoint, named after it: {@code create-order}
 * is {@code createOrder()} and takes a {@code request.CreateOrder}.
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
     * address it comes back with, and {@code retrievePayments} says what became
     * of it once they are back.
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
     * Payments as they stand — by token, every attempt under one of the
     * merchant's own references, or the ones made between two days; the
     * refused ones included, oldest first.
     */
    public com.odemehub.response.PaymentList retrievePayments(com.odemehub.request.RetrievePayments payments) {
        return com.odemehub.response.PaymentList.fromBody(send(payments));
    }

    /**
     * Ask what the gateway's provider knows about a card by the head of its
     * number, and how an amount may be paid off on it. Nothing is charged and
     * nothing is written down.
     */
    public com.odemehub.response.Bin retrieveBin(com.odemehub.request.RetrieveBin bin) {
        return com.odemehub.response.Bin.fromBody(send(bin));
    }

    /**
     * Open an order to be paid on the gateway's own page. Every call opens a
     * new order under a new token, even under a reference sent before, so
     * keep the token it comes back with. Nothing is charged here; the
     * customer is sent to the address it comes back with and pays there.
     */
    public com.odemehub.response.OrderDetails createOrder(com.odemehub.request.CreateOrder order) {
        return com.odemehub.response.OrderDetails.fromBody(send(order));
    }

    /**
     * Orders as they stand, each with its customer.
     */
    public com.odemehub.response.OrderList retrieveOrders(com.odemehub.request.RetrieveOrders orders) {
        return com.odemehub.response.OrderList.fromBody(send(orders));
    }

    /**
     * Change an open order. Only what is sent is written.
     */
    public com.odemehub.response.OrderDetails updateOrder(com.odemehub.request.UpdateOrder order) {
        return com.odemehub.response.OrderDetails.fromBody(send(order));
    }

    /**
     * Open a payment link. Every call opens a new link under a new token,
     * even under a reference sent before, so keep the token it comes back
     * with. The address it comes back with is the link itself.
     */
    public com.odemehub.response.PaymentLinkDetails createPaymentLink(com.odemehub.request.CreatePaymentLink link) {
        return com.odemehub.response.PaymentLinkDetails.fromBody(send(link));
    }

    /**
     * Payment links as they stand, each with the latest fifty payment attempts
     * made on it and how many there have been in all.
     */
    public com.odemehub.response.PaymentLinkList retrievePaymentLinks(com.odemehub.request.RetrievePaymentLinks links) {
        return com.odemehub.response.PaymentLinkList.fromBody(send(links));
    }

    /**
     * Change a payment link, or switch it off. Only what is sent is written.
     */
    public com.odemehub.response.PaymentLinkDetails updatePaymentLink(com.odemehub.request.UpdatePaymentLink link) {
        return com.odemehub.response.PaymentLinkDetails.fromBody(send(link));
    }

    /**
     * Payments made at the team's links, each with what was paid, the link it
     * was made at, the payer as they billed themselves and, once it is paid,
     * the attempt that paid it. They are opened by the payers paying, never
     * by the merchant, so they are only asked after.
     */
    public com.odemehub.response.LinkPaymentList retrieveLinkPayments(com.odemehub.request.RetrieveLinkPayments linkPayments) {
        return com.odemehub.response.LinkPaymentList.fromBody(send(linkPayments));
    }

    /**
     * Open a subscription. Every call opens a new subscription under a new
     * token, even under a reference sent before, so keep the token it comes
     * back with. The customer is sent to the address it comes back with and
     * pays the first renewal there; the rest are taken from the card they pay
     * with.
     */
    public com.odemehub.response.SubscriptionDetails createSubscription(com.odemehub.request.CreateSubscription subscription) {
        return com.odemehub.response.SubscriptionDetails.fromBody(send(subscription));
    }

    /**
     * Subscriptions as they stand, each with its customer and the renewal it
     * is on.
     */
    public com.odemehub.response.SubscriptionList retrieveSubscriptions(com.odemehub.request.RetrieveSubscriptions subscriptions) {
        return com.odemehub.response.SubscriptionList.fromBody(send(subscriptions));
    }

    /**
     * Change a subscription, or call it off with the status
     * {@code cancelled}. Only what is sent is written. Nothing is given back
     * on a cancellation: the customer is served to the end of what they paid
     * for, and nothing is charged after that.
     */
    public com.odemehub.response.SubscriptionDetails updateSubscription(com.odemehub.request.UpdateSubscription subscription) {
        return com.odemehub.response.SubscriptionDetails.fromBody(send(subscription));
    }

    /**
     * Keep a card for a customer without making a payment on it.
     */
    public com.odemehub.response.SavedCardDetails createSavedCard(com.odemehub.request.CreateSavedCard savedCard) {
        return com.odemehub.response.SavedCardDetails.fromBody(send(savedCard));
    }

    /**
     * Kept cards — by token, every card of a customer by their reference, or
     * the ones kept between two days — each with its customer, the default
     * first.
     */
    public com.odemehub.response.SavedCardList retrieveSavedCards(com.odemehub.request.RetrieveSavedCards savedCards) {
        return com.odemehub.response.SavedCardList.fromBody(send(savedCards));
    }

    /**
     * Make a kept card the one the customer pays with unless they say
     * otherwise.
     */
    public com.odemehub.response.SavedCardDetails updateSavedCard(com.odemehub.request.UpdateSavedCard savedCard) {
        return com.odemehub.response.SavedCardDetails.fromBody(send(savedCard));
    }

    /**
     * Let go of a kept card, at the provider and here.
     */
    public com.odemehub.response.DeletedSavedCard deleteSavedCard(com.odemehub.request.DeleteSavedCard savedCard) {
        return com.odemehub.response.DeletedSavedCard.fromBody(send(savedCard));
    }

    /**
     * Read a word the gateway posted to one of the merchant's webhook
     * addresses. Hand it the request exactly as it arrived — the method, the
     * path of the address it came to (without the query string), the raw
     * body byte for byte and the two headers — and nothing in it is believed
     * until the signature is checked against the secret.
     *
     * <p>The word only names what it is about; ask the gateway what became of
     * it before acting on it. Answer with any 2xx once the word is taken; the
     * gateway tries again, up to five times, until it hears one.
     *
     * @throws SignatureException when the signature does not hold.
     */
    public com.odemehub.response.Webhook webhook(String method, String path, byte[] payload, String timestamp, String signature) {
        if (!verifyWebhook(method, path, payload, timestamp, signature)) {
            throw new SignatureException("Bildirimin imzası doğrulanamadı; bildirim ödeme geçidinden gelmemiş olabilir.");
        }

        return com.odemehub.response.Webhook.fromBody(decode(new String(payload, StandardCharsets.UTF_8), 0));
    }

    public com.odemehub.response.Webhook webhook(String method, String path, String payload, String timestamp, String signature) {
        return webhook(method, path, payload.getBytes(StandardCharsets.UTF_8), timestamp, signature);
    }

    /**
     * Whether a word that arrived at a webhook address was signed by the
     * gateway with this team's secret, recently enough to be taken. The path
     * is the address's own, with its leading slash and without the query
     * string; the body is the raw bytes as they arrived.
     */
    public boolean verifyWebhook(String method, String path, byte[] payload, String timestamp, String signature) {
        return this.signature.verify(method, path, payload, timestamp, signature);
    }

    public boolean verifyWebhook(String method, String path, String payload, String timestamp, String signature) {
        return verifyWebhook(method, path, payload.getBytes(StandardCharsets.UTF_8), timestamp, signature);
    }

    /**
     * Sign what is being asked for, hand it to the gateway and read the answer
     * back. The body is signed exactly as it is sent, byte for byte, together
     * with the moment, the method and the path, so it is written once and used
     * for both.
     */
    private JsonNode send(Message message) {
        String method = message.method();
        String path = options.path(message.path());
        byte[] body;

        try {
            body = JSON.writeValueAsBytes(message.toBody());
        } catch (JsonProcessingException exception) {
            throw new UnexpectedResponseException("İstek gövdesi JSON olarak yazılamadı: " + exception.getMessage(), 0);
        }

        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(options.url(message.path())))
            .timeout(options.getTimeout())
            .header(Options.API_KEY_HEADER, options.getApiKey())
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
