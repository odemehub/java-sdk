package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Money given back out of a payment: a cancellation or a refund, answered
 * the way every payment is, with what was given back alongside.
 */
public final class GiveBack extends Payment {

    private final Refund refund;

    private GiveBack(JsonNode body) {
        super(body);
        this.refund = Refund.in(body.path("refund"));
    }

    public static GiveBack fromBody(JsonNode body) {
        return new GiveBack(body);
    }

    /** What was given back: how, and how much. */
    public Refund getRefund() {
        return refund;
    }

    @Override
    public String toString() {
        return "GiveBack[result=" + getResult() + ", transaction=" + getTransaction() + ", refund=" + refund + "]";
    }
}
