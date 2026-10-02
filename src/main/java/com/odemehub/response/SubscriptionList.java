package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Every subscription opened on a channel within a span of days, oldest
 * first, each with its customer.
 */
public final class SubscriptionList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Subscription> subscriptions;

    private SubscriptionList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.string(body.path("created_from"));
        this.createdTo = Read.string(body.path("created_to"));
        this.subscriptions = Read.list(body.path("subscriptions"), Subscription::listed);
    }

    public static SubscriptionList fromBody(JsonNode body) {
        return new SubscriptionList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day read, {@code YYYY-MM-DD} in the team's timezone. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day read. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public String toString() {
        return "SubscriptionList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", subscriptions=" + subscriptions + "]";
    }
}
