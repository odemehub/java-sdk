package com.odemehub.enums;

/**
 * Where a subscription stands. {@code CANCELLED} is also the one status a
 * merchant may send, to call a subscription off.
 */
public enum SubscriptionStatus {

    PENDING("pending"),
    ACTIVE("active"),
    PAST_DUE("past_due"),
    CANCELLED("cancelled"),
    COMPLETED("completed");

    private final String value;

    SubscriptionStatus(String value) {
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
    public static SubscriptionStatus from(String value) {
        for (SubscriptionStatus member : values()) {
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
