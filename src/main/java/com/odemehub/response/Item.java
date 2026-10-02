package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One line of what an order, a subscription or a payment link is for, as it
 * was written down.
 */
public final class Item {

    private final String channelReference;
    private final String name;
    private final String image;
    private final int quantity;
    private final String unitAmount;
    private final String taxRate;

    private Item(JsonNode item) {
        this.channelReference = Read.nonEmptyString(item.path("channel_reference"));
        this.name = Read.string(item.path("name"));
        this.image = Read.nonEmptyString(item.path("image"));
        this.quantity = Read.integer(item.path("quantity"));
        this.unitAmount = Read.string(item.path("unit_amount"));
        this.taxRate = Read.string(item.path("tax_rate"));
    }

    public static Item fromBody(JsonNode item) {
        return new Item(item);
    }

    /** The merchant's own key for what is on the line; null when it gave none. */
    public String getChannelReference() {
        return channelReference;
    }

    public String getName() {
        return name;
    }

    /** The picture the line is shown with; null when it has none. */
    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    /** The price of one, tax included, as digits with the kurus behind a point. */
    public String getUnitAmount() {
        return unitAmount;
    }

    /** The tax inside the price, as a percentage. */
    public String getTaxRate() {
        return taxRate;
    }

    @Override
    public String toString() {
        return "Item[channelReference=" + channelReference + ", name=" + name + ", quantity=" + quantity + ", unitAmount=" + unitAmount + ", taxRate=" + taxRate + "]";
    }
}
