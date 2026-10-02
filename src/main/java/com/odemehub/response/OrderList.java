package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Every order opened on a channel within a span of days, oldest first, each
 * with its customer.
 */
public final class OrderList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Order> orders;

    private OrderList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.string(body.path("created_from"));
        this.createdTo = Read.string(body.path("created_to"));
        this.orders = Read.list(body.path("orders"), Order::listed);
    }

    public static OrderList fromBody(JsonNode body) {
        return new OrderList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day read, {@code YYYY-MM-DD} in the team's timezone. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day read. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "OrderList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", orders=" + orders + "]";
    }
}
