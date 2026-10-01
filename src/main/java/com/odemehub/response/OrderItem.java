package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One line of what an order is made up of, as it was written down when the
 * order was opened: filled in from the catalogue where the line said
 * nothing, and as the line said where it did.
 */
public final class OrderItem {

    private final String channelReference;
    private final String name;
    private final String image;
    private final int quantity;
    private final String unitAmount;
    private final String taxRate;
    private final String taxAmount;

    private OrderItem(JsonNode item) {
        this.channelReference = Read.string(item.path("channel_reference"));
        this.name = Read.string(item.path("name"));
        this.image = Read.optionalString(item.path("image"));
        this.quantity = Read.integer(item.path("quantity"));
        this.unitAmount = Read.string(item.path("unit_amount"));
        this.taxRate = Read.optionalString(item.path("tax_rate"));
        this.taxAmount = Read.optionalString(item.path("tax_amount"));
    }

    public static OrderItem fromBody(JsonNode item) {
        return new OrderItem(item);
    }

    /** The merchant's own key for what is on the line. */
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

    /** The price of one, as digits with the kurus behind a point. */
    public String getUnitAmount() {
        return unitAmount;
    }

    /** The tax included in the price, as a percentage; null for a line with no rate. */
    public String getTaxRate() {
        return taxRate;
    }

    /** The tax the line comes to; null for a line with no rate. */
    public String getTaxAmount() {
        return taxAmount;
    }

    @Override
    public String toString() {
        return "OrderItem[channelReference=" + channelReference + ", name=" + name + ", quantity=" + quantity
            + ", unitAmount=" + unitAmount + ", taxRate=" + taxRate + ", taxAmount=" + taxAmount + "]";
    }
}
