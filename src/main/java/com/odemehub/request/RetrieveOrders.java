package com.odemehub.request;

/**
 * Orders asked after, each with its customer.
 */
public final class RetrieveOrders extends Retrieve {

    private RetrieveOrders(String token, String reference, String createdFrom, String createdTo) {
        super(token, reference, createdFrom, createdTo);
    }

    /**
     * The one record with the token.
     */
    public static RetrieveOrders byToken(String token) {
        return new RetrieveOrders(Fields.required(token, "token"), null, null, null);
    }

    /**
     * The merchant's own reference for them.
     */
    public static RetrieveOrders byReference(String reference) {
        return new RetrieveOrders(null, Fields.required(reference, "reference"), null, null);
    }

    /**
     * The records made between two days, both included, as {@code YYYY-MM-DD}.
     */
    public static RetrieveOrders between(String createdFrom, String createdTo) {
        return new RetrieveOrders(null, null, Fields.required(createdFrom, "createdFrom"), Fields.required(createdTo, "createdTo"));
    }

    /**
     * The records made in the last seven days.
     */
    public static RetrieveOrders latest() {
        return new RetrieveOrders(null, null, null, null);
    }

    @Override
    public String path() {
        return "retrieve-orders";
    }
}
