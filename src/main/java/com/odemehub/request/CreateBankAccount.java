package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A bank account whose movements are to be read. The gateway first asks the
 * bank whether the credentials read the account with the IBAN; one the bank
 * turns down is not added, and the answer says why. An added account is
 * named by the token the gateway gives it from then on.
 */
public final class CreateBankAccount extends Message {

    private final String provider;
    private final String name;
    private final String iban;
    private final Map<String, String> settings;

    private CreateBankAccount(Builder builder) {
        this.provider = Fields.required(builder.provider, "provider");
        this.name = Fields.required(builder.name, "name");
        this.iban = Fields.required(builder.iban, "iban");
        this.settings = new LinkedHashMap<>(Fields.required(builder.settings, "settings"));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-bank-account";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "bank_account", Fields.of(
                "provider", provider,
                "name", name,
                "iban", iban,
                "settings", new LinkedHashMap<>(settings)
            )
        );
    }

    public static final class Builder {

        private String provider;
        private String name;
        private String iban;
        private Map<String, String> settings;

        private Builder() {
        }

        /** The bank the account is at, by the id {@code retrieveBanks} lists it under: "enpara". */
        public Builder provider(String provider) {
            this.provider = provider;
            return this;
        }

        /** What the account is called in the panel and in the listings. */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** The account's IBAN; spaces and small letters are fine, the gateway writes it in capitals without them. */
        public Builder iban(String iban) {
            this.iban = iban;
            return this;
        }

        /**
         * Every credential the bank asks for, keyed the way
         * {@code retrieveBanks} lists them: for Enpara {@code client_id},
         * {@code client_secret}, {@code access_token} and
         * {@code refresh_token}. They are never answered back.
         */
        public Builder settings(Map<String, String> settings) {
            this.settings = settings;
            return this;
        }

        public CreateBankAccount build() {
            return new CreateBankAccount(this);
        }
    }
}
