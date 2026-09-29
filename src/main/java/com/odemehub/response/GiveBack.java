package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Money given back out of a payment: a cancellation or a refund.
 */
public final class GiveBack extends Payment {

    private final String type;
    private final String amount;

    private GiveBack(JsonNode body) {
        super(body);
        JsonNode refund = body.path("refund");
        this.type = Read.string(refund.path("type"));
        this.amount = Read.optionalString(refund.path("amount"));
    }

    public static GiveBack fromBody(JsonNode body) {
        return new GiveBack(body);
    }

    /** Which of the two it was: a cancellation or a refund. */
    public String getType() {
        return type;
    }

    /** How much actually went back, whether or not it was asked for by name. */
    public String getAmount() {
        return amount;
    }
}
