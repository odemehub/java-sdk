package com.odemehub.request;

/**
 * How a payment went, asked for after the fact. A customer sent to their
 * bank comes back carrying the payment's token and nothing more, because a
 * browser cannot be given anything to sign with; this is the call that says
 * what became of it.
 */
public final class RetrievePayment extends PaymentMessage {

    public RetrievePayment(String transactionToken) {
        super(transactionToken);
    }

    @Override
    public String path() {
        return "retrieve-payment";
    }
}
