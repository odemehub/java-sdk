package com.odemehub.request;

/**
 * A payment charged straight to the card, without sending the customer to
 * their bank to confirm it. A successful answer is a settled payment.
 */
public final class RegularPayment extends Payment {

    private RegularPayment(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "regular-payment";
    }

    public static final class Builder extends Payment.Builder<Builder> {

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public RegularPayment build() {
            return new RegularPayment(this);
        }
    }
}
