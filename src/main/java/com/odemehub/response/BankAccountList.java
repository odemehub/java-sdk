package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Bank accounts asked after. The answer is always a list, oldest first, and
 * an empty one when nothing matched.
 */
public final class BankAccountList {

    private final Result result;
    private final List<BankAccount> bankAccounts;

    private BankAccountList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.bankAccounts = Read.list(body.path("bank_accounts"), BankAccount::fromBody);
    }

    public static BankAccountList fromBody(JsonNode body) {
        return new BankAccountList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    @Override
    public String toString() {
        return "BankAccountList[result=" + result + ", bankAccounts=" + bankAccounts + "]";
    }
}
