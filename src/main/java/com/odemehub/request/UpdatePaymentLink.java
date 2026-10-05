package com.odemehub.request;

import com.odemehub.enums.Currency;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A change to a payment link, named by its token in the address and again in
 * the body. Only what is sent is written: a field left out keeps what there
 * was, and lines sent replace every line there was. Switching a link off is
 * {@code isActive(false)}; switching one whose last day has gone by back on
 * needs a new {@code expiresAt} with it. A link with a payment under way
 * cannot be changed; the gateway says so on {@code token}.

 */
public final class UpdatePaymentLink extends Message {

    private final String token;
    private final List<Item> items;
    private final Currency currency;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final String expiresAt;
    private final Boolean isActive;
    private final List<String> clear;

    private UpdatePaymentLink(Builder builder) {
        this.token = Fields.required(builder.token, "token");
        this.items = builder.items == null ? null : List.copyOf(builder.items);
        this.currency = builder.currency;
        this.reference = builder.reference;
        this.description = builder.description;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.expiresAt = builder.expiresAt;
        this.isActive = builder.isActive;
        this.clear = List.copyOf(builder.clear);
    }

    /**
     * @param token The link's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-payment-link/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> link = Fields.said(
            "reference", reference,
            "description", description,
            "payment_provider_token", paymentProviderToken,
            "currency", currency == null ? null : currency.getValue(),
            "expires_at", expiresAt,
            "is_active", isActive,
            "items", Fields.each(items, Item::toBody)
        );


        return Fields.of(
            "token", token,
            "payment_link", Fields.cleared(link, clear)
        );
    }

    public static final class Builder {

        private final String token;
        private List<Item> items;
        private Currency currency;
        private String reference;
        private String description;
        private String paymentProviderToken;
        private String expiresAt;
        private Boolean isActive;
        private final List<String> clear = new ArrayList<>();

        private Builder(String token) {
            this.token = token;
        }

        /** What the link is for: 1 to 100 lines, replacing every line there was. */
        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        /** As {@code YYYY-MM-DD} in the team's timezone; today or later. */
        public Builder expiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Fields set to nothing, by their wire names: {@code expires_at} (never
         * runs out), {@code description}, {@code payment_provider_token}.
         */
        public Builder clear(String... fields) {
            this.clear.addAll(Arrays.asList(fields));
            return this;
        }

        public UpdatePaymentLink build() {
            return new UpdatePaymentLink(this);
        }
    }
}
