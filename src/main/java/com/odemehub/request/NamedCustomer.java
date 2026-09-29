package com.odemehub.request;

import java.util.Map;

/**
 * A customer the gateway already knows, named and nothing more: the channel
 * they came in on and the merchant's own key for them there. It is what the
 * endpoints that only look a customer up take, such as listing the cards
 * kept for them.
 */
public final class NamedCustomer {

    private final String channelReference;

    public NamedCustomer(String channelReference) {
        this.channelReference = Fields.required(channelReference, "channelReference");
    }

    Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "channel_token", channelToken,
            "channel_reference", channelReference
        );
    }
}
