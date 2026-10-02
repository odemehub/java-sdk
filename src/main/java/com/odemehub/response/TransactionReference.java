package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.PaymentStatus;

/**
 * The payment that paid an order: what the merchant gives back out of or
 * asks after, and what has become of its money since.
 */
public final class TransactionReference {

    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final PaymentStatus paymentStatus;

    private TransactionReference(JsonNode transaction) {
        this.token = Read.string(transaction.path("token"));
        this.channelToken = Read.string(transaction.path("channel_token"));
        this.channelReference = Read.string(transaction.path("channel_reference"));
        this.paymentStatus = PaymentStatus.from(Read.optionalString(transaction.path("payment_status")));
    }

    static TransactionReference in(JsonNode transaction) {
        return Read.object(transaction) == null ? null : new TransactionReference(transaction);
    }

    /** The payment's token in the gateway. */
    public String getToken() {
        return token;
    }

    /** The channel the payment came in on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The reference the payment was made under. */
    public String getChannelReference() {
        return channelReference;
    }

    /** What became of the money: paid, cancelled, refunded, partially refunded; null for a value this version does not know. */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return "TransactionReference[token=" + token + ", channelToken=" + channelToken + ", channelReference=" + channelReference + ", paymentStatus=" + paymentStatus + "]";
    }
}
