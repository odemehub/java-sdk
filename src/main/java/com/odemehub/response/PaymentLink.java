package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.AmountType;
import com.odemehub.enums.Currency;
import com.odemehub.enums.CurrencyType;
import com.odemehub.enums.TaxMode;
import java.util.List;

/**
 * A payment link as it stands: what it sells — its lines and what they come
 * to, or what the payer may pick and the tax on it — in which money, whether
 * it takes payments and until when, and the address it is paid at.
 */
public final class PaymentLink {

    private final String token;
    private final String reference;
    private final String description;
    private final String paymentProviderToken;
    private final AmountType amountType;
    private final String itemName;
    private final List<String> predefinedAmounts;
    private final String taxRate;
    private final TaxMode taxMode;
    private final List<Item> items;
    private final String subtotal;
    private final String taxAmount;
    private final String amount;
    private final Currency currency;
    private final CurrencyType currencyType;
    private final List<Currency> currencies;
    private final boolean emailsPayer;
    private final boolean isActive;
    private final boolean isTest;
    private final String expiresAt;
    private final String checkoutUrl;
    private final String createdAt;
    private final List<Transaction> transactions;
    private final Integer transactionsCount;

    private PaymentLink(JsonNode link) {
        this.token = Read.string(link.path("token"));
        this.reference = Read.string(link.path("reference"));
        this.description = Read.nonEmptyString(link.path("description"));
        this.paymentProviderToken = Read.nonEmptyString(link.path("payment_provider_token"));
        this.amountType = AmountType.from(Read.optionalString(link.path("amount_type")));
        this.itemName = Read.nonEmptyString(link.path("item_name"));
        this.predefinedAmounts = Read.optionalList(link.path("predefined_amounts"), Read::string);
        this.taxRate = Read.nonEmptyString(link.path("tax_rate"));
        this.taxMode = TaxMode.from(Read.optionalString(link.path("tax_mode")));
        this.items = Read.list(link.path("items"), Item::fromBody);
        this.subtotal = Read.optionalString(link.path("subtotal"));
        this.taxAmount = Read.optionalString(link.path("tax_amount"));
        this.amount = Read.optionalString(link.path("amount"));
        this.currency = Currency.from(Read.optionalString(link.path("currency")));
        this.currencyType = CurrencyType.from(Read.optionalString(link.path("currency_type")));
        this.currencies = Read.optionalList(link.path("currencies"), currency -> Currency.from(Read.optionalString(currency)));
        this.emailsPayer = Read.bool(link.path("emails_payer"));
        this.isActive = Read.bool(link.path("is_active"));
        this.isTest = Read.bool(link.path("is_test"));
        this.expiresAt = Read.nonEmptyString(link.path("expires_at"));
        this.checkoutUrl = Read.nonEmptyString(link.path("checkout_url"));
        this.createdAt = Read.nonEmptyString(link.path("created_at"));
        this.transactions = Read.list(link.path("transactions"), Transaction::fromBody);
        this.transactionsCount = Read.optionalInteger(link.path("transactions_count"));
    }

    public static PaymentLink fromBody(JsonNode link) {
        return new PaymentLink(link);
    }

    /** The link's token in the gateway; name it to ask after it or change it. */
    public String getToken() {
        return token;
    }


    /** The reference the link is known by. */
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

    /** What the payer pays: the lines, or an amount they pick; null for a type this version does not know. */
    public AmountType getAmountType() {
        return amountType;
    }

    /** The name of the one line the payer pays, on a link whose amount the payer picks; null otherwise. */
    public String getItemName() {
        return itemName;
    }

    /** The amounts the payer picks from, on a {@code predefined} or {@code predefined_and_custom} link; null otherwise. */
    public List<String> getPredefinedAmounts() {
        return predefinedAmounts;
    }

    /** The tax on what the payer pays, as a percentage, on a link whose amount the payer picks; null when it carries none. */
    public String getTaxRate() {
        return taxRate;
    }

    /** Whether the tax rate is inside what the payer pays or added on top of it. */
    public TaxMode getTaxMode() {
        return taxMode;
    }

    /** What the link is for; empty on a link whose amount the payer picks. */
    public List<Item> getItems() {
        return items;
    }

    /** What the lines come to before tax; null on a link whose amount the payer picks. */
    public String getSubtotal() {
        return subtotal;
    }

    /** The tax the lines carry; null on a link whose amount the payer picks. */
    public String getTaxAmount() {
        return taxAmount;
    }

    /** What one payment on the link comes to; null on a link whose amount the payer picks. */
    public String getAmount() {
        return amount;
    }

    /** The money the link is paid in; on a {@code selectable} link, the one picked to begin with. */
    public Currency getCurrency() {
        return currency;
    }

    /** Whether the payer may pick the money; null for a type this version does not know. */
    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    /** The money the payer may pick, the currency among them, on a {@code selectable} link; null on a {@code fixed} one. */
    public List<Currency> getCurrencies() {
        return currencies;
    }

    /** Whether the payer is sent an e-mail once their payment goes through. */
    public boolean emailsPayer() {
        return emailsPayer;
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

    /** The latest attempts made on the link, at most fifty, newest first, the refused ones included; listed links only. */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /** How many attempts have been made on the link in all, however many are listed; null but on a listed link. */
    public Integer getTransactionsCount() {
        return transactionsCount;
    }

    /** The listed attempts that went through. */
    public List<Transaction> successful() {
        return transactions.stream().filter(Transaction::isSuccessful).toList();
    }

    @Override
    public String toString() {
        return "PaymentLink[token=" + token + ", reference=" + reference + ", amountType=" + amountType + ", amount=" + amount + ", currency=" + currency + ", isActive=" + isActive + ", expiresAt=" + expiresAt + ", checkoutUrl=" + checkoutUrl + "]";
    }
}
