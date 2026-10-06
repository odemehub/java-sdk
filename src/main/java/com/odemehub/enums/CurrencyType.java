package com.odemehub.enums;

/**
 * Whether a payment link is paid in the one money the merchant set, or the
 * payer picks one of those the link offers.
 */
public enum CurrencyType {

    FIXED("fixed"),
    SELECTABLE("selectable");

    private final String value;

    CurrencyType(String value) {
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
    public static CurrencyType from(String value) {
        for (CurrencyType member : values()) {
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
