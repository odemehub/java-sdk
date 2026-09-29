package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The outcome of a payment, as the gateway reports it — whether it answers
 * straight away or posts the outcome back once the customer is home from
 * their bank. The two are the same shape, so a merchant reads them the same
 * way: how it went, which payment it was, and whose.
 *
 * <p>A payment that was turned down is an outcome like any other and arrives
 * here; only answers that were never a payment outcome are thrown as
 * exceptions.
 */
public class Payment {

    private final Result result;
    private final String transactionToken;
    private final String channelToken;
    private final String channelReference;
    private final String customerChannelReference;
    private final SavedCard savedCard;
    private final Conversion conversion;

    protected Payment(JsonNode body) {
        JsonNode transaction = body.path("transaction");
        JsonNode conversion = body.path("conversion");

        this.result = Result.fromBody(body);
        this.transactionToken = Read.string(transaction.path("token"));
        this.channelToken = Read.string(transaction.path("channel_token"));
        this.channelReference = Read.string(transaction.path("channel_reference"));
        this.customerChannelReference = Read.string(body.path("customer").path("channel_reference"));
        this.savedCard = SavedCard.in(body.path("saved_card"));
        this.conversion = conversion.isObject() ? Conversion.fromBody(conversion) : null;
    }

    public static Payment fromBody(JsonNode body) {
        return new Payment(body);
    }

    public Result getResult() {
        return result;
    }

    /** The payment's token in the gateway, which names it again for a refund. */
    public String getTransactionToken() {
        return transactionToken;
    }

    /** The channel the payment came in on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The reference the payment is known by in the calling system. */
    public String getChannelReference() {
        return channelReference;
    }

    /** The merchant's own key for the customer the payment was made for. */
    public String getCustomerChannelReference() {
        return customerChannelReference;
    }

    /**
     * The card the payment kept, for a payment that asked for one to be kept.
     * It is null while nothing was kept: because the payment did not go
     * through, because the provider handed nothing back, or because the
     * payment never asked.
     */
    public SavedCard getSavedCard() {
        return savedCard;
    }

    /**
     * What reached the card, for a payment the merchant's conversion rules
     * charged in another money than it was asked in; null for a payment
     * charged as it was asked.
     */
    public Conversion getConversion() {
        return conversion;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[result=" + result + ", transactionToken=" + transactionToken
            + ", channelReference=" + channelReference + ", savedCard=" + savedCard + ", conversion=" + conversion + "]";
    }
}
