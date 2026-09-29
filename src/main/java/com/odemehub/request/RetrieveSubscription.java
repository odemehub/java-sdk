package com.odemehub.request;

/**
 * Where a subscription stands: what it is for, the period it is on and
 * whether that period has been paid for. Nothing is changed by asking.
 */
public final class RetrieveSubscription extends SubscriptionMessage {

    public RetrieveSubscription(String subscriptionToken) {
        super(subscriptionToken);
    }

    @Override
    public String path() {
        return "retrieve-subscription";
    }
}
