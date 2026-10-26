package com.odemehub.request;

/**
 * Payment links asked after, each with how many payments were made on it and
 * the latest fifty of them.
 */
public final class RetrievePaymentLinks extends Retrieve<RetrievePaymentLinks> {

    private RetrievePaymentLinks(String token, String reference, String createdFrom, String createdTo, Integer page, Integer perPage) {
        super(token, reference, createdFrom, createdTo, page, perPage, RetrievePaymentLinks::new);
    }

    /**
     * The one record with the token.
     */
    public static RetrievePaymentLinks byToken(String token) {
        return new RetrievePaymentLinks(Fields.required(token, "token"), null, null, null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrievePaymentLinks byReference(String reference) {
        return new RetrievePaymentLinks(null, Fields.required(reference, "reference"), null, null, null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrievePaymentLinks between(String createdFrom, String createdTo) {
        return new RetrievePaymentLinks(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"), null, null);
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrievePaymentLinks latest() {
        return new RetrievePaymentLinks(null, null, null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-payment-links";
    }
}
