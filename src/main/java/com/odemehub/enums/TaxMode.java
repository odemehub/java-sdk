package com.odemehub.enums;

/**
 * How a payment link whose amount the payer picks reads its tax rate against
 * what they pay: split out of it (100 paid is 83.33 and 16.67 tax at 20%),
 * or added on top of it (100 written is 120 charged). A link of lines keeps
 * the tax inside each line.
 */
public enum TaxMode {

    INCLUSIVE("inclusive"),
    EXCLUSIVE("exclusive");

    private final String value;

    TaxMode(String value) {
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
    public static TaxMode from(String value) {
        for (TaxMode member : values()) {
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
