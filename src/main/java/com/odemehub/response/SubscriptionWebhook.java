package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Word the gateway sent about a subscription: the state it has reached and
 * the subscription as it stands now.
 */
public final class SubscriptionWebhook {

    private final String event;
    private final Subscription subscription;

    private SubscriptionWebhook(JsonNode body) {
        this.event = Read.string(body.path("event"));
        this.subscription = Subscription.fromBody(body);
    }

    public static SubscriptionWebhook fromBody(JsonNode body) {
        return new SubscriptionWebhook(body);
    }

    /**
     * Whether the subscription is being paid for: the customer has just paid a
     * period, whether the first or a later one.
     */
    public boolean isActive() {
        return event.equals("active");
    }

    /**
     * Whether a period was left unpaid. The card was tried and turned away
     * every time, and the customer has been asked to pay it themselves at the
     * subscription's checkout address.
     */
    public boolean isPastDue() {
        return event.equals("past_due");
    }

    /**
     * Whether the subscription has been called off. Nothing more will be
     * charged, but the customer is served until the period ends.
     */
    public boolean isCancelled() {
        return event.equals("cancelled");
    }

    /**
     * Whether it is over: the days that were paid for have run out and the
     * customer's access can be closed.
     */
    public boolean isEnded() {
        return event.equals("ended");
    }

    /** The state reached: active, past_due, cancelled or ended. */
    public String getEvent() {
        return event;
    }

    /** The subscription as it stands now. */
    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public String toString() {
        return "SubscriptionWebhook[event=" + event + ", subscription=" + subscription + "]";
    }
}
