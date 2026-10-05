package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A change to an open order, named by its token in the address and again in
 * the body. Only what is sent is written: a field left out keeps what there
 * was, lines sent replace every line there was, and customer fields sent are
 * merged over the ones the order had; a reference sent takes the place of the
 * one there was. A paid order, or one with a payment under way, cannot be
 * changed; the gateway says so on {@code token}.
 */
public final class UpdateOrder extends CheckoutMessage {

    private final String token;

    private UpdateOrder(Builder builder) {
        super(builder);
        this.token = Fields.required(builder.token, "token");
    }

    /**
     * @param token The order's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-order/" + token;
    }

    @Override
    protected String group() {
        return "order";
    }

    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", token);
        body.putAll(body(cleared(details())));

        return body;
    }

    public static final class Builder extends CheckoutMessage.Builder<Builder> {

        private final String token;

        private Builder(String token) {
            this.token = token;
        }

        /**
         * Fields set to nothing, by their wire names: {@code description},
         * {@code cancel_url}, {@code payment_provider_token}.
         */
        public Builder clear(String... fields) {
            return clearing(fields);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public UpdateOrder build() {
            return new UpdateOrder(this);
        }
    }
}
