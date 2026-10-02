package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one subscription: how the request went, the subscription,
 * and whose it is. The customer is said beside the subscription, as the
 * gateway answers it, and also on the subscription itself.
 */
public final class SubscriptionDetails {

    private final Result result;
    private final Subscription subscription;
    private final NamedCustomer customer;

    private SubscriptionDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.subscription = Subscription.fromBody(body);
        this.customer = NamedCustomer.in(body.path("customer"));
    }

    public static SubscriptionDetails fromBody(JsonNode body) {
        return new SubscriptionDetails(body);
    }

    public Result getResult() {
        return result;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    /** Whose the subscription is. */
    public NamedCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "SubscriptionDetails[result=" + result + ", subscription=" + subscription + ", customer=" + customer + "]";
    }
}
