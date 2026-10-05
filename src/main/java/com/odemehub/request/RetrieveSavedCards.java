package com.odemehub.request;

/**
 * Kept cards asked after: one by its token, every card of a customer by the
 * merchant's reference for them, or the ones kept between two days. A
 * customer's cards come with the one they pay with by default first.
 */
public final class RetrieveSavedCards extends Retrieve {

    private RetrieveSavedCards(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrieveSavedCards byToken(String token) {
        return new RetrieveSavedCards(Fields.required(token, "token"), null, null, null);
    }

    /**
     * Every card kept for the customer under the merchant's reference for them.
     */
    public static RetrieveSavedCards byReference(String reference) {
        return new RetrieveSavedCards(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrieveSavedCards between(String createdFrom, String createdTo) {
        return new RetrieveSavedCards(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrieveSavedCards latest() {
        return new RetrieveSavedCards(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-saved-cards";
    }

    @Override
    protected String referenceField() {
        return "customer_reference";
    }
}
