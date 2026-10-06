package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import com.odemehub.enums.LinkPaymentStatus;
import java.util.List;

/**
 * A payment at a link, as the gateway keeps it: what was paid and the tax in
 * it, where it stands, the link it was made at, the payer as they billed
 * themselves and, once it is paid, the attempt that paid it, which is what
 * the merchant gives back out of or asks after. It is opened by the payer
 * paying at the link, never by the merchant, and keeps the lines it was paid
 * for however the link is changed after.
 */
public final class LinkPayment {

    private final String token;
    private final String reference;
    private final String paymentLinkToken;
    private final String paymentLinkReference;
    private final String paymentProviderToken;
    private final LinkPaymentStatus status;
    private final List<Item> items;
    private final String subtotal;
    private final String taxAmount;
    private final String amount;
    private final Discount discount;
    private final Currency currency;
    private final PaymentCustomer customer;
    private final boolean isTest;
    private final String createdAt;
    private final TransactionReference transaction;

    private LinkPayment(JsonNode linkPayment) {
        this.token = Read.string(linkPayment.path("token"));
        this.reference = Read.string(linkPayment.path("reference"));
        this.paymentLinkToken = Read.string(linkPayment.path("payment_link").path("token"));
        this.paymentLinkReference = Read.string(linkPayment.path("payment_link").path("reference"));
        this.paymentProviderToken = Read.nonEmptyString(linkPayment.path("payment_provider_token"));
        this.status = LinkPaymentStatus.from(Read.optionalString(linkPayment.path("status")));
        this.items = Read.list(linkPayment.path("items"), Item::fromBody);
        this.subtotal = Read.string(linkPayment.path("subtotal"));
        this.taxAmount = Read.string(linkPayment.path("tax_amount"));
        this.amount = Read.string(linkPayment.path("amount"));
        this.discount = Discount.in(linkPayment.path("discount"));
        this.currency = Currency.from(Read.optionalString(linkPayment.path("currency")));
        this.customer = PaymentCustomer.in(linkPayment.path("customer"));
        this.isTest = Read.bool(linkPayment.path("is_test"));
        this.createdAt = Read.nonEmptyString(linkPayment.path("created_at"));
        this.transaction = TransactionReference.in(linkPayment.path("transaction"));
    }

    public static LinkPayment fromBody(JsonNode linkPayment) {
        return new LinkPayment(linkPayment);
    }

    /** Whether a payment has gone through. */
    public boolean isPaid() {
        return status == LinkPaymentStatus.PAID;
    }

    /** The payment's token in the gateway; the same token the webhooks and the payments name it by. */
    public String getToken() {
        return token;
    }

    /** The number the gateway gave it: {@code LINKPAY1}, {@code LINKPAY2}... */
    public String getReference() {
        return reference;
    }

    /** The token of the link it was made at. */
    public String getPaymentLinkToken() {
        return paymentLinkToken;
    }

    /** The reference of the link it was made at. */
    public String getPaymentLinkReference() {
        return paymentLinkReference;
    }

    /** The account it was paid through; null until one was picked. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    /** Where it stands: open until a payment goes through, then paid; null for a state this version does not know. */
    public LinkPaymentStatus getStatus() {
        return status;
    }

    /** What was paid for, as it was when the payer paid. */
    public List<Item> getItems() {
        return items;
    }

    /** What the lines come to before tax, the coupon taken off. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The tax the lines carry, the coupon taken off. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What the payer paid, the coupon taken off. */
    public String getAmount() {
        return amount;
    }

    /** The coupon the payer put on it; null when they put none. */
    public Discount getDiscount() {
        return discount;
    }

    /** The money it was paid in, which the payer may have picked. */
    public Currency getCurrency() {
        return currency;
    }

    /** Who paid, as they billed themselves; null until they have said. The reference is always null: a payer at a link is never kept as one of the team's customers. */
    public PaymentCustomer getCustomer() {
        return customer;
    }

    /** Whether it was made in the test environment. */
    public boolean isTest() {
        return isTest;
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** The attempt that paid it; null while it is open. */
    public TransactionReference getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        return "LinkPayment[token=" + token + ", reference=" + reference + ", paymentLinkToken=" + paymentLinkToken + ", status=" + status + ", amount=" + amount + ", currency=" + currency + ", transaction=" + transaction + "]";
    }
}
