package com.odemehub.request;

import java.util.Map;

/**
 * A product written down in the merchant's catalogue at the gateway, under
 * the merchant's own key for it on one of its channels. Order lines and
 * subscriptions name products by that key.
 *
 * <p>The same key on the same channel is the same product: sending it again
 * changes the one already saved rather than saving a second, so a merchant
 * can keep its own catalogue in step by sending every change as it happens.
 * A product is never deleted; it is taken off sale with
 * {@code isActive(false)}.
 */
public final class SaveProduct extends ChannelMessage {

    private final String channelReference;
    private final String name;
    private final String type;
    private final String amount;
    private final String taxRate;
    private final String period;
    private final String currency;
    private final Boolean isActive;
    private final String image;

    private SaveProduct(Builder builder) {
        super(builder.channelToken);
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.name = Fields.required(builder.name, "name");
        this.type = Fields.required(builder.type, "type");
        this.amount = Fields.required(builder.amount, "amount");
        this.taxRate = Fields.required(builder.taxRate, "taxRate");
        this.period = builder.period;
        this.currency = builder.currency;
        this.isActive = builder.isActive;
        this.image = builder.image;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "save-product";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "product", Fields.said(
                "channel_token", channel(channelToken),
                "channel_reference", channelReference,
                "name", name,
                "image", image,
                "type", type,
                "amount", amount,
                "currency", currency,
                "tax_rate", taxRate,
                "period", period,
                "is_active", isActive
            )
        );
    }

    public static final class Builder extends ChannelMessage.Builder<Builder> {

        private String channelReference;
        private String name;
        private String type;
        private String amount;
        private String taxRate;
        private String period;
        private String currency;
        private Boolean isActive;
        private String image;

        private Builder() {
        }

        /** The key the product is known by in the calling system. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** "simple" for something sold once, "recurring" for something subscribed to. */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /** The price of one, as digits with the kurus behind a point. */
        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        /** The tax included in the price, as a percentage, e.g. "20". */
        public Builder taxRate(String taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        /** How often a recurring product comes round: "monthly" or "annually". Only a recurring product has one. */
        public Builder period(String period) {
            this.period = period;
            return this;
        }

        /** Three letters, e.g. TRY. Left out, the gateway takes the lira. */
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        /** Whether it is on sale. Left out, it is. */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        /**
         * The https address of the picture the checkout shows it with. Left
         * out, the product keeps the picture it has; an empty string takes it
         * off.
         */
        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public SaveProduct build() {
            return new SaveProduct(this);
        }
    }
}
