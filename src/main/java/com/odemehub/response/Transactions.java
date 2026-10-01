package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Every attempt made under one of the merchant's own numbers on a channel,
 * oldest first, so they read as the attempts were made.
 */
public final class Transactions {

    private final Result result;
    private final List<Transaction> transactions;

    private Transactions(JsonNode body) {
        this.result = Result.fromBody(body);
        this.transactions = Read.list(body.path("transactions"), Transaction::fromBody);
    }

    public static Transactions fromBody(JsonNode body) {
        return new Transactions(body);
    }

    public Result getResult() {
        return result;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** The attempt that went through; null when none did. */
    public Transaction successful() {
        return transactions.stream().filter(Transaction::isSuccessful).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Transactions[result=" + result + ", transactions=" + transactions + "]";
    }
}
