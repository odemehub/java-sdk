package com.odemehub.request;

import com.odemehub.enums.Period;
import com.odemehub.enums.SubscriptionStatus;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A change to a subscription, named by its token in the address and again in
 * the body. Only what is sent is written: lines sent replace the lines and
 * re-price every renewal not yet paid, a new period reaches the next
 * renewal, the renewal limit may not fall below what has already been paid,
 * and customer fields sent are merged over the ones there were.
 *
 * <p>Until the first payment anything about it may be changed. Once it has
 * been paid, what it renews on stays as it was opened: the channel, the
 * account, the currency, the period and the customer's reference are turned
 * down if sent with another value.
 *
 * <p>This is also how a subscription is called off: send the status
 * {@code cancelled}, the one status a merchant may set. Nothing is charged
 * after that and nothing is given back; a renewal already paid is served to
 * its end. A subscription that is over, or has a payment under way, cannot
 * be changed; the gateway says so on {@code token}.
 *
 * <p>The channel is written only when this message names one; the client's
 * own is not sent.
 */
public final class UpdateSubscription extends CheckoutMessage {

    private final String token;
    private final SubscriptionStatus status;
    private final Period period;
    private final Integer renewalLimit;

    private UpdateSubscription(Builder builder) {
        super(builder);
        this.token = Fields.required(builder.token, "token");
        this.status = builder.status;
        this.period = builder.period;
        this.renewalLimit = builder.renewalLimit;
    }

    /**
     * @param token The subscription's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-subscription/" + token;
    }

    @Override
    protected String group() {
        return "subscription";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        Map<String, Object> group = details(namedChannel());
        group.putAll(Fields.said(
            "period", period == null ? null : period.getValue(),
            "renewal_limit", renewalLimit,
            "status", status == null ? null : status.getValue()
        ));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", token);
        body.putAll(body(cleared(group)));

        return body;
    }

    public static final class Builder extends CheckoutMessage.Builder<Builder> {

        private final String token;
        private SubscriptionStatus status;
        private Period period;
        private Integer renewalLimit;

        private Builder(String token) {
            this.token = token;
        }

        /** Only {@link SubscriptionStatus#CANCELLED} is taken: it calls the subscription off. The other states follow the payments. */
        public Builder status(SubscriptionStatus status) {
            this.status = status;
            return this;
        }

        /** Only until the first payment. */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /** 1 to 1000, never fewer than the renewals already paid. */
        public Builder renewalLimit(int renewalLimit) {
            this.renewalLimit = renewalLimit;
            return this;
        }

        /**
         * Fields set to nothing, by their wire names: {@code renewal_limit}
         * (runs until it is called off), {@code description},
         * {@code cancel_url}, {@code payment_provider_token}.
         */
        public Builder clear(String... fields) {
            return clearing(fields);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public UpdateSubscription build() {
            return new UpdateSubscription(this);
        }
    }
}
