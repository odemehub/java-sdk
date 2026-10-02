package com.odemehub.request;

/**
 * Every subscription opened on a channel within a span of days, oldest
 * first, each with its customer.
 */
public final class RetrieveSubscriptionsByChannelReference extends RetrieveByChannelReference {

    /**
     * The last seven days on the client's channel.
     */
    public RetrieveSubscriptionsByChannelReference() {
        this(null, null, null);
    }

    public RetrieveSubscriptionsByChannelReference(String createdFrom, String createdTo) {
        this(createdFrom, createdTo, null);
    }

    public RetrieveSubscriptionsByChannelReference(String createdFrom, String createdTo, String channelToken) {
        super(createdFrom, createdTo, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-subscriptions-by-channel-reference";
    }
}
