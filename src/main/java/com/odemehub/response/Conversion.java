package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * What reached the card, for a payment the merchant's conversion rules
 * charged in another money than it was asked in.
 */
public final class Conversion {

    private final String amount;
    private final String currency;
    private final String rate;

    private Conversion(JsonNode conversion) {
        this.amount = Read.string(conversion.path("amount"));
        this.currency = Read.string(conversion.path("currency"));
        this.rate = Read.string(conversion.path("rate"));
    }

    public static Conversion fromBody(JsonNode conversion) {
        return new Conversion(conversion);
    }

    /** What was taken from the card. */
    public String getAmount() {
        return amount;
    }

    /** The money it was taken in, e.g. TRY. */
    public String getCurrency() {
        return currency;
    }

    /** What a unit of the asked-for money was charged as, with any margin on top. */
    public String getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "Conversion[amount=" + amount + ", currency=" + currency + ", rate=" + rate + "]";
    }
}
