package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import com.odemehub.enums.Period;
import com.odemehub.enums.SubscriptionStatus;
import java.util.List;

/**
 * A subscription as it stands: what it is written as, how often it renews,
 * where it stands, the renewal it is on, when the next falls due, whose it is
 * and the address a renewal still owed is paid at. The same subscription
 * comes back whether it has just been opened, changed, asked after, listed,
 * or the gateway is telling the merchant it moved on.
 */
public final class Subscription {

    private final String token;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final SubscriptionStatus status;
    private final boolean requiresShipping;
    private final boolean locksCustomer;
    private final boolean emailsCustomer;
    private final Period period;
    private final Integer renewalLimit;
    private final int renewalsPaid;
    private final List<Item> items;
    private final ShippingMethod shippingMethod;
    private final Discount discount;
    private final String subtotal;
    private final String shippingAmount;
    private final String taxAmount;
    private final String amount;
    private final Currency currency;
    private final boolean isTest;
    private final Renewal renewal;
    private final String nextPaymentAt;
    private final String cancelledAt;
    private final String createdAt;
    private final String checkoutUrl;
    private final NamedCustomer customer;

    private Subscription(JsonNode subscription, JsonNode customer) {
        this.token = Read.string(subscription.path("token"));
        this.reference = Read.string(subscription.path("reference"));
        this.description = Read.nonEmptyString(subscription.path("description"));
        this.paymentProviderToken = Read.nonEmptyString(subscription.path("payment_provider_token"));
        this.status = SubscriptionStatus.from(Read.optionalString(subscription.path("status")));
        this.requiresShipping = Read.bool(subscription.path("requires_shipping"));
        this.locksCustomer = Read.bool(subscription.path("locks_customer"));
        this.emailsCustomer = Read.bool(subscription.path("emails_customer"));
        this.period = Period.from(Read.optionalString(subscription.path("period")));
        this.renewalLimit = Read.optionalInteger(subscription.path("renewal_limit"));
        this.renewalsPaid = Read.integer(subscription.path("renewals_paid"));
        this.items = Read.list(subscription.path("items"), Item::fromBody);
        this.shippingMethod = ShippingMethod.in(subscription.path("shipping_method"));
        this.discount = Discount.in(subscription.path("discount"));
        this.subtotal = Read.string(subscription.path("subtotal"));
        this.shippingAmount = Read.string(subscription.path("shipping_amount"));
        this.taxAmount = Read.string(subscription.path("tax_amount"));
        this.amount = Read.string(subscription.path("amount"));
        this.currency = Currency.from(Read.optionalString(subscription.path("currency")));
        this.isTest = Read.bool(subscription.path("is_test"));
        this.renewal = Renewal.in(subscription.path("renewal"));
        this.nextPaymentAt = Read.nonEmptyString(subscription.path("next_payment_at"));
        this.cancelledAt = Read.nonEmptyString(subscription.path("cancelled_at"));
        this.createdAt = Read.nonEmptyString(subscription.path("created_at"));
        this.checkoutUrl = Read.nonEmptyString(subscription.path("checkout_url"));
        this.customer = NamedCustomer.in(customer);
    }

    /**
     * The subscription an answer carries under {@code subscription}, with
     * the customer beside it.
     */
    public static Subscription fromBody(JsonNode body) {
        return new Subscription(body.path("subscription"), body.path("customer"));
    }

    /**
     * A subscription a list carries, its customer inside it.
     */
    static Subscription listed(JsonNode subscription) {
        return new Subscription(subscription, subscription.path("customer"));
    }

    /** Whether the first renewal has yet to be paid. */
    public boolean isPending() {
        return status == SubscriptionStatus.PENDING;
    }

    /** Whether it is being paid for. A subscription called off but still serving a paid renewal is active until that ends. */
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    /** Whether a renewal was left unpaid: the kept card was turned away and the customer has been asked to pay at the checkout address. */
    public boolean isPastDue() {
        return status == SubscriptionStatus.PAST_DUE;
    }

    /** Whether it has been called off and is over. */
    public boolean isCancelled() {
        return status == SubscriptionStatus.CANCELLED;
    }

    /** Whether every renewal of its limit has been paid and served. */
    public boolean isCompleted() {
        return status == SubscriptionStatus.COMPLETED;
    }

    /** The subscription's token in the gateway; name it to ask after it or change it. */
    public String getToken() {
        return token;
    }


    /** The key the subscription is known by in the calling system. */
    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    /** The account it is paid through; null when the Gate rules pick it at pay time. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    /** Where it stands; null for a state this version does not know. */
    public SubscriptionStatus getStatus() {
        return status;
    }

    /** Whether the checkout page asks the payer where the goods go. */
    public boolean requiresShipping() {
        return requiresShipping;
    }

    /** Whether the customer stays as sent, shown and not asked on the checkout page. */
    public boolean locksCustomer() {
        return locksCustomer;
    }

    /** Whether the customer is sent an e-mail at their billing address. */
    public boolean emailsCustomer() {
        return emailsCustomer;
    }

    /** How often a renewal comes round. */
    public Period getPeriod() {
        return period;
    }

    /** How many renewals are paid in all; null when it runs until it is called off. */
    public Integer getRenewalLimit() {
        return renewalLimit;
    }

    /** How many renewals have been paid so far. */
    public int getRenewalsPaid() {
        return renewalsPaid;
    }

    /** What is subscribed to. */
    public List<Item> getItems() {
        return items;
    }


    /** The way the payer picked; null until they have, or when none was offered. */
    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    /**
     * The coupon the payer put on the first payment, the only one that takes
     * a coupon; null when they put none. The subscription's own amounts are
     * without it: what the first payment charged is on the first renewal's
     * amount.
     */
    public Discount getDiscount() {
        return discount;
    }

    /** What the lines come to before tax. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The shipping picked, before tax. */
    public String getShippingAmount() {
        return shippingAmount;
    }

    /** The tax the lines and the shipping carry. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What a renewal comes to, added up by the gateway. */
    public String getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    /** Whether it belongs to the test environment. */
    public boolean isTest() {
        return isTest;
    }

    /** The renewal it is on now. */
    public Renewal getRenewal() {
        return renewal;
    }

    /** When the next renewal is charged; null when none is. */
    public String getNextPaymentAt() {
        return nextPaymentAt;
    }

    /** When it was called off; null when it has not been. */
    public String getCancelledAt() {
        return cancelledAt;
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** Where the customer pays the renewal it is on, while that is owed; null otherwise. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** Whose the subscription is. */
    public NamedCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Subscription[token=" + token + ", reference=" + reference + ", status=" + status + ", period=" + period + ", amount=" + amount + ", currency=" + currency + ", nextPaymentAt=" + nextPaymentAt + ", checkoutUrl=" + checkoutUrl + "]";
    }
}
