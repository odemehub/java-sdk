package com.odemehub.request;

import java.util.List;
import java.util.Map;

/**
 * A subscription opened for a customer and paid for the first time on the
 * gateway's own page. Nothing is charged here: the answer carries the
 * address to send the customer to. The card is kept, because the periods to
 * come are taken from it.
 *
 * <p>The products subscribed to come round at the same frequency and are
 * priced in the same money, because a subscription is charged as one thing.
 */
public final class SubscriptionPayment extends ChannelMessage {

    private final String channelReference;
    private final List<SubscriptionItem> items;
    private final String successUrl;
    private final Customer customer;
    private final String cancelUrl;
    private final String webhookUrl;
    private final String paymentProviderToken;

    private SubscriptionPayment(Builder builder) {
        super(builder.channelToken);
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.items = List.copyOf(Fields.required(builder.items, "items"));
        this.successUrl = Fields.required(builder.successUrl, "successUrl");
        this.customer = Fields.required(builder.customer, "customer");
        this.cancelUrl = builder.cancelUrl;
        this.webhookUrl = builder.webhookUrl;
        this.paymentProviderToken = builder.paymentProviderToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "subscription-payment";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "subscription", Fields.said(
                "channel_token", channel(channelToken),
                "channel_reference", channelReference,
                "payment_provider_token", paymentProviderToken,
                "items", items.stream().map(SubscriptionItem::toBody).toList(),
                "success_url", successUrl,
                "cancel_url", cancelUrl,
                "webhook_url", webhookUrl
            ),
            "customer", customer.toBody()
        );
    }

    public static final class Builder extends ChannelMessage.Builder<Builder> {

        private String channelReference;
        private List<SubscriptionItem> items;
        private String successUrl;
        private Customer customer;
        private String cancelUrl;
        private String webhookUrl;
        private String paymentProviderToken;

        private Builder() {
        }

        /** The key the subscription is known by in the calling system. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        /** What is subscribed to; at least one line, each product once. */
        public Builder items(List<SubscriptionItem> items) {
            this.items = items;
            return this;
        }

        /** Where the customer is posted back to, with the signed outcome, once the first period is paid. */
        public Builder successUrl(String successUrl) {
            this.successUrl = successUrl;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        /** Where the customer goes if they turn back without paying. */
        public Builder cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        /** Where the merchant is told, signed, whenever the subscription's state changes. */
        public Builder webhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }

        /**
         * The payment account the subscription is paid through, by its token;
         * the card is kept there and renewals are taken there. Left out, the
         * merchant's Gate rules pick the account, and its default account is
         * used where none of them holds.
         */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public SubscriptionPayment build() {
            return new SubscriptionPayment(this);
        }
    }
}
