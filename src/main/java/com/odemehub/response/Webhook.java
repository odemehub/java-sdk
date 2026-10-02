package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.WebhookEvent;

/**
 * A word the gateway sent about something of the merchant's: an order
 * paid, a link paid, a subscription's state changed, a payment finished,
 * money given back. It goes to the addresses set for the thing's channel
 * under Webhook in the panel, as plain JSON signed the way every answer is.
 *
 * <p>It is a notification, never the answer. It names the thing by token —
 * and the payment beside it when money moved — and nothing else; ask the
 * gateway what became of it ({@code retrieveOrder}, {@code retrievePaymentLink},
 * {@code retrieveSubscription}, {@code retrievePayment}) and act on that. A
 * word may arrive more than once; the id tells the copies apart.
 */
public final class Webhook {

    private final String id;
    private final String event;
    private final String createdAt;
    private final String orderToken;
    private final String paymentLinkToken;
    private final String subscriptionToken;
    private final String transactionToken;

    private Webhook(JsonNode body) {
        this.id = Read.string(body.path("id"));
        this.event = Read.string(body.path("event"));
        this.createdAt = Read.nonEmptyString(body.path("created_at"));
        this.orderToken = Read.optionalString(body.path("order").path("token"));
        this.paymentLinkToken = Read.optionalString(body.path("payment_link").path("token"));
        this.subscriptionToken = Read.optionalString(body.path("subscription").path("token"));
        this.transactionToken = Read.optionalString(body.path("transaction").path("token"));
    }

    public static Webhook fromBody(JsonNode body) {
        return new Webhook(body);
    }

    /** The word's own token, the same on every delivery of it. */
    public String getId() {
        return id;
    }

    /** What happened, as the gateway writes it: {@code order.paid}, {@code subscription.active}... */
    public String getEvent() {
        return event;
    }

    /** What happened; null for an event this version does not know. */
    public WebhookEvent getWebhookEvent() {
        return WebhookEvent.from(event);
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** The order, for the {@code order.*} events. */
    public String getOrderToken() {
        return orderToken;
    }

    /** The payment link, for the {@code payment_link.*} events. */
    public String getPaymentLinkToken() {
        return paymentLinkToken;
    }

    /** The subscription, for the {@code subscription.*} events. */
    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    /** The payment: for the {@code transaction.*} events, and beside the thing wherever money moved at it. */
    public String getTransactionToken() {
        return transactionToken;
    }

    @Override
    public String toString() {
        return "Webhook[id=" + id + ", event=" + event + ", orderToken=" + orderToken + ", paymentLinkToken=" + paymentLinkToken
            + ", subscriptionToken=" + subscriptionToken + ", transactionToken=" + transactionToken + "]";
    }
}
