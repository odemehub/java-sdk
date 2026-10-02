package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.RefundType;

/**
 * What was given back out of a payment.
 */
public final class Refund {

    private final RefundType type;
    private final String amount;

    private Refund(JsonNode refund) {
        this.type = RefundType.from(Read.optionalString(refund.path("type")));
        this.amount = Read.string(refund.path("amount"));
    }

    static Refund in(JsonNode refund) {
        return Read.object(refund) == null ? null : new Refund(refund);
    }

    /** A cancellation or a refund; null for a kind this version does not know. */
    public RefundType getType() {
        return type;
    }

    /** How much was asked to go back, whether or not it was named in the request. */
    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Refund[type=" + type + ", amount=" + amount + "]";
    }
}
