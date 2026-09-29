package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A card kept, made the default or let go of.
 */
public final class KeptCard {

    private final Result result;
    private final SavedCard savedCard;
    private final String customerChannelReference;

    private KeptCard(JsonNode body) {
        this.result = Result.fromBody(body);
        this.savedCard = SavedCard.in(body.path("saved_card"));
        this.customerChannelReference = Read.string(body.path("customer").path("channel_reference"));
    }

    public static KeptCard fromBody(JsonNode body) {
        return new KeptCard(body);
    }

    public Result getResult() {
        return result;
    }

    /** The card as it now stands, or null when there was none to keep. */
    public SavedCard getSavedCard() {
        return savedCard;
    }

    /** The merchant's own key for the customer the card belongs to. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    @Override
    public String toString() {
        return "KeptCard[result=" + result + ", savedCard=" + savedCard + ", customerChannelReference=" + customerChannelReference + "]";
    }
}
