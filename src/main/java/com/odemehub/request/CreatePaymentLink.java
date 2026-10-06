package com.odemehub.request;

import com.odemehub.enums.AmountType;
import com.odemehub.enums.Currency;
import com.odemehub.enums.CurrencyType;
import com.odemehub.enums.TaxMode;
import java.util.List;
import java.util.Map;

/**
 * A payment link: a page on the gateway paid again and again, by anybody
 * with the address, until it is switched off or its last day runs out. It
 * names no customer; whoever pays says who they are on the page. The answer
 * carries the checkout address, which is the link itself.
 *
 * <p>What the payer pays is the link's amount type: the lines the merchant
 * writes ({@code fixed}, the default), any amount the payer writes
 * ({@code custom}), one of the amounts offered ({@code predefined}), or one
 * of those or an amount of their own ({@code predefined_and_custom}). A link
 * whose amount the payer picks is paid as one line under its item name, with
 * its tax rate, and needs no lines; any sent are passed over. The payer may
 * also pick the money, where the currency type is {@code selectable}.
 *
 * <p>Every opening is a new link under a new token, even under a reference
 * sent before; nothing already there is written over. Keep the token the
 * answer comes back with. A link opened without a reference is given one.
 */
public final class CreatePaymentLink extends Message {

    private final List<Item> items;
    private final Currency currency;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final AmountType amountType;
    private final String itemName;
    private final List<String> predefinedAmounts;
    private final String taxRate;
    private final TaxMode taxMode;
    private final CurrencyType currencyType;
    private final List<Currency> currencies;
    private final Boolean emailsPayer;
    private final String expiresAt;
    private final Boolean isActive;

    private CreatePaymentLink(Builder builder) {
        this.items = builder.items == null ? null : List.copyOf(builder.items);
        this.currency = Fields.required(builder.currency, "currency");
        this.reference = builder.reference;
        this.description = builder.description;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.amountType = builder.amountType;
        this.itemName = builder.itemName;
        this.predefinedAmounts = builder.predefinedAmounts == null ? null : List.copyOf(builder.predefinedAmounts);
        this.taxRate = builder.taxRate;
        this.taxMode = builder.taxMode;
        this.currencyType = builder.currencyType;
        this.currencies = builder.currencies == null ? null : List.copyOf(builder.currencies);
        this.emailsPayer = builder.emailsPayer;
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
                "amount_type", amountType == null ? null : amountType.getValue(),
                "item_name", itemName,
                "predefined_amounts", predefinedAmounts,
                "tax_rate", taxRate,
                "tax_mode", taxMode == null ? null : taxMode.getValue(),
                "currency", currency.getValue(),
                "currency_type", currencyType == null ? null : currencyType.getValue(),
                "currencies", Fields.each(currencies, Currency::getValue),
                "emails_payer", emailsPayer,
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
        private AmountType amountType;
        private String itemName;
        private List<String> predefinedAmounts;
        private String taxRate;
        private TaxMode taxMode;
        private CurrencyType currencyType;
        private List<Currency> currencies;
        private Boolean emailsPayer;
        private String expiresAt;
        private Boolean isActive;

        private Builder() {
        }

        /** What the link is for: 1 to 100 lines. Needed on a {@code fixed} link only; passed over on one whose amount the payer picks. */
        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        /** The money the link is paid in; on a {@code selectable} link, the one picked to begin with. */
        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        /** The reference the link is known by in the calling system. It has to carry at least one digit, and may be sent again. Left out, the gateway makes one up. */
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

        /** What the payer pays. Left out, the link is {@code fixed}: its lines. */
        public Builder amountType(AmountType amountType) {
            this.amountType = amountType;
            return this;
        }

        /** The name of the one line the payer pays, on a link whose amount the payer picks. */
        public Builder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        /** The amounts the payer picks from, at most ten, as digits with the kurus behind a point: "100.00". On a {@code predefined} or {@code predefined_and_custom} link. */
        public Builder predefinedAmounts(List<String> predefinedAmounts) {
            this.predefinedAmounts = predefinedAmounts;
            return this;
        }

        /** The tax on what the payer pays, as a percentage: "20" or "20.00", on a link whose amount the payer picks. Left out, it carries no tax. */
        public Builder taxRate(String taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        /** Whether the tax rate is inside what the payer pays or added on top of it. Left out, it is inside. */
        public Builder taxMode(TaxMode taxMode) {
            this.taxMode = taxMode;
            return this;
        }

        /** Whether the payer may pick the money. Left out, the link is {@code fixed}: paid in its currency. */
        public Builder currencyType(CurrencyType currencyType) {
            this.currencyType = currencyType;
            return this;
        }

        /** The money the payer may pick besides the currency, on a {@code selectable} link. */
        public Builder currencies(List<Currency> currencies) {
            this.currencies = currencies;
            return this;
        }

        /** Whether the payer is sent an e-mail once their payment goes through. Left out, they are not. */
        public Builder emailsPayer(boolean emailsPayer) {
            this.emailsPayer = emailsPayer;
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
