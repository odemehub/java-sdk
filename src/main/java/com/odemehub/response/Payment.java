package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The outcome of a payment, as the gateway reports it, whether it answers
 * straight away, is asked after, or posts the outcome back once the customer
 * is home from their bank. The shapes are the same, so a merchant reads them
 * the same way: how it went, which payment it was, and whose. The gateway's
 * own answers also say the payment in full: its state, what it was charged
 * and how ({@link #getTransaction()}).
 *
 * <p>A payment that was turned down is an outcome like any other and arrives
 * here; only answers that were never a payment outcome are thrown as
 * exceptions.
 */
public class Payment {

    private final Result result;
    private final PaymentTransaction transaction;
    private final PaymentCustomer customer;
    private final Conversion conversion;
    private final SavedCard savedCard;

    protected Payment(JsonNode body) {
        JsonNode conversion = body.path("conversion");

        this.result = Result.fromBody(body);
        this.transaction = PaymentTransaction.in(body.path("transaction"));
        this.customer = PaymentCustomer.in(body.path("customer"));
        this.conversion = conversion.isObject() ? Conversion.fromBody(conversion) : null;
        this.savedCard = SavedCard.in(body.path("saved_card"));
    }

    public static Payment fromBody(JsonNode body) {
        return new Payment(body);
    }

    public Result getResult() {
        return result;
    }

    /** The payment itself. */
    public PaymentTransaction getTransaction() {
        return transaction;
    }

    /** Who the payment was made for: the reference, if any, and the billing address. */
    public PaymentCustomer getCustomer() {
        return customer;
    }

    /**
     * What reached the card, for a payment the merchant's conversion rules
     * charged in another money than it was asked in; null for a payment
     * charged as it was asked.
     */
    public Conversion getConversion() {
        return conversion;
    }

    /**
     * The card the payment kept, for a payment that asked for one to be kept.
     * It is null while nothing was kept: because the payment did not go
     * through, because the provider handed nothing back, or because the
     * payment never asked.
     */
    public SavedCard getSavedCard() {
        return savedCard;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[result=" + result + ", transaction=" + transaction
            + ", savedCard=" + savedCard + ", conversion=" + conversion + "]";
    }
}
