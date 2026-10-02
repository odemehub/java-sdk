package com.odemehub.request;

/**
 * Every payment attempt made on a channel within a span of days, oldest
 * first, the refused and the expired ones included, and the ones made on the
 * gateway's own checkout page too.
 */
public final class RetrievePaymentsByChannelReference extends RetrieveByChannelReference {

    /**
     * The last seven days on the client's channel.
     */
    public RetrievePaymentsByChannelReference() {
        this(null, null, null);
    }

    public RetrievePaymentsByChannelReference(String createdFrom, String createdTo) {
        this(createdFrom, createdTo, null);
    }

    public RetrievePaymentsByChannelReference(String createdFrom, String createdTo, String channelToken) {
        super(createdFrom, createdTo, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-payments-by-channel-reference";
    }
}
