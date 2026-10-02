package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one order: how the request went, the order, and whose it
 * is. The customer is said beside the order, as the gateway answers it, and
 * also on the order itself.
 */
public final class OrderDetails {

    private final Result result;
    private final Order order;
    private final NamedCustomer customer;

    private OrderDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.order = Order.fromBody(body);
        this.customer = NamedCustomer.in(body.path("customer"));
    }

    public static OrderDetails fromBody(JsonNode body) {
        return new OrderDetails(body);
    }

    public Result getResult() {
        return result;
    }

    public Order getOrder() {
        return order;
    }

    /** Whose the order is; null while nobody has said who pays. */
    public NamedCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "OrderDetails[result=" + result + ", order=" + order + ", customer=" + customer + "]";
    }
}
