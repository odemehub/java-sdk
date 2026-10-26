package com.odemehub.request;

/**
 * Subscriptions asked after, each with its customer and the renewal it is on.
 */
public final class RetrieveSubscriptions extends Retrieve<RetrieveSubscriptions> {

    private RetrieveSubscriptions(String token, String reference, String createdFrom, String createdTo, Integer page, Integer perPage) {
        super(token, reference, createdFrom, createdTo, page, perPage, RetrieveSubscriptions::new);
    }

    /**
     * The one record with the token.
     */
    public static RetrieveSubscriptions byToken(String token) {
        return new RetrieveSubscriptions(Fields.required(token, "token"), null, null, null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrieveSubscriptions byReference(String reference) {
        return new RetrieveSubscriptions(null, Fields.required(reference, "reference"), null, null, null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrieveSubscriptions between(String createdFrom, String createdTo) {
        return new RetrieveSubscriptions(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"), null, null);
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrieveSubscriptions latest() {
        return new RetrieveSubscriptions(null, null, null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-subscriptions";
    }
}
