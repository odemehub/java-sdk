package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The customer a card is kept for, by the key the merchant keeps them under,
 * which is what the card is found by again.
 */
public final class SavedCardCustomer {

    private final String reference;

    private SavedCardCustomer(JsonNode customer) {
        this.reference = Read.string(customer.path("reference"));
    }

    static SavedCardCustomer in(JsonNode customer) {
        return new SavedCardCustomer(customer);
    }

    /** The merchant's own key for the customer. */
    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "SavedCardCustomer[reference=" + reference + "]";
    }
}
