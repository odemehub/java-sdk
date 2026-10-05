package com.odemehub.request;

import java.util.Map;

/**
 * The customer a payment is made for, an order or a subscription is opened
 * for, or a card is kept for: the merchant's own key for them, where they
 * are billed and, for goods, where the goods go.
 *
 * <p>The reference is what makes them one of the team's customers: the
 * customer is written under it once a payment for them goes through, and
 * their cards are kept for them and found again by it. It may be left out of
 * a payment or an order, and the payer is then nobody the team keeps; but a
 * payment that keeps its card, a card kept on its own and a subscription
 * have to carry it.
 *
 * <p>Payments and kept cards take the reference and the whole billing
 * address only; orders and subscriptions take any of the three parts.
 */
public final class Customer {

    private final String reference;
    private final Address billingAddress;
    private final Address shippingAddress;

    private Customer(Builder builder) {
        this.reference = builder.reference;
        this.billingAddress = builder.billingAddress;
        this.shippingAddress = builder.shippingAddress;
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "reference", reference,
            "billing_address", billingAddress == null ? null : billingAddress.toBody(),
            "shipping_address", shippingAddress == null ? null : shippingAddress.toBody()
        );
    }

    public static final class Builder {

        private String reference;
        private Address billingAddress;
        private Address shippingAddress;

        private Builder() {
        }

        /** The key the merchant keeps this customer under in its own system. */
        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        /** Where the customer is billed. */
        public Builder billingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        /** Where the goods go. Orders and subscriptions only. */
        public Builder shippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
