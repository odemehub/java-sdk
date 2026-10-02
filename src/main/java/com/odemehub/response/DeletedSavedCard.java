package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A kept card let go of, at the provider and with the gateway. A card the
 * provider would not let go of stays; the result says why.
 */
public final class DeletedSavedCard {

    private final Result result;
    private final String savedCardToken;
    private final SavedCardCustomer customer;

    private DeletedSavedCard(JsonNode body) {
        this.result = Result.fromBody(body);
        this.savedCardToken = Read.string(body.path("saved_card").path("token"));
        this.customer = SavedCardCustomer.in(body.path("customer"));
    }

    public static DeletedSavedCard fromBody(JsonNode body) {
        return new DeletedSavedCard(body);
    }

    public Result getResult() {
        return result;
    }

    /** The card's token. */
    public String getSavedCardToken() {
        return savedCardToken;
    }

    /** Whose the card was. */
    public SavedCardCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "DeletedSavedCard[result=" + result + ", savedCardToken=" + savedCardToken + ", customer=" + customer + "]";
    }
}
