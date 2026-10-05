package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Payments asked after, each with its state, amount, customer and what became
 * of its money, the ones the bank turned away included. The answer is always a list, oldest first, and an empty one when
 * nothing matched. The days are the ones the gateway used, when the records
 * were asked for by the days they were made on: the ones asked for, or the
 * last seven when none were.
 */
public final class PaymentList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Transaction> payments;

    private PaymentList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.payments = Read.list(body.path("payments"), Transaction::fromBody);
    }

    public static PaymentList fromBody(JsonNode body) {
        return new PaymentList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone; null when they were asked for by token or reference. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<Transaction> getPayments() {
        return payments;
    }

    /** The payments that went through. */
    public List<Transaction> successful() {
        return payments.stream().filter(Transaction::isSuccessful).toList();
    }

    @Override
    public String toString() {
        return "PaymentList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", payments=" + payments + "]";
    }
}
