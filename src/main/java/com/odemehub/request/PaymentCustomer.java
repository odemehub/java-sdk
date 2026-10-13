package com.odemehub.request;

import java.util.Map;

/**
 * The customer a payment is made for or a card is kept for: the whole
 * billing address, or what is known of it where the team fills the rest
 * from its default payer (Varsayılan Ödeyen), and the merchant's own key
 * for them. Nothing goes to a
 * shipping address here; the gateway turns down a payment or a kept card
 * that names one.
 *
 * <p>The reference may be left out of a payment, and the payer is then
 * nobody the team keeps; but a payment that keeps its card, one made with a
 * kept card and a card kept on its own have to carry it.
 */
public final class PaymentCustomer {

    private final String reference;
    private final Address billingAddress;

    private PaymentCustomer(Builder builder) {
        this.reference = builder.reference;
        this.billingAddress = Fields.required(builder.billingAddress, "billingAddress");
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "reference", reference,
            "billing_address", billingAddress.toBody()
        );
    }

    public static final class Builder {

        private String reference;
        private Address billingAddress;

        private Builder() {
        }

        /** The key the merchant keeps this customer under in its own system. */
        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        /** Where the customer is billed: all eight person fields, unless the team fills what is left out from its default payer; the three company fields together or not at all. */
        public Builder billingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public PaymentCustomer build() {
            return new PaymentCustomer(this);
        }
    }
}
