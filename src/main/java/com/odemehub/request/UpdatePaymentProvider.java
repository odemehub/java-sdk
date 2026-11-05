package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A change to a payment account, named by its token in the address and again
 * in the body: its name, the settings given, or its becoming the default,
 * the rest staying as they are. Its provider does not change.
 */
public final class UpdatePaymentProvider extends Message {

    private final String token;
    private final String name;
    private final Map<String, String> settings;
    private final Boolean isDefault;

    private UpdatePaymentProvider(Builder builder) {
        this.token = Fields.required(builder.token, "token");
        this.name = builder.name;
        this.settings = builder.settings == null ? null : new LinkedHashMap<>(builder.settings);
        this.isDefault = builder.isDefault;
    }

    /**
     * @param token The account's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-payment-provider/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "token", token,
            "payment_provider", Fields.said(
                "name", name,
                "settings", settings == null ? null : new LinkedHashMap<>(settings),
                "is_default", isDefault
            )
        );
    }

    public static final class Builder {

        private final String token;
        private String name;
        private Map<String, String> settings;
        private Boolean isDefault;

        private Builder(String token) {
            this.token = token;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** Only the settings to replace, keyed the way {@code retrieveProviders} lists them. */
        public Builder settings(Map<String, String> settings) {
            this.settings = settings;
            return this;
        }

        /**
         * Make the account the default, the one a payment goes through when
         * it names none. Only {@code true} means anything: an account stops
         * being the default when another is made it.
         */
        public Builder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public UpdatePaymentProvider build() {
            return new UpdatePaymentProvider(this);
        }
    }
}
