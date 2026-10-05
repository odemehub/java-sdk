package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Kept cards asked after, each with the customer it is kept for. A customer's
 * cards come with the one they pay with by default first. The answer is always a list, oldest first, and an empty one when
 * nothing matched. The days are the ones the gateway used, when the records
 * were asked for by the days they were made on: the ones asked for, or the
 * last seven when none were.
 */
public final class SavedCardList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<SavedCard> savedCards;

    private SavedCardList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.savedCards = Read.list(body.path("saved_cards"), SavedCard::fromBody);
    }

    public static SavedCardList fromBody(JsonNode body) {
        return new SavedCardList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone; null when they were asked for by token or reference. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<SavedCard> getSavedCards() {
        return savedCards;
    }

    /** The card the customer pays with unless they say otherwise, when the cards were asked for by the customer's reference. */
    public SavedCard defaultCard() {
        return savedCards.stream().filter(SavedCard::isDefault).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "SavedCardList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", savedCards=" + savedCards + "]";
    }
}
