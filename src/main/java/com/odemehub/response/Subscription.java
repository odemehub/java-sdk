package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A subscription as it stands: what it is for, the period it is on and
 * whether that period has been paid for.
 */
public final class Subscription {

    private final Result result;
    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final List<SubscriptionItem> items;
    private final String status;
    private final String period;
    private final String amount;
    private final String currency;
    private final String startsAt;
    private final String endsAt;
    private final String paidAt;
    private final String cancelledAt;
    private final String checkoutUrl;
    private final Boolean isTest;
    private final String customerChannelReference;

    private Subscription(JsonNode body) {
        JsonNode subscription = body.path("subscription");

        this.result = Result.fromBody(body);
        this.token = Read.string(subscription.path("token"));
        this.channelToken = Read.string(subscription.path("channel_token"));
        this.channelReference = Read.string(subscription.path("channel_reference"));
        this.items = Read.list(subscription.path("items"), SubscriptionItem::fromBody);
        this.status = Read.string(subscription.path("status"));
        this.period = Read.string(subscription.path("period"));
        this.amount = Read.string(subscription.path("amount"));
        this.currency = Read.string(subscription.path("currency"));
        this.startsAt = Read.nonEmptyString(subscription.path("starts_at"));
        this.endsAt = Read.nonEmptyString(subscription.path("ends_at"));
        this.paidAt = Read.nonEmptyString(subscription.path("paid_at"));
        this.cancelledAt = Read.nonEmptyString(subscription.path("cancelled_at"));
        this.checkoutUrl = Read.nonEmptyString(subscription.path("checkout_url"));
        this.isTest = Read.optionalBool(subscription.path("is_test"));
        this.customerChannelReference = Read.nonEmptyString(body.path("customer").path("channel_reference"));
    }

    public static Subscription fromBody(JsonNode body) {
        return new Subscription(body);
    }

    /**
     * Whether the subscription is being paid for: a customer who has been
     * through the checkout and whose card has not since been turned away.
     */
    public boolean isActive() {
        return status.equals("active");
    }

    /**
     * Whether the first period has yet to be paid for. A subscription stays
     * here until the customer has been through the checkout.
     */
    public boolean isPending() {
        return status.equals("pending");
    }

    /**
     * Whether a period has been left unpaid: the card was tried and turned away
     * every time, and the customer has been asked to pay it themselves at the
     * checkout address.
     */
    public boolean isPastDue() {
        return status.equals("past_due");
    }

    /**
     * Whether it is over. A subscription that has been called off but is still
     * serving days that were paid for is not over yet — read
     * {@link #getCancelledAt()} for that.
     */
    public boolean isCancelled() {
        return status.equals("cancelled");
    }

    public Result getResult() {
        return result;
    }

    /** The subscription's token in the gateway; name it to ask after it later. */
    public String getToken() {
        return token;
    }

    /** The channel the subscription was opened on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The key the subscription is known by in the calling system. */
    public String getChannelReference() {
        return channelReference;
    }

    /** What is subscribed to. */
    public List<SubscriptionItem> getItems() {
        return items;
    }

    /** Where it stands: pending, active, past_due or cancelled. */
    public String getStatus() {
        return status;
    }

    /** How often a period comes round: monthly or yearly. */
    public String getPeriod() {
        return period;
    }

    /** What the period it is on costs, with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    /** When the period it is on began, once it has been paid for. */
    public String getStartsAt() {
        return startsAt;
    }

    /** When the period it is on runs out, which is when the next is charged. */
    public String getEndsAt() {
        return endsAt;
    }

    /** When the period it is on was paid for, if it has been. */
    public String getPaidAt() {
        return paidAt;
    }

    /** The day it was called off on, if it has been. */
    public String getCancelledAt() {
        return cancelledAt;
    }

    /** Where the customer pays the period it is on, while that is still owed. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** Whether it was paid for in the test environment; null until the first payment. */
    public Boolean isTest() {
        return isTest;
    }

    /** The merchant's own key for the customer, answered when the subscription is opened. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    @Override
    public String toString() {
        return "Subscription[result=" + result + ", token=" + token + ", channelReference=" + channelReference
            + ", status=" + status + ", period=" + period + ", amount=" + amount + ", currency=" + currency
            + ", endsAt=" + endsAt + ", checkoutUrl=" + checkoutUrl + ", items=" + items + "]";
    }
}
