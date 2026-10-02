package com.odemehub.request;

import java.util.Map;

/**
 * The card a payment is attempted with, or kept without one. The number and the security code
 * travel no further than the request body, and are kept out of
 * {@link #toString()} so they never reach a log: the gateway keeps only the
 * head and the tail digits of the number and no digit of the code.
 */
public final class Card {

    private final String holderName;
    private final String number;
    private final String securityCode;
    private final String expiryMonth;
    private final String expiryYear;
    private final Boolean shouldSave;

    private Card(Builder builder) {
        this.holderName = Fields.required(builder.holderName, "holderName");
        this.number = Fields.required(builder.number, "number");
        this.securityCode = builder.securityCode;
        this.expiryMonth = Fields.required(builder.expiryMonth, "expiryMonth");
        this.expiryYear = Fields.required(builder.expiryYear, "expiryYear");
        this.shouldSave = builder.shouldSave;
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "holder_name", holderName,
            "number", number,
            "security_code", securityCode,
            "expiry_month", expiryMonth,
            "expiry_year", expiryYear,
            "should_save", shouldSave
        );
    }

    @Override
    public String toString() {
        return "Card[holderName=" + holderName + ", number=*****, securityCode=*****, expiryMonth=" + expiryMonth
            + ", expiryYear=" + expiryYear + ", shouldSave=" + shouldSave + "]";
    }

    public static final class Builder {

        private String holderName;
        private String number;
        private String securityCode;
        private String expiryMonth;
        private String expiryYear;
        private Boolean shouldSave;

        private Builder() {
        }

        public Builder holderName(String holderName) {
            this.holderName = holderName;
            return this;
        }

        /** The number, 12 to 19 digits; spaces between the groups are taken out by the gateway. */
        public Builder number(String number) {
            this.number = number;
            return this;
        }

        /**
         * The security code, three or four digits. A payment with a card typed
         * in always needs it. A card kept without a payment
         * ({@code createSavedCard()}) needs it only at providers that keep a
         * card by charging and giving back a small amount; left out, it is not
         * sent.
         */
        public Builder securityCode(String securityCode) {
            this.securityCode = securityCode;
            return this;
        }

        /** Two digits, e.g. 04. */
        public Builder expiryMonth(String expiryMonth) {
            this.expiryMonth = expiryMonth;
            return this;
        }

        /** Four digits, e.g. 2030. */
        public Builder expiryYear(String expiryYear) {
            this.expiryYear = expiryYear;
            return this;
        }

        /**
         * Whether the customer asked for this card to be kept after a
         * successful payment, so they can pay with it again without typing it
         * out. Needs a customer reference to keep it under, a plan that covers
         * saved cards and an account whose provider keeps cards; the gateway
         * turns the payment down on this field otherwise. Read only for
         * payments; left out, it is not sent.
         */
        public Builder shouldSave(boolean shouldSave) {
            this.shouldSave = shouldSave;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
