package com.odemehub.enums;

/**
 * Whether the money on a card is lent, drawn from an account or loaded
 * beforehand.
 */
public enum CardType {

    CREDIT("credit"),
    DEBIT("debit"),
    PREPAID("prepaid");

    private final String value;

    CardType(String value) {
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
    public static CardType from(String value) {
        for (CardType member : values()) {
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
