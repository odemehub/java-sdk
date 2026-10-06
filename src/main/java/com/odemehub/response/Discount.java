package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The coupon the payer put on an order, a subscription's first payment or a
 * payment at a link: the code they typed on the checkout page and what it
 * took off the lines, in the thing's own money. Coupons are only ever typed
 * there; nothing about them is sent through the gateway.
 */
public final class Discount {

    private final String code;
    private final String amount;

    private Discount(JsonNode discount) {
        this.code = Read.string(discount.path("code"));
        this.amount = Read.string(discount.path("amount"));
    }

    static Discount in(JsonNode discount) {
        return Read.object(discount) == null ? null : new Discount(discount);
    }

    /** The code the payer typed. */
    public String getCode() {
        return code;
    }

    /** What it took off the lines, with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Discount[code=" + code + ", amount=" + amount + "]";
    }
}
