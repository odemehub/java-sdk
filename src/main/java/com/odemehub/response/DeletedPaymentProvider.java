package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A payment account removed.
 */
public final class DeletedPaymentProvider {

    private final Result result;
    private final String paymentProviderToken;

    private DeletedPaymentProvider(JsonNode body) {
        this.result = Result.fromBody(body);
        this.paymentProviderToken = Read.string(body.path("payment_provider").path("token"));
    }

    public static DeletedPaymentProvider fromBody(JsonNode body) {
        return new DeletedPaymentProvider(body);
    }

    public Result getResult() {
        return result;
    }

    /** The account's token. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    @Override
    public String toString() {
        return "DeletedPaymentProvider[result=" + result + ", paymentProviderToken=" + paymentProviderToken + "]";
    }
}
