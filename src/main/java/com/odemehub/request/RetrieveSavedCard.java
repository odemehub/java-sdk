package com.odemehub.request;

/**
 * One kept card, by its token, with the reference of the customer it is kept
 * for.
 */
public final class RetrieveSavedCard extends RetrieveByToken {

    public RetrieveSavedCard(String token) {
        super(token);
    }

    @Override
    protected String endpoint() {
        return "retrieve-saved-card";
    }
}
