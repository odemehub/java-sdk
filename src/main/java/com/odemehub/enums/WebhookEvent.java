package com.odemehub.enums;

/**
 * What a webhook says happened. The first part is what it is about —
 * {@code order}, {@code payment_link}, {@code subscription},
 * {@code transaction} — and the webhook carries that thing's token.
 */
public enum WebhookEvent {

    ORDER_PAID("order.paid"),
    ORDER_PAYMENT_REFUNDED("order.payment_refunded"),
    ORDER_PAYMENT_CANCELLED("order.payment_cancelled"),
    PAYMENT_LINK_PAID("payment_link.paid"),
    PAYMENT_LINK_PAYMENT_REFUNDED("payment_link.payment_refunded"),
    PAYMENT_LINK_PAYMENT_CANCELLED("payment_link.payment_cancelled"),
    SUBSCRIPTION_ACTIVE("subscription.active"),
    SUBSCRIPTION_PAST_DUE("subscription.past_due"),
    SUBSCRIPTION_CANCELLED("subscription.cancelled"),
    SUBSCRIPTION_ENDED("subscription.ended"),
    SUBSCRIPTION_COMPLETED("subscription.completed"),
    SUBSCRIPTION_PAYMENT_REFUNDED("subscription.payment_refunded"),
    SUBSCRIPTION_PAYMENT_CANCELLED("subscription.payment_cancelled"),
    TRANSACTION_SUCCESSFUL("transaction.successful"),
    TRANSACTION_FAILED("transaction.failed"),
    TRANSACTION_EXPIRED("transaction.expired"),
    TRANSACTION_PAYMENT_REFUNDED("transaction.payment_refunded"),
    TRANSACTION_PAYMENT_CANCELLED("transaction.payment_cancelled");

    private final String value;

    WebhookEvent(String value) {
        this.value = value;
    }

    /** The value as the gateway writes it. */
    public String getValue() {
        return value;
    }

    /**
     * The member for a value the gateway sent, or null for one this version
     * of the SDK does not know yet, so a new value never breaks reading.
     */
    public static WebhookEvent from(String value) {
        for (WebhookEvent member : values()) {
            if (member.value.equals(value)) {
                return member;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
