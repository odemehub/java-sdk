package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A bank account removed, with every movement read off it.
 */
public final class DeletedBankAccount {

    private final Result result;
    private final String bankAccountToken;

    private DeletedBankAccount(JsonNode body) {
        this.result = Result.fromBody(body);
        this.bankAccountToken = Read.string(body.path("bank_account").path("token"));
    }

    public static DeletedBankAccount fromBody(JsonNode body) {
        return new DeletedBankAccount(body);
    }

    public Result getResult() {
        return result;
    }

    /** The account's token. */
    public String getBankAccountToken() {
        return bankAccountToken;
    }

    @Override
    public String toString() {
        return "DeletedBankAccount[result=" + result + ", bankAccountToken=" + bankAccountToken + "]";
    }
}
