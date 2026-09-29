package com.odemehub.request;

import java.util.Map;

/**
 * A payment the customer confirms with their bank. The gateway does not
 * settle it; it hands back the address the customer has to be sent to, and
 * posts them back to the callback address once they are done.
 */
public final class SecurePayment extends Payment {

    private final String callbackUrl;

    private SecurePayment(Builder builder) {
        super(builder);
        this.callbackUrl = Fields.required(builder.callbackUrl, "callbackUrl");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "secure-payment";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> toBody(String channelToken) {
        Map<String, Object> body = super.toBody(channelToken);
        ((Map<String, Object>) body.get("transaction")).put("callback_url", callbackUrl);

        return body;
    }

    public static final class Builder extends Payment.Builder<Builder> {

        private String callbackUrl;

        private Builder() {
        }

        /** Where the customer is posted back to, with the signed outcome, once they are done at their bank. */
        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public SecurePayment build() {
            return new SecurePayment(this);
        }
    }
}
