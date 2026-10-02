package com.odemehub.enums;

/**
 * Where a payment attempt got to.
 */
public enum TransactionStatus {

    STARTED("started"),
    REDIRECTED_TO_SECURE_PAGE("redirected_to_secure_page"),
    RETURNED_FROM_SECURE_PAGE("returned_from_secure_page"),
    TIMEOUT("timeout"),
    FAILED("failed"),
    EXPIRED("expired"),
    SUCCESSFUL("successful");

    private final String value;

    TransactionStatus(String value) {
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
    public static TransactionStatus from(String value) {
        for (TransactionStatus member : values()) {
            if (member.value.equals(value)) {
                return member;
            }
        }

        return null;
    }

    /**
     * Whether the attempt is over and will not move on by itself: it went
     * through, was turned down, or expired.
     */
    public boolean isFinished() {
        return this == SUCCESSFUL || this == FAILED || this == EXPIRED;
    }

    @Override
    public String toString() {
        return value;
    }
}
