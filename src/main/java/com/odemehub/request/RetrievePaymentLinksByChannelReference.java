package com.odemehub.request;

import java.util.Map;

/**
 * Every payment link opened on a channel within a span of days, oldest
 * first. Give {@link ChannelMessage#ODEMEHUB_CHANNEL} as the channel to list
 * the links on the team's own ödemehub channel.
 */
public final class RetrievePaymentLinksByChannelReference extends RetrieveByChannelReference {

    /**
     * The last seven days on the client's channel.
     */
    public RetrievePaymentLinksByChannelReference() {
        this(null, null, null);
    }

    public RetrievePaymentLinksByChannelReference(String createdFrom, String createdTo) {
        this(createdFrom, createdTo, null);
    }

    public RetrievePaymentLinksByChannelReference(String createdFrom, String createdTo, String channelToken) {
        super(createdFrom, createdTo, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-payment-links-by-channel-reference";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.said(
            "channel_token", linkChannel(channelToken),
            "created_from", createdFrom(),
            "created_to", createdTo()
        );
    }
}
