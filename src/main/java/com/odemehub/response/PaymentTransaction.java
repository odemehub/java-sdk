package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;
import com.odemehub.enums.PaymentStatus;
import com.odemehub.enums.SecurityType;
import com.odemehub.enums.TransactionStatus;

/**
 * The payment an answer is about. The gateway's own answers ({@code
 * securePayment}, {@code regularPayment}, {@code refundPayment},
 * {@code cancelPayment}, {@code retrievePayment} and by reference) say it in
 * full. A payment made at an order, a payment link or a subscription names
 * it, so a webhook about one of them can be checked against the payment it
 * names.
 */
public final class PaymentTransaction {

    private final String token;
    private final String channelToken;
    private final String channelReference;
    private final TransactionStatus status;
    private final PaymentStatus paymentStatus;
    private final SecurityType securityType;
    private final String amount;
    private final String baseAmount;
    private final Currency currency;
    private final Integer installmentNumber;
    private final Boolean isTest;
    private final String createdAt;
    private final String orderToken;
    private final String paymentLinkToken;
    private final String subscriptionToken;

    private PaymentTransaction(JsonNode transaction) {
        this.token = Read.string(transaction.path("token"));
        this.channelToken = Read.string(transaction.path("channel_token"));
        this.channelReference = Read.string(transaction.path("channel_reference"));
        this.status = TransactionStatus.from(Read.optionalString(transaction.path("status")));
        this.paymentStatus = PaymentStatus.from(Read.optionalString(transaction.path("payment_status")));
        this.securityType = SecurityType.from(Read.optionalString(transaction.path("security_type")));
        this.amount = Read.nonEmptyString(transaction.path("amount"));
        this.baseAmount = Read.nonEmptyString(transaction.path("base_amount"));
        this.currency = Currency.from(Read.optionalString(transaction.path("currency")));
        this.installmentNumber = Read.optionalInteger(transaction.path("installment_number"));
        this.isTest = Read.optionalBool(transaction.path("is_test"));
        this.createdAt = Read.nonEmptyString(transaction.path("created_at"));
        this.orderToken = Read.optionalString(transaction.path("order").path("token"));
        this.paymentLinkToken = Read.optionalString(transaction.path("payment_link").path("token"));
        this.subscriptionToken = Read.optionalString(transaction.path("subscription").path("token"));
    }

    static PaymentTransaction in(JsonNode transaction) {
        return new PaymentTransaction(transaction);
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

    /** The reference the payment is known by in the calling system. */
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

    /** Secure (confirmed at the bank) or regular. */
    public SecurityType getSecurityType() {
        return securityType;
    }

    /** What the card was charged, as asked for. */
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

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    /** Whether it was made in the test environment. */
    public Boolean isTest() {
        return isTest;
    }

    /** ISO 8601, UTC. */
    public String getCreatedAt() {
        return createdAt;
    }

    /** The order the payment was made at; null when it was made at none. */
    public String getOrderToken() {
        return orderToken;
    }

    /** The payment link the payment was made on; null when it was made on none. */
    public String getPaymentLinkToken() {
        return paymentLinkToken;
    }

    /** The subscription whose renewal the payment paid; null when it paid none. */
    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    @Override
    public String toString() {
        return "PaymentTransaction[token=" + token + ", channelReference=" + channelReference + ", status=" + status + ", paymentStatus=" + paymentStatus + ", amount=" + amount + ", currency=" + currency + "]";
    }
}
