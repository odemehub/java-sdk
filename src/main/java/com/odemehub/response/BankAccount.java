package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A bank account whose movements the gateway reads. Its credentials are never
 * answered back.
 */
public final class BankAccount {

    private final String token;
    private final String provider;
    private final String name;
    private final String iban;
    private final String syncedAt;
    private final String lastError;
    private final String createdAt;

    private BankAccount(JsonNode bankAccount) {
        this.token = Read.string(bankAccount.path("token"));
        this.provider = Read.string(bankAccount.path("provider"));
        this.name = Read.string(bankAccount.path("name"));
        this.iban = Read.string(bankAccount.path("iban"));
        this.syncedAt = Read.optionalString(bankAccount.path("synced_at"));
        this.lastError = Read.nonEmptyString(bankAccount.path("last_error"));
        this.createdAt = Read.optionalString(bankAccount.path("created_at"));
    }

    public static BankAccount fromBody(JsonNode bankAccount) {
        return new BankAccount(bankAccount);
    }

    /**
     * The account an answer carries under this key, or null when it carries none.
     */
    static BankAccount in(JsonNode bankAccount) {
        return bankAccount.isObject() ? new BankAccount(bankAccount) : null;
    }

    /** The account's token in the gateway, which names it again later and its movements are asked for by. */
    public String getToken() {
        return token;
    }

    /** The bank the account is at, by its id: "enpara". */
    public String getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    /** In capitals, without spaces. */
    public String getIban() {
        return iban;
    }

    /** When the gateway last read the account at the bank, ISO-8601 in UTC; null until it first has. */
    public String getSyncedAt() {
        return syncedAt;
    }

    /** Why the last reading failed, in the bank's own words; null when it went through. */
    public String getLastError() {
        return lastError;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "BankAccount[token=" + token + ", provider=" + provider + ", name=" + name + ", iban=" + iban
            + ", syncedAt=" + syncedAt + ", lastError=" + lastError + "]";
    }
}
