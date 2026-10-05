package com.odemehub.request;

import com.odemehub.enums.Currency;
import java.util.Map;

/**
 * A payment handed to the gateway. What is common to every kind of payment
 * lives here; the endpoint it is sent to is what tells the kinds apart.
 *
 * <p>A payment is made with a card the customer typed in or with one they
 * let the merchant keep, never with both. With a kept card the payment goes
 * through the account the card is kept at, so no account is named either;
 * the gateway turns down a payment that names both.
 */
public abstract class Payment extends Message {

    private final String reference;
    private final String amount;
    private final int installmentNumber;
    private final String ip;
    private final Customer customer;
    private final Card card;
    private final String savedCardToken;
    private final Currency currency;
    private final String paymentProviderToken;
    private final String baseAmount;

    protected Payment(Builder<?> builder) {
        this.reference = Fields.required(builder.reference, "reference");
        this.amount = Fields.required(builder.amount, "amount");
        this.installmentNumber = Fields.required(builder.installmentNumber, "installmentNumber");
        this.ip = Fields.required(builder.ip, "ip");
        this.customer = Fields.required(builder.customer, "customer");
        this.card = builder.card;
        this.savedCardToken = builder.savedCardToken;
        this.currency = builder.currency;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.baseAmount = builder.baseAmount;
    }

    /**
     * The request body. The signature is not part of it; the client signs the
     * body as a whole and sends the signature in a header of its own.
     */
    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> body = Fields.of(
            "transaction", Fields.said(
                "reference", reference,
                "payment_provider_token", paymentProviderToken,
                "amount", amount,
                "base_amount", baseAmount,
                "currency", currency == null ? null : currency.getValue(),
                "installment_number", installmentNumber,
                "ip", ip,
                "saved_card_token", savedCardToken
            ),
            "customer", customer.toBody()
        );

        if (card != null) {
            body.put("card", card.toBody());
        }

        return body;
    }

    public abstract static class Builder<B extends Builder<B>> {
        private String reference;
        private String amount;
        private Integer installmentNumber;
        private String ip;
        private Customer customer;
        private Card card;
        private String savedCardToken;
        private Currency currency;
        private String paymentProviderToken;
        private String baseAmount;

        protected abstract B self();

        /**
         * The reference the payment is known by in the calling system, such as
         * SIP-10231. It has to carry at least one digit: its digits end the
         * order number the bank is sent, so the payment can be found in the
         * bank's panel by it.
         */
        public B reference(String reference) {
            this.reference = reference;
            return self();
        }

        /**
         * The amount, as digits with the kurus behind a point: "100", "100.1"
         * or "100.10". A comma is refused. It is a string so that it is signed
         * and sent exactly as it is written here, with no rounding on the way.
         */
        public B amount(String amount) {
            this.amount = amount;
            return self();
        }

        /** 1 to 12. More than one only when the payment is asked for and charged in lira. */
        public B installmentNumber(int installmentNumber) {
            this.installmentNumber = installmentNumber;
            return self();
        }

        /** The address the customer is paying from, as the merchant sees it. */
        public B ip(String ip) {
            this.ip = ip;
            return self();
        }

        /**
         * Who is paying: the reference, if there is one, and the whole billing
         * address. A payment that keeps its card, or pays with a kept one,
         * needs the reference.
         */
        public B customer(Customer customer) {
            this.customer = customer;
            return self();
        }

        /** The card typed in. Left out only when a kept card is named instead. */
        public B card(Card card) {
            this.card = card;
            return self();
        }

        /** A card the customer let the merchant keep, by the token the gateway gave it. */
        public B savedCardToken(String savedCardToken) {
            this.savedCardToken = savedCardToken;
            return self();
        }

        /** Left out, the gateway takes the lira. */
        public B currency(Currency currency) {
            this.currency = currency;
            return self();
        }

        /**
         * The payment account to charge through. Left out, the team's routing
         * rules pick the account, and the team's default account is used when
         * none of them holds. Never named together with a kept card: a
         * payment with a kept card always goes through the account the card
         * is kept at.
         */
        public B paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return self();
        }

        /**
         * What is being sold, where the customer spreads the amount over
         * months and the bank takes something for the waiting on top of it.
         * Never more than the amount. Left out where the two are the same,
         * which is most payments.
         */
        public B baseAmount(String baseAmount) {
            this.baseAmount = baseAmount;
            return self();
        }
    }
}
