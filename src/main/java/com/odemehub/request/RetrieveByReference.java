package com.odemehub.request;

import java.util.Map;

/**
 * One record asked after by the merchant's own reference for it on a
 * channel. The latest record under that pair is answered: a merchant that
 * opened something and lost its token, or never heard back, finds it again
 * this way. Nothing is changed by asking.
 */
public abstract class RetrieveByReference extends ChannelMessage {

    private final String channelReference;

    /**
     * @param channelReference The reference the record was made under in the calling system.
     * @param channelToken     The channel to look on, or null for the client's own.
     */
    protected RetrieveByReference(String channelReference, String channelToken) {
        super(channelToken);
        this.channelReference = Fields.required(channelReference, "channelReference");
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.said(
            "channel_token", channel(channelToken),
            "channel_reference", channelReference
        );
    }

    /**
     * The reference the record was made under.
     */
    protected String channelReference() {
        return channelReference;
    }
}
