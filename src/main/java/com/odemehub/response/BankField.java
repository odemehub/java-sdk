package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One credential a bank asks for before its accounts may be read.
 */
public final class BankField {

    private final String key;
    private final boolean required;
    private final String label;

    private BankField(JsonNode field) {
        this.key = Read.string(field.path("key"));
        this.required = Read.bool(field.path("required"));
        this.label = Read.string(field.path("label"));
    }

    public static BankField fromBody(JsonNode field) {
        return new BankField(field);
    }

    /** The key the credential is sent under in {@code settings}: "client_id". */
    public String getKey() {
        return key;
    }

    public boolean isRequired() {
        return required;
    }

    /** What the bank calls it: "Client ID". */
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "BankField[key=" + key + ", required=" + required + ", label=" + label + "]";
    }
}
