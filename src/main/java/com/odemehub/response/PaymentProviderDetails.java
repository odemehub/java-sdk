package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one payment account: added just now, or changed.
 */
public final class PaymentProviderDetails {

    private final Result result;
    private final PaymentProvider paymentProvider;

    private PaymentProviderDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.paymentProvider = PaymentProvider.in(body.path("payment_provider"));
    }

    public static PaymentProviderDetails fromBody(JsonNode body) {
        return new PaymentProviderDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The account as it now stands. */
    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    @Override
    public String toString() {
        return "PaymentProviderDetails[result=" + result + ", paymentProvider=" + paymentProvider + "]";
    }
}
