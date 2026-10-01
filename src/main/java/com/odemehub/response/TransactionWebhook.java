package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Word the gateway sent about a payment the merchant started and the
 * customer finished — or did not — at their bank. It is the same answer
 * {@code retrievePayment} gives, with the state reached on top: the customer
 * may have closed the page before their browser could bring the outcome
 * back, and then this is the only word the merchant hears.
 */
public final class TransactionWebhook extends Payment {

    private final String event;

    private TransactionWebhook(JsonNode body) {
        super(body);
        this.event = Read.string(body.path("event"));
    }

    public static TransactionWebhook fromBody(JsonNode body) {
        return new TransactionWebhook(body);
    }

    /** The state reached: successful, failed or expired. */
    public String getEvent() {
        return event;
    }

    /** Whether the payment went through. */
    public boolean isSuccessful() {
        return event.equals("successful");
    }

    /** Whether the bank turned the payment away. */
    public boolean isFailed() {
        return event.equals("failed");
    }

    /** Whether the customer never opened the bank's page in time, so the payment was closed without being tried. */
    public boolean isExpired() {
        return event.equals("expired");
    }

    @Override
    public String toString() {
        return "TransactionWebhook[event=" + event + ", transactionToken=" + getTransactionToken()
            + ", channelReference=" + getChannelReference() + ", result=" + getResult() + "]";
    }
}
