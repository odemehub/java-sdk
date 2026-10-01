package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * An order as the gateway keeps it: what is being paid for, what it comes
 * to, where it stands and — once it is paid — the payment that paid it. The
 * same answer comes back whether the order has just been opened, asked
 * after, or the gateway is telling the merchant it was paid.
 *
 * <p>Nothing is charged when an order is opened: the customer has to be sent
 * to the checkout address and gives their card there. What becomes of it is
 * posted to the merchant's webhook address, if it gave one, and is always
 * there to be asked after by the order's token.
 */
public final class Order {

    private final Result result;
    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final String description;
    private final String status;
    private final List<OrderItem> items;
    private final String subtotal;
    private final String taxAmount;
    private final String amount;
    private final String currency;
    private final Boolean isTest;
    private final String createdAt;
    private final String checkoutUrl;
    private final String transactionToken;
    private final String customerChannelReference;

    private Order(JsonNode body) {
        JsonNode order = body.path("order");

        this.result = Result.fromBody(body);
        this.token = Read.string(order.path("token"));
        this.channelToken = Read.string(order.path("channel_token"));
        this.channelReference = Read.string(order.path("channel_reference"));
        this.description = Read.nonEmptyString(order.path("description"));
        this.status = Read.string(order.path("status"));
        this.items = Read.list(order.path("items"), OrderItem::fromBody);
        this.subtotal = Read.nonEmptyString(order.path("subtotal"));
        this.taxAmount = Read.nonEmptyString(order.path("tax_amount"));
        this.amount = Read.string(order.path("amount"));
        this.currency = Read.string(order.path("currency"));
        this.isTest = Read.optionalBool(order.path("is_test"));
        this.createdAt = Read.nonEmptyString(order.path("created_at"));
        this.checkoutUrl = Read.nonEmptyString(order.path("checkout_url"));
        this.transactionToken = Read.nonEmptyString(order.path("transaction").path("token"));
        this.customerChannelReference = Read.nonEmptyString(body.path("customer").path("channel_reference"));
    }

    public static Order fromBody(JsonNode body) {
        return new Order(body);
    }

    /** Whether the order has been paid. */
    public boolean isPaid() {
        return status.equals("paid");
    }

    public Result getResult() {
        return result;
    }

    /** The order's token in the gateway; name it to ask after it later. */
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

    public String getDescription() {
        return description;
    }

    /** Where the order stands: open until it is paid, then paid. */
    public String getStatus() {
        return status;
    }

    /** What the order is made up of. */
    public List<OrderItem> getItems() {
        return items;
    }

    /** What the lines come to before tax; null when no line carried a rate. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The tax the order carries; null when no line carried a rate. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What the order comes to, added up from its lines by the gateway. */
    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    /** Whether it was paid in the test environment; null until it is paid. */
    public Boolean getIsTest() {
        return isTest;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    /** Where the customer pays, while the order is still open; null once it is paid. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** The token of the payment that paid the order, which names it again for a refund; null while it is open. */
    public String getTransactionToken() {
        return transactionToken;
    }

    /** The merchant's own key for the customer the order is for; null for an order opened without one. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    @Override
    public String toString() {
        return "Order[result=" + result + ", token=" + token + ", channelReference=" + channelReference + ", status=" + status
            + ", amount=" + amount + ", currency=" + currency + ", checkoutUrl=" + checkoutUrl
            + ", transactionToken=" + transactionToken + "]";
    }
}
