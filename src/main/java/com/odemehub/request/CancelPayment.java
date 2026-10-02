package com.odemehub.request;

/**
 * The whole of a payment taken back before the provider has settled it.
 * There is no amount to name: a cancellation is always for the whole of the
 * payment, and a payment part of which has already been refunded cannot be
 * cancelled, only refunded for the rest.
 */
public final class CancelPayment extends PaymentMessage {

    /**
     * @param token The payment's token in the gateway.
     */
    public CancelPayment(String token) {
        super(token);
    }

    @Override
    public String path() {
        return "cancel-payment";
    }
}
