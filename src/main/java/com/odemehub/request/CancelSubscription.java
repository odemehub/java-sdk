package com.odemehub.request;

/**
 * A subscription called off. Nothing is given back: the customer keeps the
 * days they have already paid for and is served to the end of them, and
 * nothing is charged after that.
 */
public final class CancelSubscription extends SubscriptionMessage {

    public CancelSubscription(String subscriptionToken) {
        super(subscriptionToken);
    }

    @Override
    public String path() {
        return "cancel-subscription";
    }
}
