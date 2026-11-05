package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A payment account of the team's. Its settings are never answered back.
 */
public final class PaymentProvider {

    private final String token;
    private final String name;
    private final String provider;
    private final boolean isDefault;
    private final String createdAt;

    private PaymentProvider(JsonNode paymentProvider) {
        this.token = Read.string(paymentProvider.path("token"));
        this.name = Read.string(paymentProvider.path("name"));
        this.provider = Read.string(paymentProvider.path("provider"));
        this.isDefault = Read.bool(paymentProvider.path("is_default"));
        this.createdAt = Read.optionalString(paymentProvider.path("created_at"));
    }

    public static PaymentProvider fromBody(JsonNode paymentProvider) {
        return new PaymentProvider(paymentProvider);
    }

    /**
     * The account an answer carries under this key, or null when it carries none.
     */
    static PaymentProvider in(JsonNode paymentProvider) {
        return paymentProvider.isObject() ? new PaymentProvider(paymentProvider) : null;
    }

    /** The account's token in the gateway, which a payment names it by as its {@code paymentProviderToken}. */
    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    /** The provider the account is at, by its id: "iyzico". */
    public String getProvider() {
        return provider;
    }

    /** Whether a payment that names no account goes through this one. */
    public boolean isDefault() {
        return isDefault;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PaymentProvider[token=" + token + ", name=" + name + ", provider=" + provider + ", isDefault=" + isDefault + "]";
    }
}
