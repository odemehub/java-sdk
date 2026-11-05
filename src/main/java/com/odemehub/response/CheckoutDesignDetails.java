package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about how the team's checkout pages are dressed: as asked
 * after, or as just changed.
 */
public final class CheckoutDesignDetails {

    private final Result result;
    private final CheckoutDesign checkoutDesign;

    private CheckoutDesignDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.checkoutDesign = CheckoutDesign.in(body.path("checkout_design"));
    }

    public static CheckoutDesignDetails fromBody(JsonNode body) {
        return new CheckoutDesignDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The design as it now stands. */
    public CheckoutDesign getCheckoutDesign() {
        return checkoutDesign;
    }

    @Override
    public String toString() {
        return "CheckoutDesignDetails[result=" + result + ", checkoutDesign=" + checkoutDesign + "]";
    }
}
