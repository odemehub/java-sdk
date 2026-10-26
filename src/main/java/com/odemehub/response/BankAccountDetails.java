package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one bank account: added just now, or changed. An account
 * whose credentials the bank turned down is not added, or not changed; the
 * result says why, in the bank's words.
 */
public final class BankAccountDetails {

    private final Result result;
    private final BankAccount bankAccount;

    private BankAccountDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.bankAccount = BankAccount.in(body.path("bank_account"));
    }

    public static BankAccountDetails fromBody(JsonNode body) {
        return new BankAccountDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The account as it now stands; null when the bank turned the credentials down. */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    @Override
    public String toString() {
        return "BankAccountDetails[result=" + result + ", bankAccount=" + bankAccount + "]";
    }
}
