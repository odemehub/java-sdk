package com.odemehub.enums;

/**
 * How often a subscription renews.
 */
public enum Period {

    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    ANNUALLY("annually");

    private final String value;

    Period(String value) {
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
    public static Period from(String value) {
        for (Period member : values()) {
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
