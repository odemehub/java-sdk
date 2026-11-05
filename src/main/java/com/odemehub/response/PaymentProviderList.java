package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Payment accounts asked after. The answer is always a list, and an empty
 * one when nothing matched; it is not cut into pages.
 */
public final class PaymentProviderList {

    private final Result result;
    private final List<PaymentProvider> paymentProviders;

    private PaymentProviderList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.paymentProviders = Read.list(body.path("payment_providers"), PaymentProvider::fromBody);
    }

    public static PaymentProviderList fromBody(JsonNode body) {
        return new PaymentProviderList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<PaymentProvider> getPaymentProviders() {
        return paymentProviders;
    }

    @Override
    public String toString() {
        return "PaymentProviderList[result=" + result + ", paymentProviders=" + paymentProviders + "]";
    }
}
