package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * One attempt at a payment, as the gateway lists it: enough to tell the
 * attempts apart and see where each got to. Where it stands is said twice on
 * purpose — the attempt's own state, and what became of the money, which can
 * move on to refunded long after the attempt is over.
 */
public final class Transaction {

    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final String status;
    private final String paymentStatus;
    private final String securityType;
    private final String amount;
    private final String baseAmount;
    private final String currency;
    private final int installmentNumber;
    private final boolean isTest;
    private final String errorCode;
    private final String errorMessage;
    private final String createdAt;
    private final String customerChannelReference;
    private final Conversion conversion;
    private final String orderToken;
    private final String subscriptionToken;

    private Transaction(JsonNode transaction) {
        JsonNode conversion = transaction.path("conversion");
        int installmentNumber = Read.integer(transaction.path("installment_number"));

        this.token = Read.string(transaction.path("token"));
        this.channelToken = Read.string(transaction.path("channel_token"));
        this.channelReference = Read.string(transaction.path("channel_reference"));
        this.status = Read.string(transaction.path("status"));
        this.paymentStatus = Read.string(transaction.path("payment_status"));
        this.securityType = Read.string(transaction.path("security_type"));
        this.amount = Read.string(transaction.path("amount"));
        this.baseAmount = Read.string(transaction.path("base_amount"));
        this.currency = Read.string(transaction.path("currency"));
        this.installmentNumber = installmentNumber == 0 ? 1 : installmentNumber;
        this.isTest = Read.bool(transaction.path("is_test"));
        this.errorCode = Read.nonEmptyString(transaction.path("error_code"));
        this.errorMessage = Read.nonEmptyString(transaction.path("error_message"));
        this.createdAt = Read.nonEmptyString(transaction.path("created_at"));
        this.customerChannelReference = Read.nonEmptyString(transaction.path("customer").path("channel_reference"));
        this.conversion = conversion.isObject() ? Conversion.fromBody(conversion) : null;
        this.orderToken = Read.nonEmptyString(transaction.path("order").path("token"));
        this.subscriptionToken = Read.nonEmptyString(transaction.path("subscription").path("token"));
    }

    public static Transaction fromBody(JsonNode transaction) {
        return new Transaction(transaction);
    }

    /** Whether the attempt went through. */
    public boolean isSuccessful() {
        return status.equals("successful");
    }

    /** The payment's token in the gateway, which names it again to ask after or give back. */
    public String getToken() {
        return token;
    }

    /** The channel the payment came in on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The reference the payment was made under in the calling system. */
    public String getChannelReference() {
        return channelReference;
    }

    /** The attempt's state: started, redirected_to_secure_page, returned_from_secure_page, failed, expired or successful. */
    public String getStatus() {
        return status;
    }

    /** What became of the money: unpaid, paid, cancelled, refunded or partially_refunded. */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /** How it was made: secure (confirmed at the bank) or regular. */
    public String getSecurityType() {
        return securityType;
    }

    /** What the card was charged, with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    /** What was being sold, before anything added for instalments. */
    public String getBaseAmount() {
        return baseAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    /** Whether it was made in the test environment. */
    public boolean isTest() {
        return isTest;
    }

    /** What the provider called the refusal, for an attempt that failed; null otherwise. */
    public String getErrorCode() {
        return errorCode;
    }

    /** Why it failed, written for a person; null otherwise. */
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    /** The merchant's own key for the customer; null for a payer the merchant never named. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    /** What reached the card when it was charged in another money; null when charged as asked. */
    public Conversion getConversion() {
        return conversion;
    }

    /** The token of the order this attempt was at; null when it was at none. */
    public String getOrderToken() {
        return orderToken;
    }

    /** The token of the subscription this attempt paid a period of; null when it paid none. */
    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    @Override
    public String toString() {
        return "Transaction[token=" + token + ", channelReference=" + channelReference + ", status=" + status
            + ", paymentStatus=" + paymentStatus + ", amount=" + amount + ", currency=" + currency
            + ", errorMessage=" + errorMessage + "]";
    }
}
