package com.odemehub.request;

/**
 * Every order opened on a channel within a span of days, oldest first, each
 * with its customer.
 */
public final class RetrieveOrdersByChannelReference extends RetrieveByChannelReference {

    /**
     * The last seven days on the client's channel.
     */
    public RetrieveOrdersByChannelReference() {
        this(null, null, null);
    }

    public RetrieveOrdersByChannelReference(String createdFrom, String createdTo) {
        this(createdFrom, createdTo, null);
    }

    public RetrieveOrdersByChannelReference(String createdFrom, String createdTo, String channelToken) {
        super(createdFrom, createdTo, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-orders-by-channel-reference";
    }
}
