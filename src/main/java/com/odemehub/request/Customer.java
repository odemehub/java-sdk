package com.odemehub.request;

import java.util.Map;

/**
 * The customer an order or a subscription is opened for: the merchant's own
 * key for them, where they are billed and, for goods, where the goods go.
 * Any of the three parts may be given; what is left out is asked on the
 * checkout page. Payments and kept cards take a {@link PaymentCustomer}
 * instead.
 *
 * <p>The reference is what makes them one of the team's customers: the
 * customer is written under it once a payment for them goes through, and
 * their cards are kept for them and found again by it. It may be left out of
 * an order; a subscription has to carry it.
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

        /** Where the goods go: the eight person fields only; an address naming a company, a tax number or a tax office is turned down here. */
        public Builder shippingAddress(Address shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
