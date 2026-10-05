package com.odemehub.request;

/**
 * Subscriptions asked after, each with its customer and the renewal it is on.
 */
public final class RetrieveSubscriptions extends Retrieve {

    private RetrieveSubscriptions(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrieveSubscriptions byToken(String token) {
        return new RetrieveSubscriptions(Fields.required(token, "token"), null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrieveSubscriptions byReference(String reference) {
        return new RetrieveSubscriptions(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrieveSubscriptions between(String createdFrom, String createdTo) {
        return new RetrieveSubscriptions(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrieveSubscriptions latest() {
        return new RetrieveSubscriptions(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-subscriptions";
    }
}
