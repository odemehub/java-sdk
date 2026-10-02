package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Every payment attempt made on a channel within a span of days, oldest
 * first, with the span that was read.
 */
public final class PaymentList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Transaction> payments;

    private PaymentList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.string(body.path("created_from"));
        this.createdTo = Read.string(body.path("created_to"));
        this.payments = Read.list(body.path("payments"), Transaction::fromBody);
    }

    public static PaymentList fromBody(JsonNode body) {
        return new PaymentList(body);
    }

    /** The attempts that went through. */
    public List<Transaction> successful() {
        return payments.stream().filter(Transaction::isSuccessful).toList();
    }

    public Result getResult() {
        return result;
    }

    /** The first day read, {@code YYYY-MM-DD} in the team's timezone. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day read. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<Transaction> getPayments() {
        return payments;
    }

    @Override
    public String toString() {
        return "PaymentList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", payments=" + payments + "]";
    }
}
