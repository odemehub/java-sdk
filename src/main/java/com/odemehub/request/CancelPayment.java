package com.odemehub.request;

/**
 * The whole of a payment taken back before the provider has settled it.
 * There is no amount to name: a cancellation is always for the whole of the
 * payment, and anything less goes back as a refund.
 */
public final class CancelPayment extends PaymentMessage {

    public CancelPayment(String transactionToken) {
        super(transactionToken);
    }

    @Override
    public String path() {
        return "cancel-payment";
    }
}
