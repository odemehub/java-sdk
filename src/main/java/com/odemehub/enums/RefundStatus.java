package com.odemehub.enums;

/**
 * Where a refund stands. A pending one already counts against what the
 * payment has left to give back.
 */
public enum RefundStatus {

    PENDING("pending"),
    SUCCESSFUL("successful"),
    FAILED("failed");

    private final String value;

    RefundStatus(String value) {
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
    public static RefundStatus from(String value) {
        for (RefundStatus member : values()) {
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
