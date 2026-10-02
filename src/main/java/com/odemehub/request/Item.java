package com.odemehub.request;

import java.util.Map;

/**
 * One line of what an order, a subscription or a payment link is for. The
 * unit price includes the tax: a line of 120 at 20% is 100 of goods and 20 of
 * tax, and the gateway splits it so. Nothing is looked up in a catalogue;
 * what is sent is what is sold. What the whole comes to is never sent: the
 * gateway adds the lines up and answers with the total.
 */
public final class Item {

    private final String name;
    private final String unitAmount;
    private final int quantity;
    private final String taxRate;
    private final String channelReference;
    private final String image;

    private Item(Builder builder) {
        this.name = Fields.required(builder.name, "name");
        this.unitAmount = Fields.required(builder.unitAmount, "unitAmount");
        this.quantity = Fields.required(builder.quantity, "quantity");
        this.taxRate = Fields.required(builder.taxRate, "taxRate");
        this.channelReference = builder.channelReference;
        this.image = builder.image;
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "channel_reference", channelReference,
            "name", name,
            "image", image,
            "quantity", quantity,
            "unit_amount", unitAmount,
            "tax_rate", taxRate
        );
    }

    @Override
    public String toString() {
        return "Item[name=" + name + ", unitAmount=" + unitAmount + ", quantity=" + quantity + ", taxRate=" + taxRate
            + ", channelReference=" + channelReference + "]";
    }

    public static final class Builder {

        private String name;
        private String unitAmount;
        private Integer quantity;
        private String taxRate;
        private String channelReference;
        private String image;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** The price of one, tax included, as digits with the kurus behind a point: "120.00". */
        public Builder unitAmount(String unitAmount) {
            this.unitAmount = unitAmount;
            return this;
        }

        /** 1 to 9999. */
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        /** The tax inside the price, as a percentage: "20" or "20.00". */
        public Builder taxRate(String taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        /** The merchant's own key for what is on the line, if it has one. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        /** The https address of the picture shown beside the line at checkout. */
        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
