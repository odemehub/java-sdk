package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A payment charged straight to the card. A successful answer is a settled
 * payment.
 */
public final class RegularPayment extends Payment {

    private RegularPayment(JsonNode body) {
        super(body);
    }

    public static RegularPayment fromBody(JsonNode body) {
        return new RegularPayment(body);
    }
}
