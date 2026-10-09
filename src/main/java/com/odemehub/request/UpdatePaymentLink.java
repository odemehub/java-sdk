package com.odemehub.request;

import com.odemehub.enums.AmountType;
import com.odemehub.enums.Currency;
import com.odemehub.enums.CurrencyType;
import com.odemehub.enums.TaxMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A change to a payment link, named by its token in the address and again in
 * the body. Only what is sent is written: a field left out keeps what there
 * was, and lines sent replace every line there was. A link that is
 * {@code fixed} once the change is written needs lines: one turned back to
 * {@code fixed} from an amount the payer picks has to be sent them, or the
 * gateway says so on {@code payment_link.items}. Switching a link off is
 * {@code isActive(false)}; one whose last day has gone by is switched back on
 * by giving it a new {@code expiresAt}, or none. A payment under way does not stand in
 * the way: the payments to come are charged as the link is changed.
 */
public final class UpdatePaymentLink extends Message {

    private final String token;
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
    private final Boolean emailsCustomer;
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
        this.amountType = builder.amountType;
        this.itemName = builder.itemName;
        this.predefinedAmounts = builder.predefinedAmounts == null ? null : List.copyOf(builder.predefinedAmounts);
        this.taxRate = builder.taxRate;
        this.taxMode = builder.taxMode;
        this.currencyType = builder.currencyType;
        this.currencies = builder.currencies == null ? null : List.copyOf(builder.currencies);
        this.emailsCustomer = builder.emailsCustomer;
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
            "amount_type", amountType == null ? null : amountType.getValue(),
            "item_name", itemName,
            "predefined_amounts", predefinedAmounts,
            "tax_rate", taxRate,
            "tax_mode", taxMode == null ? null : taxMode.getValue(),
            "currency", currency == null ? null : currency.getValue(),
            "currency_type", currencyType == null ? null : currencyType.getValue(),
            "currencies", Fields.each(currencies, Currency::getValue),
            "emails_customer", emailsCustomer,
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
        private AmountType amountType;
        private String itemName;
        private List<String> predefinedAmounts;
        private String taxRate;
        private TaxMode taxMode;
        private CurrencyType currencyType;
        private List<Currency> currencies;
        private Boolean emailsCustomer;
        private String expiresAt;
        private Boolean isActive;
        private final List<String> clear = new ArrayList<>();

        private Builder(String token) {
            this.token = token;
        }

        /** What the link is for: 1 to 100 lines, replacing every line there was. Passed over on a link whose amount the payer picks. */
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

        /** What the payer pays: the lines, or an amount the payer picks. */
        public Builder amountType(AmountType amountType) {
            this.amountType = amountType;
            return this;
        }

        /** The name of the one line the payer pays, on a link whose amount the payer picks. */
        public Builder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        /** The amounts the payer picks from, at most ten, as digits with the kurus behind a point: "100.00". */
        public Builder predefinedAmounts(List<String> predefinedAmounts) {
            this.predefinedAmounts = predefinedAmounts;
            return this;
        }

        /** The tax on what the payer pays, as a percentage, on a link whose amount the payer picks. */
        public Builder taxRate(String taxRate) {
            this.taxRate = taxRate;
            return this;
        }

        /** Whether the tax rate is inside what the payer pays or added on top of it. */
        public Builder taxMode(TaxMode taxMode) {
            this.taxMode = taxMode;
            return this;
        }

        /** Whether the payer may pick the money. */
        public Builder currencyType(CurrencyType currencyType) {
            this.currencyType = currencyType;
            return this;
        }

        /** The money the payer may pick besides the currency, on a {@code selectable} link. */
        public Builder currencies(List<Currency> currencies) {
            this.currencies = currencies;
            return this;
        }

        /** Whether the payer is sent an e-mail, at the address they give on the checkout page, once their payment goes through. */
        public Builder emailsCustomer(boolean emailsCustomer) {
            this.emailsCustomer = emailsCustomer;
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
         * runs out), {@code description}, {@code payment_provider_token},
         * {@code item_name}, {@code predefined_amounts}, {@code tax_rate},
         * {@code currencies}.
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
