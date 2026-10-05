package com.odemehub.request;

import com.odemehub.enums.Currency;
import java.util.List;
import java.util.Map;

/**
 * A payment link: a page on the gateway paid again and again, by anybody
 * with the address, until it is switched off or its last day runs out. It
 * names no customer; whoever pays says who they are on the page. The answer
 * carries the checkout address, which is the link itself.
 *
 * <p>Opening is idempotent per reference: opening again under a
 * reference that already has a link overwrites that link with what is sent
 * and answers with it, under its own token. Only a link with a payment under
 * way is left alone. A link opened without a reference is given one.

 */
public final class CreatePaymentLink extends Message {

    private final List<Item> items;
    private final Currency currency;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final String expiresAt;
    private final Boolean isActive;

    private CreatePaymentLink(Builder builder) {
        this.items = List.copyOf(Fields.required(builder.items, "items"));
        this.currency = Fields.required(builder.currency, "currency");
        this.reference = builder.reference;
        this.description = builder.description;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.expiresAt = builder.expiresAt;
        this.isActive = builder.isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-payment-link";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "payment_link", Fields.said(
                "reference", reference,
                "description", description,
                "payment_provider_token", paymentProviderToken,
                "currency", currency.getValue(),
                "expires_at", expiresAt,
                "is_active", isActive,
                "items", Fields.each(items, Item::toBody)
            )
        );
    }

    public static final class Builder {

        private List<Item> items;
        private Currency currency;
        private String reference;
        private String description;
        private String paymentProviderToken;
        private String expiresAt;
        private Boolean isActive;

        private Builder() {
        }

        /** What the link is for: 1 to 100 lines. */
        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        /** The reference the link is known by in the calling system. It has to carry at least one digit. Left out, the gateway makes one up. */
        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /** The account the link is paid through; it has to take 3D payments. Left out, the Gate rules and the default account decide at pay time. */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        /** The last day the link may be paid, {@code YYYY-MM-DD} in the team's timezone; today or later. Left out, it never runs out. */
        public Builder expiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        /** Whether the link takes payments. Left out, it does. */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CreatePaymentLink build() {
            return new CreatePaymentLink(this);
        }
    }
}
