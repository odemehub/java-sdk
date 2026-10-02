package com.odemehub.request;

import java.util.Map;

/**
 * A card kept for a customer without a payment being made on it. The
 * provider is told who the card belongs to, so the token it hands back is
 * held under that customer and the card can be charged again later. It is
 * kept under the channel and the customer's reference.
 *
 * <p>Providers without a card store of their own keep a card by charging a
 * small amount and giving it straight back; those need the security code,
 * and the ones with a real card store do not. It is never stored.
 */
public final class CreateSavedCard extends ChannelMessage {

    private final Customer customer;
    private final Card card;
    private final String paymentProviderToken;

    private CreateSavedCard(Builder builder) {
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
        return "create-saved-card";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        Map<String, Object> card = this.card.toBody();
        card.remove("should_save");

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

        /** Who the card belongs to: the reference and the whole billing address, both required. */
        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder card(Card card) {
            this.card = card;
            return this;
        }

        /** The payment account to keep the card at; it has to keep cards. Left out, the team's default account is used. */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public CreateSavedCard build() {
            return new CreateSavedCard(this);
        }
    }
}
