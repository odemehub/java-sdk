package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One line of what a subscription is for.
 */
public final class SubscriptionItem {

    private final String channelReference;
    private final String name;
    private final int quantity;
    private final String unitAmount;
    private final String taxRate;

    private SubscriptionItem(JsonNode item) {
        this.channelReference = Read.string(item.path("channel_reference"));
        this.name = Read.string(item.path("name"));
        this.quantity = Read.integer(item.path("quantity"));
        this.unitAmount = Read.string(item.path("unit_amount"));
        this.taxRate = Read.optionalString(item.path("tax_rate"));
    }

    public static SubscriptionItem fromBody(JsonNode item) {
        return new SubscriptionItem(item);
    }

    /** The merchant's own key for the product. */
    public String getChannelReference() {
        return channelReference;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    /** The price of one, as digits with the kurus behind a point. */
    public String getUnitAmount() {
        return unitAmount;
    }

    /** The tax included in the price, as a percentage. */
    public String getTaxRate() {
        return taxRate;
    }

    @Override
    public String toString() {
        return "SubscriptionItem[channelReference=" + channelReference + ", name=" + name + ", quantity=" + quantity
            + ", unitAmount=" + unitAmount + ", taxRate=" + taxRate + "]";
    }
}
