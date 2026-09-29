package com.odemehub.request;

import java.util.Map;

/**
 * One line of what an order is made up of. A line names one of the
 * merchant's products by its own key for it; whatever it leaves unsaid —
 * name, price, tax — is filled in from the product saved with
 * {@code saveProduct()}. What it does say holds for this order alone; the
 * product itself is never changed by an order.
 *
 * <p>A line whose key names no product still goes through, as long as it
 * brings its own name and price: something sold once and never again does
 * not have to be saved as a product first.
 */
public final class OrderItem {

    private final String channelReference;
    private final String name;
    private final Integer quantity;
    private final String unitAmount;
    private final String taxRate;

    private OrderItem(Builder builder) {
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.unitAmount = builder.unitAmount;
        this.taxRate = builder.taxRate;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * A line that takes everything from the product saved under this key.
     */
    public static OrderItem of(String channelReference) {
        return builder().channelReference(channelReference).build();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "channel_reference", channelReference,
            "name", name,
            "quantity", quantity,
            "unit_amount", unitAmount,
            "tax_rate", taxRate
        );
    }

    public static final class Builder {

        private String channelReference;
        private String name;
        private Integer quantity;
        private String unitAmount;
        private String taxRate;

        private Builder() {
        }

        /** The key the product is saved under on the order's channel. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        /** Left out, the product's own name is shown. */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** Left out, the line is for one. */
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        /** The price of one, as digits with the kurus behind a point. Left out, the product's own price is charged. */
        public Builder unitAmount(String unitAmount) {
            this.unitAmount = unitAmount;
            return this;
        }

        /** The tax included in the price, as a percentage, e.g. "20". Left out, the product's own rate is used. */
        public Builder taxRate(String taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
