package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A payment account to be added: the provider it is at, what it is called
 * and the settings the provider asks for. The settings are not tried against
 * the provider when the account is added. An added account is named by the
 * token the gateway gives it from then on.
 */
public final class CreatePaymentProvider extends Message {

    private final String provider;
    private final String name;
    private final Map<String, String> settings;

    private CreatePaymentProvider(Builder builder) {
        this.provider = Fields.required(builder.provider, "provider");
        this.name = Fields.required(builder.name, "name");
        this.settings = builder.settings == null ? null : new LinkedHashMap<>(builder.settings);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-payment-provider";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "payment_provider", Fields.said(
                "provider", provider,
                "name", name,
                "settings", settings == null ? null : new LinkedHashMap<>(settings)
            )
        );
    }

    public static final class Builder {

        private String provider;
        private String name;
        private Map<String, String> settings;

        private Builder() {
        }

        /** The provider, by the id {@code retrieveProviders} lists it under: "iyzico". */
        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        /** What the account is called in the panel and in the listings. */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The settings the provider asks for, keyed the way
         * {@code retrieveProviders} lists them. Every setting goes twice: the
         * live one under its key and the test one under the key with a
         * {@code test_} prefix; the ones the team's own environment uses are
         * the ones that must be given. They are never answered back.
         */
        public Builder settings(Map<String, String> settings) {
            this.settings = settings;
            return this;
        }

        public CreatePaymentProvider build() {
            return new CreatePaymentProvider(this);
        }
    }
}
