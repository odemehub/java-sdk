package com.odemehub.enums;

/**
 * How a payment was made: confirmed by the customer at their bank (3D), or
 * charged straight to the card.
 */
public enum SecurityType {

    SECURE("secure"),
    REGULAR("regular");

    private final String value;

    SecurityType(String value) {
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
    public static SecurityType from(String value) {
        for (SecurityType member : values()) {
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
