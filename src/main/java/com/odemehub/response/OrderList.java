package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Orders asked after, each with its customer. The answer is always a list, oldest first, and an empty one when
 * nothing matched. The days are the ones the gateway used, when the records
 * were asked for by the days they were made on: the ones asked for, or the
 * last seven when none were.
 */
public final class OrderList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<Order> orders;

    private OrderList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.orders = Read.list(body.path("orders"), Order::listed);
    }

    public static OrderList fromBody(JsonNode body) {
        return new OrderList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone; null when they were asked for by token or reference. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
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
