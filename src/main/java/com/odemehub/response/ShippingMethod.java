package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One way the goods of an order or a subscription may be sent.
 */
public final class ShippingMethod {

    private final String handle;
    private final String title;
    private final String amount;
    private final String taxRate;

    private ShippingMethod(JsonNode method) {
        this.handle = Read.string(method.path("handle"));
        this.title = Read.string(method.path("title"));
        this.amount = Read.string(method.path("amount"));
        this.taxRate = Read.string(method.path("tax_rate"));
    }

    public static ShippingMethod fromBody(JsonNode method) {
        return new ShippingMethod(method);
    }

    static ShippingMethod in(JsonNode method) {
        return Read.object(method) == null ? null : new ShippingMethod(method);
    }

    /** The merchant's own key for it. */
    public String getHandle() {
        return handle;
    }

    /** What the payer sees. */
    public String getTitle() {
        return title;
    }

    /** What it costs, tax included. */
    public String getAmount() {
        return amount;
    }

    /** The tax inside the amount, as a percentage. */
    public String getTaxRate() {
        return taxRate;
    }

    @Override
    public String toString() {
        return "ShippingMethod[handle=" + handle + ", title=" + title + ", amount=" + amount + ", taxRate=" + taxRate + "]";
    }
}
