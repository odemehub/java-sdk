package com.odemehub.request;

/**
 * Where an order stands: what it is for, what it comes to, whether it has
 * been paid and, if so, by which payment, and whose it is. The one call a
 * merchant holding nothing but the order's token can make.
 */
public final class RetrieveOrder extends RetrieveByToken {

    public RetrieveOrder(String token) {
        super(token);
    }

    @Override
    protected String endpoint() {
        return "retrieve-order";
    }
}
