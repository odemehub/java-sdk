package com.odemehub.request;

import java.util.Map;

/**
 * One way the goods of an order or a subscription may be sent, offered to
 * the payer on the checkout page. The one they pick is added to what they
 * pay. The handle is the merchant's own key for it and has to be unique
 * within the list; the amount includes the tax, the way an item's price
 * does.
 */
public final class ShippingMethod {

    private final String handle;
    private final String title;
    private final String amount;
    private final String taxRate;

    /**
     * @param title   What the payer sees, e.g. "Standart Kargo".
     * @param amount  What it costs, tax included, as digits with the kurus behind a point; "0" for free.
     * @param taxRate The tax inside the amount, as a percentage.
     */
    public ShippingMethod(String handle, String title, String amount, String taxRate) {
        this.handle = Fields.required(handle, "handle");
        this.title = Fields.required(title, "title");
        this.amount = Fields.required(amount, "amount");
        this.taxRate = Fields.required(taxRate, "taxRate");
    }

    Map<String, Object> toBody() {
        return Fields.of(
            "handle", handle,
            "title", title,
            "amount", amount,
            "tax_rate", taxRate
        );
    }

    @Override
    public String toString() {
        return "ShippingMethod[handle=" + handle + ", title=" + title + ", amount=" + amount + ", taxRate=" + taxRate + "]";
    }
}
