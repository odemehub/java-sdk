package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * The cards kept for a customer, the default one first, the rest oldest
 * first.
 */
public final class SavedCardList {

    private final Result result;
    private final List<SavedCard> savedCards;
    private final SavedCardCustomer customer;

    private SavedCardList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.savedCards = Read.list(body.path("saved_cards"), SavedCard::fromBody);
        this.customer = SavedCardCustomer.in(body.path("customer"));
    }

    public static SavedCardList fromBody(JsonNode body) {
        return new SavedCardList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<SavedCard> getSavedCards() {
        return savedCards;
    }

    /** Whose the cards are. */
    public SavedCardCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "SavedCardList[result=" + result + ", savedCards=" + savedCards + ", customer=" + customer + "]";
    }
}
