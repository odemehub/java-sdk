package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one kept card: kept just now, asked after, or made the
 * default.
 */
public final class SavedCardDetails {

    private final Result result;
    private final SavedCard savedCard;
    private final SavedCardCustomer customer;

    private SavedCardDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.savedCard = SavedCard.in(body.path("saved_card"));
        this.customer = SavedCardCustomer.in(body.path("customer"));
    }

    public static SavedCardDetails fromBody(JsonNode body) {
        return new SavedCardDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The card as it now stands; null when keeping it failed. */
    public SavedCard getSavedCard() {
        return savedCard;
    }

    /** Whose the card is. */
    public SavedCardCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "SavedCardDetails[result=" + result + ", savedCard=" + savedCard + ", customer=" + customer + "]";
    }
}
