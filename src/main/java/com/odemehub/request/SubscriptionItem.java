package com.odemehub.request;

import java.util.Map;

/**
 * One line of what a subscription is for: one of the merchant's recurring
 * products, named by its own key for it. What it costs and how often it
 * comes round are the product's, as saved with {@code saveProduct()}.
 */
public final class SubscriptionItem {

    private final String channelReference;
    private final Integer quantity;
    private final String unitAmount;
    private final String image;

    private SubscriptionItem(Builder builder) {
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.quantity = builder.quantity;
        this.unitAmount = builder.unitAmount;
        this.image = builder.image;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * A line for one of the product saved under this key, at its own price.
     */
    public static SubscriptionItem of(String channelReference) {
        return builder().channelReference(channelReference).build();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "channel_reference", channelReference,
            "quantity", quantity,
            "unit_amount", unitAmount,
            "image", image
        );
    }

    public static final class Builder {

        private String channelReference;
        private Integer quantity;
        private String unitAmount;
        private String image;

        private Builder() {
        }

        /** The key the recurring product is saved under on the subscription's channel. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        /** Left out, the line is for one. */
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * The price of one for the first period only, as digits with the kurus
         * behind a point: an opening offer. The periods after it are charged at
         * the product's own price. Left out, the first period is charged at that
         * price too.
         */
        public Builder unitAmount(String unitAmount) {
            this.unitAmount = unitAmount;
            return this;
        }

        /** The https address of the picture shown at checkout for this line. Left out, the product's own picture is shown. */
        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public SubscriptionItem build() {
            return new SubscriptionItem(this);
        }
    }
}
