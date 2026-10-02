package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import com.odemehub.enums.PaymentStatus;
import com.odemehub.enums.SecurityType;
import com.odemehub.enums.TransactionStatus;

/**
 * One attempt at a payment, as the gateway lists it: enough to tell the
 * attempts apart and see where each got to. Where it stands is said twice on
 * purpose: the attempt's own state, and what became of the money, which can
 * move on to refunded long after the attempt is over. A payment made at an
 * order, a payment link or a subscription names it.
 */
public final class Transaction {

    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final TransactionStatus status;
    private final PaymentStatus paymentStatus;
    private final SecurityType securityType;
    private final String amount;
    private final String baseAmount;
    private final Currency currency;
    private final int installmentNumber;
    private final boolean isTest;
    private final String errorCode;
    private final String errorMessage;
    private final String createdAt;
    private final PaymentCustomer customer;
    private final Conversion conversion;
    private final String orderToken;
    private final String paymentLinkToken;
    private final String subscriptionToken;

    private Transaction(JsonNode transaction) {
        this.token = Read.string(transaction.path("token"));
        this.channelToken = Read.string(transaction.path("channel_token"));
        this.channelReference = Read.string(transaction.path("channel_reference"));
        this.status = TransactionStatus.from(Read.optionalString(transaction.path("status")));
        this.paymentStatus = PaymentStatus.from(Read.optionalString(transaction.path("payment_status")));
        this.securityType = SecurityType.from(Read.optionalString(transaction.path("security_type")));
        this.amount = Read.string(transaction.path("amount"));
        this.baseAmount = Read.string(transaction.path("base_amount"));
        this.currency = Currency.from(Read.optionalString(transaction.path("currency")));
        this.installmentNumber = Read.integer(transaction.path("installment_number"));
        this.isTest = Read.bool(transaction.path("is_test"));
        this.errorCode = Read.nonEmptyString(transaction.path("error_code"));
        this.errorMessage = Read.nonEmptyString(transaction.path("error_message"));
        this.createdAt = Read.nonEmptyString(transaction.path("created_at"));
        this.customer = PaymentCustomer.in(transaction.path("customer"));
        this.conversion = Read.object(transaction.path("conversion")) == null ? null : Conversion.fromBody(transaction.path("conversion"));
        this.orderToken = Read.nonEmptyString(transaction.path("order").path("token"));
        this.paymentLinkToken = Read.nonEmptyString(transaction.path("payment_link").path("token"));
        this.subscriptionToken = Read.nonEmptyString(transaction.path("subscription").path("token"));
    }

    public static Transaction fromBody(JsonNode transaction) {
        return new Transaction(transaction);
    }

    /** Whether the attempt went through. */
    public boolean isSuccessful() {
        return status == TransactionStatus.SUCCESSFUL;
    }

    /** Whether the attempt is over: successful, failed or expired. */
    public boolean isFinished() {
        return status != null && status.isFinished();
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

    /** The attempt's state; null for a state this version does not know. */
    public TransactionStatus getStatus() {
        return status;
    }

    /** What became of the money; null for a value this version does not know. */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /** How it was made: secure (confirmed at the bank) or regular. */
    public SecurityType getSecurityType() {
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

    public Currency getCurrency() {
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

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** Who paid, as the payment keeps them. */
    public PaymentCustomer getCustomer() {
        return customer;
    }

    /** What reached the card when it was charged in another money; null when charged as asked. */
    public Conversion getConversion() {
        return conversion;
    }

    /** The token of the order this attempt was at; null when it was at none. */
    public String getOrderToken() {
        return orderToken;
    }

    /** The token of the payment link this attempt was at; null when it was at none. */
    public String getPaymentLinkToken() {
        return paymentLinkToken;
    }

    /** The token of the subscription this attempt paid a renewal of; null when it paid none. */
    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    @Override
    public String toString() {
        return "Transaction[token=" + token + ", channelReference=" + channelReference + ", status=" + status + ", paymentStatus=" + paymentStatus + ", amount=" + amount + ", currency=" + currency + ", errorMessage=" + errorMessage + "]";
    }
}
