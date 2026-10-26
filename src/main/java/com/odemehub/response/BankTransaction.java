package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;

/**
 * A movement the bank booked on one of the team's accounts, as the gateway
 * last read it.
 */
public final class BankTransaction {

    private final String token;
    private final String bankAccountToken;
    private final String bookedAt;
    private final String valueDate;
    private final String amount;
    private final Currency currency;
    private final String balanceAfter;
    private final String description;
    private final String senderDescription;
    private final String counterpartyIban;
    private final String counterpartyTaxNumber;

    private BankTransaction(JsonNode bankTransaction) {
        this.token = Read.string(bankTransaction.path("token"));
        this.bankAccountToken = Read.string(bankTransaction.path("bank_account_token"));
        this.bookedAt = Read.string(bankTransaction.path("booked_at"));
        this.valueDate = Read.optionalString(bankTransaction.path("value_date"));
        this.amount = Read.string(bankTransaction.path("amount"));
        this.currency = Currency.from(Read.optionalString(bankTransaction.path("currency")));
        this.balanceAfter = Read.optionalString(bankTransaction.path("balance_after"));
        this.description = Read.optionalString(bankTransaction.path("description"));
        this.senderDescription = Read.optionalString(bankTransaction.path("sender_description"));
        this.counterpartyIban = Read.optionalString(bankTransaction.path("counterparty_iban"));
        this.counterpartyTaxNumber = Read.optionalString(bankTransaction.path("counterparty_tax_number"));
    }

    public static BankTransaction fromBody(JsonNode bankTransaction) {
        return new BankTransaction(bankTransaction);
    }

    /** The movement's token in the gateway. */
    public String getToken() {
        return token;
    }

    /** The account the movement was booked on. */
    public String getBankAccountToken() {
        return bankAccountToken;
    }

    /** When the bank booked it, ISO-8601 in UTC. */
    public String getBookedAt() {
        return bookedAt;
    }

    /** The day the money counts from, {@code YYYY-MM-DD}; null when the bank does not say. */
    public String getValueDate() {
        return valueDate;
    }

    /** As digits with the kurus behind a point, signed: above zero for money coming in ("22164.96"), below for money going out ("-40.00"). */
    public String getAmount() {
        return amount;
    }

    /** The money the account is kept in. */
    public Currency getCurrency() {
        return currency;
    }

    /** What the account held once the movement was booked, the same way; null when the bank does not say. */
    public String getBalanceAfter() {
        return balanceAfter;
    }

    /** The bank's own description of the movement. */
    public String getDescription() {
        return description;
    }

    /** What the sender wrote on the transfer. */
    public String getSenderDescription() {
        return senderDescription;
    }

    /** The IBAN on the other side, when the bank says it. */
    public String getCounterpartyIban() {
        return counterpartyIban;
    }

    /** The tax number on the other side, when the bank says it. */
    public String getCounterpartyTaxNumber() {
        return counterpartyTaxNumber;
    }

    @Override
    public String toString() {
        return "BankTransaction[token=" + token + ", bankAccountToken=" + bankAccountToken + ", bookedAt=" + bookedAt
            + ", amount=" + amount + ", currency=" + currency + "]";
    }
}
