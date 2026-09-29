package com.odemehub.request;

import java.util.List;
import java.util.Map;

/**
 * An order opened to be paid on the gateway's own page. Nothing is charged
 * here: the answer carries the address to send the customer to, and they
 * give their card there. The customer is given whole, so the page asks for
 * nothing but the card.
 *
 * <p>What the order comes to is not sent. The gateway adds up the lines and
 * answers with the amount, so the total can never disagree with what it is
 * made up of.
 */
public final class OrderPayment extends ChannelMessage {

    private final String channelReference;
    private final String successUrl;
    private final Customer customer;
    private final List<OrderItem> items;
    private final String cancelUrl;
    private final String description;
    private final String currency;
    private final String paymentProviderToken;

    private OrderPayment(Builder builder) {
        super(builder.channelToken);
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.successUrl = Fields.required(builder.successUrl, "successUrl");
        this.customer = Fields.required(builder.customer, "customer");
        this.items = List.copyOf(Fields.required(builder.items, "items"));
        this.cancelUrl = builder.cancelUrl;
        this.description = builder.description;
        this.currency = builder.currency;
        this.paymentProviderToken = builder.paymentProviderToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "order-payment";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "order", Fields.said(
                "channel_token", channel(channelToken),
                "channel_reference", channelReference,
                "payment_provider_token", paymentProviderToken,
                "description", description,
                "currency", currency,
                "success_url", successUrl,
                "cancel_url", cancelUrl,
                "items", items.stream().map(OrderItem::toBody).toList()
            ),
            "customer", customer.toBody()
        );
    }

    public static final class Builder extends ChannelMessage.Builder<Builder> {

        private String channelReference;
        private String successUrl;
        private Customer customer;
        private List<OrderItem> items;
        private String cancelUrl;
        private String description;
        private String currency;
        private String paymentProviderToken;

        private Builder() {
        }

        /** The number the order is known by in the calling system. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
        }

        /** Where the customer is posted back to, with the signed outcome, once the order is paid. */
        public Builder successUrl(String successUrl) {
            this.successUrl = successUrl;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        /** What the order is made up of; at least one line. */
        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        /** Where the customer goes if they turn back without paying. */
        public Builder cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /** Three letters, e.g. TRY. Left out, the gateway takes the lira. */
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        /**
         * The payment account the order is paid through, by its token. Left
         * out, the merchant's Gate rules pick the account, and its default
         * account is used where none of them holds.
         */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public OrderPayment build() {
            return new OrderPayment(this);
        }
    }
}
