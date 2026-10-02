package com.odemehub.request;

/**
 * Where a subscription stands: what it is for, the renewal it is on and
 * whether that has been paid, when the next is due, whether it has been
 * called off, and whose it is.
 */
public final class RetrieveSubscription extends RetrieveByToken {

    public RetrieveSubscription(String token) {
        super(token);
    }

    @Override
    protected String endpoint() {
        return "retrieve-subscription";
    }
}
