package com.odemehub.enums;

/**
 * The network a card belongs to.
 */
public enum CardScheme {

    VISA("visa"),
    MASTERCARD("mastercard"),
    AMERICAN_EXPRESS("american_express"),
    TROY("troy"),
    DISCOVER("discover"),
    DINERS_CLUB("diners_club"),
    JCB("jcb"),
    UNIONPAY("unionpay"),
    MAESTRO("maestro");

    private final String value;

    CardScheme(String value) {
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
    public static CardScheme from(String value) {
        for (CardScheme member : values()) {
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
