package com.odemehub.request;

import java.util.Map;

/**
 * A card kept for a customer without a payment being made on it. The
 * provider is told who the card belongs to, so the token it hands back is
 * held under that customer and the card can be charged again later.
 *
 * <p>Providers without a card store of their own keep a card by charging one
 * lira and giving it straight back; those ask for the security code, and the
 * ones with a real card store do not.
 */
public final class SaveCard extends ChannelMessage {

    private final Customer customer;
    private final Card card;
    private final String paymentProviderToken;

    private SaveCard(Builder builder) {
        super(builder.channelToken);
        this.customer = Fields.required(builder.customer, "customer");
        this.card = Fields.required(builder.card, "card");
        this.paymentProviderToken = builder.paymentProviderToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "save-card";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        Map<String, Object> card = this.card.toBody();
        card.remove("should_save");

        if ("".equals(card.get("security_code"))) {
            card.remove("security_code");
        }

        return Fields.of(
            "saved_card", Fields.said(
                "channel_token", channel(channelToken),
                "payment_provider_token", paymentProviderToken
            ),
            "customer", customer.toBody(),
            "card", card
        );
    }

    public static final class Builder extends ChannelMessage.Builder<Builder> {

        private Customer customer;
        private Card card;
        private String paymentProviderToken;

        private Builder() {
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder card(Card card) {
            this.card = card;
            return this;
        }

        /** The payment account to keep the card at. Left out, the team's default account is used. */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public SaveCard build() {
            return new SaveCard(this);
        }
    }
}
