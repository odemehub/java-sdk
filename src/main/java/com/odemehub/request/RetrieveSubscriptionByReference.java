package com.odemehub.request;

/**
 * The latest subscription opened under one of the merchant's own references
 * on a channel, with its customer.
 */
public final class RetrieveSubscriptionByReference extends RetrieveByReference {

    public RetrieveSubscriptionByReference(String channelReference) {
        this(channelReference, null);
    }

    public RetrieveSubscriptionByReference(String channelReference, String channelToken) {
        super(channelReference, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-subscription-by-reference";
    }
}
