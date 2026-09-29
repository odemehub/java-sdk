package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * The cards a customer let the merchant keep, the default one first.
 */
public final class KeptCards {

    private final Result result;
    private final List<SavedCard> savedCards;
    private final String customerChannelReference;

    private KeptCards(JsonNode body) {
        this.result = Result.fromBody(body);
        this.savedCards = Read.list(body.path("saved_cards"), SavedCard::fromBody);
        this.customerChannelReference = Read.string(body.path("customer").path("channel_reference"));
    }

    public static KeptCards fromBody(JsonNode body) {
        return new KeptCards(body);
    }

    public Result getResult() {
        return result;
    }

    public List<SavedCard> getSavedCards() {
        return savedCards;
    }

    /** The merchant's own key for the customer the cards belong to. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    @Override
    public String toString() {
        return "KeptCards[result=" + result + ", savedCards=" + savedCards + ", customerChannelReference=" + customerChannelReference + "]";
    }
}
