package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A page of the movements on the accounts asked after, oldest first, the
 * movements of every account woven together by when the bank booked them;
 * an empty one when nothing matched. The days are the ones the gateway used:
 * the ones asked for, or the last seven when none were.
 */
public final class BankTransactionList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final Pagination pagination;
    private final List<BankTransaction> bankTransactions;

    private BankTransactionList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.pagination = Pagination.fromBody(body.path("pagination"));
        this.bankTransactions = Read.list(body.path("bank_transactions"), BankTransaction::fromBody);
    }

    public static BankTransactionList fromBody(JsonNode body) {
        return new BankTransactionList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
    public String getCreatedTo() {
        return createdTo;
    }

    /** Which page this is and how many there are; ask for the next with {@code page(...)}. */
    public Pagination getPagination() {
        return pagination;
    }

    public List<BankTransaction> getBankTransactions() {
        return bankTransactions;
    }

    @Override
    public String toString() {
        return "BankTransactionList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo
            + ", pagination=" + pagination + ", bankTransactions=" + bankTransactions + "]";
    }
}
