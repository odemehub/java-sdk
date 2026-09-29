package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * An order opened to be paid on the gateway's own page.
 */
public final class OrderPayment {

    private final Result result;
    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final String amount;
    private final String currency;
    private final String status;
    private final String checkoutUrl;
    private final String customerChannelReference;

    private OrderPayment(JsonNode body) {
        JsonNode order = body.path("order");

        this.result = Result.fromBody(body);
        this.token = Read.string(order.path("token"));
        this.channelToken = Read.string(order.path("channel_token"));
        this.channelReference = Read.string(order.path("channel_reference"));
        this.amount = Read.string(order.path("amount"));
        this.currency = Read.string(order.path("currency"));
        this.status = Read.string(order.path("status"));
        this.checkoutUrl = Read.string(order.path("checkout_url"));
        this.customerChannelReference = Read.string(body.path("customer").path("channel_reference"));
    }

    public static OrderPayment fromBody(JsonNode body) {
        return new OrderPayment(body);
    }

    public Result getResult() {
        return result;
    }

    /** The order's token in the gateway. */
    public String getToken() {
        return token;
    }

    /** The channel the order was opened on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The number the order is known by in the calling system. */
    public String getChannelReference() {
        return channelReference;
    }

    /** What the order comes to, added up from its lines by the gateway. */
    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    /** Where the order stands: open until it is paid. */
    public String getStatus() {
        return status;
    }

    /** Where the customer has to be sent to pay. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** The merchant's own key for the customer the order is for. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    @Override
    public String toString() {
        return "OrderPayment[result=" + result + ", token=" + token + ", amount=" + amount + ", currency=" + currency
            + ", status=" + status + ", checkoutUrl=" + checkoutUrl + "]";
    }
}
