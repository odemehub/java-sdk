package com.odemehub.request;

import java.util.Map;

/**
 * A question about a card before anything is charged to it: who issued it,
 * what kind of card it is, and how the amount may be paid off on it. Only
 * the head of the number is sent, never the whole of it, because nothing is
 * charged.
 *
 * <p>The body reads like the start of a payment, so the same account and
 * amount the payment would be made with are named here.
 */
public final class RetrieveBin extends Message {

    private final String bin;
    private final String amount;
    private final String paymentProviderToken;
    private final String currency;

    private RetrieveBin(Builder builder) {
        this.bin = Fields.required(builder.bin, "bin");
        this.amount = Fields.required(builder.amount, "amount");
        this.paymentProviderToken = builder.paymentProviderToken;
        this.currency = builder.currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "retrieve-bin";
    }

    /**
     * The body. What the caller left unsaid is left out altogether rather than
     * sent empty, so the gateway fills it in itself.
     */
    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "transaction", Fields.said(
                "payment_provider_token", paymentProviderToken,
                "amount", amount,
                "currency", currency
            ),
            "card", Fields.of("bin", bin)
        );
    }

    public static final class Builder {

        private String bin;
        private String amount;
        private String paymentProviderToken;
        private String currency;

        private Builder() {
        }

        /**
         * The first six to eight digits of the card. Six is what banks key
         * their tables on; eight is what the gateway keeps of a card it has
         * been paid with, so a stored card's digits can be sent as they are.
         */
        public Builder bin(String bin) {
            this.bin = bin;
            return this;
        }

        /** What the payment would come to, as digits with the kurus behind a point: "1000.00". */
        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        /**
         * The account to ask. Left out, the account the team's routing rules
         * would send the card to is asked — the default one when none of them
         * holds — so the instalments match a payment that names no account
         * either.
         */
        public Builder paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return this;
        }

        /** The money the payment is taken in; the lira unless another is named. */
        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public RetrieveBin build() {
            return new RetrieveBin(this);
        }
    }
}
