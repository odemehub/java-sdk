package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * The answer about one payment link: how the request went and the link.
 * Asked after by its token ({@code retrievePaymentLink()}), it also says how
 * many payment attempts were made on it and the latest fifty of them, newest
 * first; the other endpoints answer none.
 */
public final class PaymentLinkDetails {

    private final Result result;
    private final PaymentLink paymentLink;
    private final List<Transaction> transactions;
    private final Integer transactionsCount;

    private PaymentLinkDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.paymentLink = PaymentLink.fromBody(body.path("payment_link"));
        this.transactions = Read.list(body.path("payment_link").path("transactions"), Transaction::fromBody);
        this.transactionsCount = Read.optionalInteger(body.path("payment_link").path("transactions_count"));
    }

    public static PaymentLinkDetails fromBody(JsonNode body) {
        return new PaymentLinkDetails(body);
    }

    /** The attempts that went through. */
    public List<Transaction> successful() {
        return transactions.stream().filter(Transaction::isSuccessful).toList();
    }

    public Result getResult() {
        return result;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    /** The latest fifty attempts, newest first; empty but for {@code retrievePaymentLink()}. */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** How many attempts were made on the link in all; null but for {@code retrievePaymentLink()}. */
    public Integer getTransactionsCount() {
        return transactionsCount;
    }

    @Override
    public String toString() {
        return "PaymentLinkDetails[result=" + result + ", paymentLink=" + paymentLink + ", transactions=" + transactions + ", transactionsCount=" + transactionsCount + "]";
    }
}
