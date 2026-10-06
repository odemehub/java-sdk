package com.odemehub.request;

/**
 * Payments made at the team's links asked after: one by its token, one by
 * the number the gateway gave it ({@code LINKPAY1}, {@code LINKPAY2}...), or
 * the ones made between two days.
 */
public final class RetrieveLinkPayments extends Retrieve {

    private RetrieveLinkPayments(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrieveLinkPayments byToken(String token) {
        return new RetrieveLinkPayments(Fields.required(token, "token"), null, null, null);
    }

    /**
     * The number the gateway gave it: {@code LINKPAY1}. The link's own
     * reference does not name its payments.
     */
    public static RetrieveLinkPayments byReference(String reference) {
        return new RetrieveLinkPayments(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrieveLinkPayments between(String createdFrom, String createdTo) {
        return new RetrieveLinkPayments(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrieveLinkPayments latest() {
        return new RetrieveLinkPayments(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-link-payments";
    }
}
