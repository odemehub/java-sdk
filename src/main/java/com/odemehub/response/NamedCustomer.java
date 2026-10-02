package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The customer of an order or a subscription, as they were written: the key
 * the merchant keeps them under (or the {@code guest-} one made up for a payer
 * it never named), where the bill goes, and where the goods go when somebody
 * said.
 */
public final class NamedCustomer {

    private final String reference;
    private final Address billingAddress;
    private final Address shippingAddress;

    private NamedCustomer(JsonNode customer) {
        this.reference = Read.string(customer.path("reference"));
        this.billingAddress = Address.in(customer.path("billing_address"));
        this.shippingAddress = Address.in(customer.path("shipping_address"));
    }

    static NamedCustomer in(JsonNode customer) {
        return Read.object(customer) == null ? null : new NamedCustomer(customer);
    }

    /** The merchant's own key for the customer. */
    public String getReference() {
        return reference;
    }

    /** Where they are billed, as far as it is known. */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /** Where the goods go; null when nobody said. */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    @Override
    public String toString() {
        return "NamedCustomer[reference=" + reference + ", billingAddress=" + billingAddress + ", shippingAddress=" + shippingAddress + "]";
    }
}
