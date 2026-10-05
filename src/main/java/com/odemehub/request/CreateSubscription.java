package com.odemehub.request;

import com.odemehub.enums.Period;
import java.util.Map;

/**
 * A subscription opened for a customer, its first renewal paid on the
 * gateway's own checkout page and the rest taken from the card kept then.
 * The answer carries the checkout address; the customer is sent there, pays
 * with a card the gateway keeps as their default, and is posted back to the
 * success address. The addresses the team set under Webhook in the panel hear
 * every change of state after that: each renewal paid, one that could not
 * be, the cancellation, the end.
 *
 * <p>The customer needs a reference: the card the renewals are taken from is
 * kept for the team's customer under it. The account, named or the default, has to keep
 * cards and take 3D payments, and the plan has to cover saved cards.
 */
public final class CreateSubscription extends CheckoutMessage {

    private final Period period;
    private final Integer renewalLimit;

    private CreateSubscription(Builder builder) {
        super(builder);
        Fields.required(builder.reference, "reference");
        Fields.required(builder.successUrl, "successUrl");
        Fields.required(builder.items, "items");
        Fields.required(builder.customer, "customer");
        this.period = Fields.required(builder.period, "period");
        this.renewalLimit = builder.renewalLimit;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-subscription";
    }

    @Override
    protected String group() {
        return "subscription";
    }

    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> group = details();
        group.put("period", period.getValue());

        if (renewalLimit != null) {
            group.put("renewal_limit", renewalLimit);
        }

        return body(group);
    }

    public static final class Builder extends CheckoutMessage.Builder<Builder> {

        private Period period;
        private Integer renewalLimit;

        private Builder() {
        }

        /** How often a renewal comes round. */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /** How many renewals are paid in all, 1 to 1000, after which it is completed. Left out, it runs until it is called off. */
        public Builder renewalLimit(int renewalLimit) {
            this.renewalLimit = renewalLimit;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public CreateSubscription build() {
            return new CreateSubscription(this);
        }
    }
}
