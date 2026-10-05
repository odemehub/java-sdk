package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import com.odemehub.enums.OrderStatus;
import java.util.List;

/**
 * An order as the gateway keeps it: what is being paid for, what it comes
 * to, where it stands, whose it is and, once it is paid, the payment that
 * paid it. The same order comes back whether it has just been opened,
 * changed, asked after, listed, or the gateway is telling the merchant it was
 * paid.
 */
public final class Order {

    private final String token;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final OrderStatus status;
    private final List<Item> items;
    private final ShippingMethod shippingMethod;
    private final String subtotal;
    private final String shippingAmount;
    private final String taxAmount;
    private final String amount;
    private final Currency currency;
    private final boolean isTest;
    private final String createdAt;
    private final String checkoutUrl;
    private final TransactionReference transaction;
    private final NamedCustomer customer;

    private Order(JsonNode order, JsonNode customer) {
        this.token = Read.string(order.path("token"));
        this.reference = Read.string(order.path("reference"));
        this.description = Read.nonEmptyString(order.path("description"));
        this.paymentProviderToken = Read.nonEmptyString(order.path("payment_provider_token"));
        this.status = OrderStatus.from(Read.optionalString(order.path("status")));
        this.items = Read.list(order.path("items"), Item::fromBody);
        this.shippingMethod = ShippingMethod.in(order.path("shipping_method"));
        this.subtotal = Read.string(order.path("subtotal"));
        this.shippingAmount = Read.string(order.path("shipping_amount"));
        this.taxAmount = Read.string(order.path("tax_amount"));
        this.amount = Read.string(order.path("amount"));
        this.currency = Currency.from(Read.optionalString(order.path("currency")));
        this.isTest = Read.bool(order.path("is_test"));
        this.createdAt = Read.nonEmptyString(order.path("created_at"));
        this.checkoutUrl = Read.nonEmptyString(order.path("checkout_url"));
        this.transaction = TransactionReference.in(order.path("transaction"));
        this.customer = NamedCustomer.in(customer);
    }

    /**
     * The order an answer carries under {@code order}, with the customer
     * beside it.
     */
    public static Order fromBody(JsonNode body) {
        return new Order(body.path("order"), body.path("customer"));
    }

    /**
     * An order a list carries, its customer inside it.
     */
    static Order listed(JsonNode order) {
        return new Order(order, order.path("customer"));
    }

    /** Whether the order has been paid. */
    public boolean isPaid() {
        return status == OrderStatus.PAID;
    }

    /** The order's token in the gateway; name it to ask after it or change it. */
    public String getToken() {
        return token;
    }


    /** The number the order is known by in the calling system. */
    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    /** The account it is paid through; null when the Gate rules pick it at pay time. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    /** Where the order stands: open until it is paid, then paid; null for a state this version does not know. */
    public OrderStatus getStatus() {
        return status;
    }

    /** What the order is made up of. */
    public List<Item> getItems() {
        return items;
    }


    /** The way the payer picked; null until they have, or when none was offered. */
    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    /** What the lines come to before tax. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The shipping picked, before tax. */
    public String getShippingAmount() {
        return shippingAmount;
    }

    /** The tax the lines and the shipping carry. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What the order comes to, added up by the gateway. */
    public String getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    /** Whether it belongs to the test environment. */
    public boolean isTest() {
        return isTest;
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** Where the customer pays, while the order can be paid; null otherwise. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** The payment that paid the order; null while it is open. */
    public TransactionReference getTransaction() {
        return transaction;
    }

    /** Whose the order is; null while nobody has said who pays. */
    public NamedCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Order[token=" + token + ", reference=" + reference + ", status=" + status + ", amount=" + amount + ", currency=" + currency + ", checkoutUrl=" + checkoutUrl + ", transaction=" + transaction + "]";
    }
}
