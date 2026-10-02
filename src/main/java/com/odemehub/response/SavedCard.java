package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.CardScheme;

/**
 * A card a customer let the merchant keep.
 */
public final class SavedCard {

    private final String token;
    private final String paymentProviderToken;
    private final String holderName;
    private final CardScheme scheme;
    private final String firstDigits;
    private final String lastFourDigit;
    private final String expiryMonth;
    private final String expiryYear;
    private final boolean isDefault;
    private final String createdAt;

    private SavedCard(JsonNode card) {
        this.token = Read.string(card.path("token"));
        this.paymentProviderToken = Read.optionalString(card.path("payment_provider_token"));
        this.holderName = Read.string(card.path("holder_name"));
        this.scheme = CardScheme.from(Read.optionalString(card.path("scheme")));
        this.firstDigits = Read.string(card.path("first_digits"));
        this.lastFourDigit = Read.string(card.path("last_four_digit"));
        this.expiryMonth = Read.string(card.path("expiry_month"));
        this.expiryYear = Read.string(card.path("expiry_year"));
        this.isDefault = Read.bool(card.path("is_default"));
        this.createdAt = Read.optionalString(card.path("created_at"));
    }

    public static SavedCard fromBody(JsonNode card) {
        return new SavedCard(card);
    }

    /**
     * The card an answer carries under this key, or null when it carries none.
     */
    static SavedCard in(JsonNode card) {
        return card.isObject() ? new SavedCard(card) : null;
    }

    /** The card's token in the gateway, which names it again later. */
    public String getToken() {
        return token;
    }

    /** The account the card is kept at; it can only be charged there. */
    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    public String getHolderName() {
        return holderName;
    }

    /** The network the card belongs to, as far as it is known; null otherwise. */
    public CardScheme getScheme() {
        return scheme;
    }

    /** The head of the number: eight digits, or six for a number shorter than sixteen digits. */
    public String getFirstDigits() {
        return firstDigits;
    }

    public String getLastFourDigit() {
        return lastFourDigit;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    /** Whether this is the card the customer pays with unless they say otherwise. */
    public boolean isDefault() {
        return isDefault;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "SavedCard[token=" + token + ", scheme=" + scheme + ", isDefault=" + isDefault + "]";
    }
}
