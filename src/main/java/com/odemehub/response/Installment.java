package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One way an amount may be paid off on a card.
 */
public final class Installment {

    private final int number;
    private final String amount;
    private final String total;

    private Installment(JsonNode installment) {
        this.number = Read.integer(installment.path("number"));
        this.amount = Read.string(installment.path("amount"));
        this.total = Read.string(installment.path("total"));
    }

    public static Installment fromBody(JsonNode installment) {
        return new Installment(installment);
    }

    public int getNumber() {
        return number;
    }

    /** What is charged each month, as digits with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    /** What the card is charged in all, the same way. */
    public String getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Installment[number=" + number + ", amount=" + amount + ", total=" + total + "]";
    }
}
