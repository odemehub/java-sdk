package com.odemehub.request;

import java.util.Map;

/**
 * A payment link asked after by the merchant's own reference for it on a
 * channel. Give {@link ChannelMessage#ODEMEHUB_CHANNEL} as the channel to
 * find a link on the team's own ödemehub channel, which is where the
 * panel opens its links.
 */
public final class RetrievePaymentLinkByReference extends RetrieveByReference {

    public RetrievePaymentLinkByReference(String channelReference) {
        this(channelReference, null);
    }

    public RetrievePaymentLinkByReference(String channelReference, String channelToken) {
        super(channelReference, channelToken);
    }

    @Override
    public String path() {
        return "retrieve-payment-link-by-reference";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.said(
            "channel_token", linkChannel(channelToken),
            "channel_reference", channelReference()
        );
    }
}
