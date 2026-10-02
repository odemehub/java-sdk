package com.odemehub.request;

/**
 * The latest order opened under one of the merchant's own references on a
 * channel, with its customer.
 */
public final class RetrieveOrderByReference extends RetrieveByReference {

    public RetrieveOrderByReference(String channelReference) {
        this(channelReference, null);
    }

    public RetrieveOrderByReference(String channelReference, String channelToken) {
        super(channelReference, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-order-by-reference";
    }
}
