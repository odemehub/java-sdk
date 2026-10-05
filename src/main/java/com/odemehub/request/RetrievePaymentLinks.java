package com.odemehub.request;

/**
 * Payment links asked after, each with how many payments were made on it and
 * the latest fifty of them.
 */
public final class RetrievePaymentLinks extends Retrieve {

    private RetrievePaymentLinks(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrievePaymentLinks byToken(String token) {
        return new RetrievePaymentLinks(Fields.required(token, "token"), null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrievePaymentLinks byReference(String reference) {
        return new RetrievePaymentLinks(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrievePaymentLinks between(String createdFrom, String createdTo) {
        return new RetrievePaymentLinks(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrievePaymentLinks latest() {
        return new RetrievePaymentLinks(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-payment-links";
    }
}
