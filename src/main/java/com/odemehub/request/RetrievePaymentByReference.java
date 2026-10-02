package com.odemehub.request;

/**
 * The latest payment made under one of the merchant's own references on a
 * channel. A merchant that sent a payment and never heard the answer (the
 * connection dropped) finds out here whether it was made, without trying it
 * again.
 */
public final class RetrievePaymentByReference extends RetrieveByReference {

    public RetrievePaymentByReference(String channelReference) {
        this(channelReference, null);
    }

    public RetrievePaymentByReference(String channelReference, String channelToken) {
        super(channelReference, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-payment-by-reference";
    }
}
