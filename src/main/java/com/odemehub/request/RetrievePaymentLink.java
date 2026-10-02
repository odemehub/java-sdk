package com.odemehub.request;

/**
 * A payment link as it stands, with how many payment attempts were made on
 * it and the latest fifty of them, newest first, the refused ones included.
 * The rest are read through {@code retrievePaymentsByChannelReference()}.
 */
public final class RetrievePaymentLink extends RetrieveByToken {

    public RetrievePaymentLink(String token) {
        super(token);
    }

    @Override
    protected String endpoint() {
        return "retrieve-payment-link";
    }
}
