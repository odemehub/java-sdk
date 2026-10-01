package com.odemehub.request;

import java.util.Map;

/**
 * Where an order stands: what it is for, whether it has been paid and, if
 * so, by which payment. The order is named by the token the gateway gave it
 * when it was opened, which is all a merchant holds of an order whose
 * customer never came back from the checkout. Nothing is changed by asking.
 */
public final class RetrieveOrder extends Message {

    private final String orderToken;

    /**
     * @param orderToken The order's token in the gateway, as it answered when it was opened.
     */
    public RetrieveOrder(String orderToken) {
        this.orderToken = Fields.required(orderToken, "orderToken");
    }

    @Override
    public String path() {
        return "retrieve-order";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of("order", Fields.of("token", orderToken));
    }
}
