package com.odemehub.request;

import java.util.Map;

/**
 * Something asked of a subscription that has already been opened. The
 * subscription is named by the token the gateway gave it, and nothing else
 * is sent: the gateway holds the products, the customer, the channel and the
 * periods it has been through.
 */
public abstract class SubscriptionMessage extends Message {

    private final String subscriptionToken;

    /**
     * @param subscriptionToken The subscription's token in the gateway, as it answered when it was opened.
     */
    protected SubscriptionMessage(String subscriptionToken) {
        this.subscriptionToken = Fields.required(subscriptionToken, "subscriptionToken");
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of("subscription", Fields.of("token", subscriptionToken));
    }
}
