package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A change to a bank account, named by its token in the address and again in
 * the body: its name, and the credentials given, the rest staying as they
 * are. New credentials are tried against the bank first and kept only if it
 * lets them read the account; ones it turns down change nothing, and the
 * answer says why. Its bank and its IBAN do not change. A change that sends
 * neither a name nor credentials is turned down.
 */
public final class UpdateBankAccount extends Message {

    private final String token;
    private final String name;
    private final Map<String, String> settings;

    private UpdateBankAccount(Builder builder) {
        this.token = Fields.required(builder.token, "token");
        this.name = builder.name;
        this.settings = builder.settings == null ? null : new LinkedHashMap<>(builder.settings);
    }

    /**
     * @param token The account's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-bank-account/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "token", token,
            "bank_account", Fields.said(
                "name", name,
                "settings", settings == null ? null : new LinkedHashMap<>(settings)
            )
        );
    }

    public static final class Builder {

        private final String token;
        private String name;
        private Map<String, String> settings;

        private Builder(String token) {
            this.token = token;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** Only the credentials to replace, keyed the way {@code retrieveBanks} lists them. */
        public Builder settings(Map<String, String> settings) {
            this.settings = settings;
            return this;
        }

        public UpdateBankAccount build() {
            return new UpdateBankAccount(this);
        }
    }
}
