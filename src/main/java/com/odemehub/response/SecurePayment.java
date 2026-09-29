package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A 3D payment that has been started. A successful answer is not a settled
 * payment: the customer still has to be sent to the redirect address.
 */
public final class SecurePayment extends Payment {

    private final String redirectUrl;

    private SecurePayment(JsonNode body) {
        super(body);
        this.redirectUrl = Read.nonEmptyString(body.path("result").path("redirect_url"));
    }

    public static SecurePayment fromBody(JsonNode body) {
        return new SecurePayment(body);
    }

    /** Where the customer has to be sent. Always there when the payment started. */
    public String getRedirectUrl() {
        return redirectUrl;
    }
}
