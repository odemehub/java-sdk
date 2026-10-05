package com.odemehub.request;

/**
 * Payments asked after: one by its token, every attempt made under the
 * merchant's reference, or the ones made between two days — the ones the bank
 * turned away included.
 */
public final class RetrievePayments extends Retrieve {

    private RetrievePayments(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrievePayments byToken(String token) {
        return new RetrievePayments(Fields.required(token, "token"), null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrievePayments byReference(String reference) {
        return new RetrievePayments(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrievePayments between(String createdFrom, String createdTo) {
        return new RetrievePayments(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrievePayments latest() {
        return new RetrievePayments(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-payments";
    }
}
