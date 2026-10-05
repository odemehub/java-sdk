package com.odemehub.request;

import java.util.Map;

/**
 * An order opened to be paid once on the gateway's own checkout page. The
 * answer carries the checkout address; the customer is sent there, pays, and
 * is posted back to the success address. The addresses the team set under
 * Webhook in the panel hear that it was paid whether or not the customer
 * comes back. The customer may be left out, or sent without a reference: the
 * payer then says who they are on the checkout, and is not kept as one of
 * the team's customers.
 */
public final class CreateOrder extends CheckoutMessage {

    private CreateOrder(Builder builder) {
        super(builder);
        Fields.required(builder.reference, "reference");
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
    public Map<String, Object> toBody() {
        return body(details());
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
