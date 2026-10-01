package com.odemehub.request;

import java.util.Map;

/**
 * Every attempt at paying something the merchant names by its own number on
 * a channel: the order number it opened an order with, or started a payment
 * with. How many times the customer tried, which were refused and which went
 * through. Nothing is changed by asking.
 */
public final class RetrieveTransactions extends ChannelMessage {

    private final String channelReference;

    /**
     * @param channelReference The number the payments were made under in the calling system.
     */
    public RetrieveTransactions(String channelReference) {
        this(channelReference, null);
    }

    public RetrieveTransactions(String channelReference, String channelToken) {
        super(channelToken);
        this.channelReference = Fields.required(channelReference, "channelReference");
    }

    @Override
    public String path() {
        return "retrieve-transactions";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "transaction", Fields.of(
                "channel_token", channel(channelToken),
                "channel_reference", channelReference
            )
        );
    }
}
