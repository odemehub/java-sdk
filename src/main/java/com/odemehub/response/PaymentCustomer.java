package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The customer a payment was made for, as the payment's own copy keeps them:
 * it stays as it was however the thing paid for moves on.
 */
public final class PaymentCustomer {

    private final String reference;
    private final Address billingAddress;

    private PaymentCustomer(JsonNode customer) {
        this.reference = Read.nonEmptyString(customer.path("reference"));
        this.billingAddress = Address.in(customer.path("billing_address"));
    }

    static PaymentCustomer in(JsonNode customer) {
        return Read.object(customer) == null ? null : new PaymentCustomer(customer);
    }

    /** The merchant's own key for the customer; null for a payer nobody keeps a key for. */
    public String getReference() {
        return reference;
    }

    /** Where they were billed. */
    public Address getBillingAddress() {
        return billingAddress;
    }

    @Override
    public String toString() {
        return "PaymentCustomer[reference=" + reference + ", billingAddress=" + billingAddress + "]";
    }
}
