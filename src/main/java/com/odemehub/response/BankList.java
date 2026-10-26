package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * The banks whose accounts may be added, each with the credentials it asks
 * for.
 */
public final class BankList {

    private final Result result;
    private final List<Bank> banks;

    private BankList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.banks = Read.list(body.path("banks"), Bank::fromBody);
    }

    public static BankList fromBody(JsonNode body) {
        return new BankList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    @Override
    public String toString() {
        return "BankList[result=" + result + ", banks=" + banks + "]";
    }
}
