package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import java.util.List;

/**
 * A payment link as it stands: what it sells, what it comes to, whether it
 * takes payments and until when, and the address it is paid at.
 */
public final class PaymentLink {

    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final String description;
    private final String paymentProviderToken;
    private final List<Item> items;
    private final String subtotal;
    private final String taxAmount;
    private final String amount;
    private final Currency currency;
    private final boolean isActive;
    private final boolean isTest;
    private final String expiresAt;
    private final String checkoutUrl;
    private final String createdAt;

    private PaymentLink(JsonNode link) {
        this.token = Read.string(link.path("token"));
        this.channelToken = Read.nonEmptyString(link.path("channel_token"));
        this.channelReference = Read.string(link.path("channel_reference"));
        this.description = Read.nonEmptyString(link.path("description"));
        this.paymentProviderToken = Read.nonEmptyString(link.path("payment_provider_token"));
        this.items = Read.list(link.path("items"), Item::fromBody);
        this.subtotal = Read.string(link.path("subtotal"));
        this.taxAmount = Read.string(link.path("tax_amount"));
        this.amount = Read.string(link.path("amount"));
        this.currency = Currency.from(Read.optionalString(link.path("currency")));
        this.isActive = Read.bool(link.path("is_active"));
        this.isTest = Read.bool(link.path("is_test"));
        this.expiresAt = Read.nonEmptyString(link.path("expires_at"));
        this.checkoutUrl = Read.nonEmptyString(link.path("checkout_url"));
        this.createdAt = Read.nonEmptyString(link.path("created_at"));
    }

    public static PaymentLink fromBody(JsonNode link) {
        return new PaymentLink(link);
    }

    /** The link's token in the gateway; name it to ask after it or change it. */
    public String getToken() {
        return token;
    }

    /** The channel the link is on; null for the team's own ödemehub channel. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The reference the link is known by. */
    public String getChannelReference() {
        return channelReference;
    }

    public String getDescription() {
        return description;
    }

    /** The account it is paid through; null when the Gate rules pick it at pay time. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    /** What the link is for. */
    public List<Item> getItems() {
        return items;
    }

    /** What the lines come to before tax. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The tax the lines carry. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What one payment on the link comes to. */
    public String getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    /** Whether it takes payments: switched on and its last day not gone by. */
    public boolean isActive() {
        return isActive;
    }

    /** Whether its payments are taken in the test environment now. */
    public boolean isTest() {
        return isTest;
    }

    /** The last moment it may be paid, ISO 8601 in UTC; null when it never runs out. */
    public String getExpiresAt() {
        return expiresAt;
    }

    /** The address it is paid at, while it can be paid; null otherwise. */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PaymentLink[token=" + token + ", channelReference=" + channelReference + ", amount=" + amount + ", currency=" + currency + ", isActive=" + isActive + ", expiresAt=" + expiresAt + ", checkoutUrl=" + checkoutUrl + "]";
    }
}
