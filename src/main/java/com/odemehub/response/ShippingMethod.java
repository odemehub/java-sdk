package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The way the payer picked to have the goods sent, from the team's own list,
 * as it was copied onto the order or the subscription. The amount includes
 * the tax.
 */
public final class ShippingMethod {

    private final String reference;
    private final String title;
    private final String amount;
    private final String taxRate;

    private ShippingMethod(JsonNode method) {
        this.reference = Read.string(method.path("reference"));
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

    /** The merchant's own key for the way, on the team's list. */
    public String getReference() {
        return reference;
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
        return "ShippingMethod[reference=" + reference + ", title=" + title + ", amount=" + amount + ", taxRate=" + taxRate + "]";
    }
}
