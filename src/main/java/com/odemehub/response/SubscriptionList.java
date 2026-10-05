package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Subscriptions asked after, each with its customer. The answer is always a list, oldest first, and an empty one when
 * nothing matched. The days are the ones the gateway used, when the records
 * were asked for by the days they were made on: the ones asked for, or the
 * last seven when none were.
 */
public final class SubscriptionList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Subscription> subscriptions;

    private SubscriptionList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.subscriptions = Read.list(body.path("subscriptions"), Subscription::listed);
    }

    public static SubscriptionList fromBody(JsonNode body) {
        return new SubscriptionList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone; null when they were asked for by token or reference. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
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
