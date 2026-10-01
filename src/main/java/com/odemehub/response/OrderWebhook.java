package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Word the gateway sent about one of the merchant's orders: that it was
 * paid, with the payment that paid it. An order is only ever told of once;
 * an attempt that fails leaves it open and the customer trying again.
 */
public final class OrderWebhook {

    private final String event;
    private final Order order;

    private OrderWebhook(JsonNode body) {
        this.event = Read.string(body.path("event"));
        this.order = Order.fromBody(body);
    }

    public static OrderWebhook fromBody(JsonNode body) {
        return new OrderWebhook(body);
    }

    /** Whether the order has been paid, which is the one thing said here. */
    public boolean isPaid() {
        return event.equals("paid");
    }

    /** The state reached: paid. */
    public String getEvent() {
        return event;
    }

    /** The order as it stands now, with the payment that paid it. */
    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "OrderWebhook[event=" + event + ", order=" + order + "]";
    }
}
