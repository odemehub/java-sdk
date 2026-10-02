package com.odemehub.request;

import java.util.Map;

/**
 * An order opened to be paid once on the gateway's own checkout page. The
 * answer carries the checkout address; the customer is sent there, pays, and
 * is posted back to the success address. The addresses set for the order's
 * channel under Webhook in the panel hear that it was paid whether or not
 * the customer comes back.
 */
public final class CreateOrder extends CheckoutMessage {

    private CreateOrder(Builder builder) {
        super(builder);
        Fields.required(builder.channelReference, "channelReference");
        Fields.required(builder.successUrl, "successUrl");
        Fields.required(builder.items, "items");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-order";
    }

    @Override
    protected String group() {
        return "order";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return body(details(channel(channelToken)));
    }

    public static final class Builder extends CheckoutMessage.Builder<Builder> {

        private Builder() {
        }

        @Override
        protected Builder self() {
            return this;
        }

        public CreateOrder build() {
            return new CreateOrder(this);
        }
    }
}
