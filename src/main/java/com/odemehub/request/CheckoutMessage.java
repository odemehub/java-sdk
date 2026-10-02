package com.odemehub.request;

import com.odemehub.enums.Currency;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An order or a subscription, opened or changed, to be paid on the gateway's
 * own checkout page. Nothing is charged here: the answer carries the address
 * to send the customer to, and they give their card there.
 *
 * <p>What it comes to is not sent. The gateway adds up the lines and the
 * shipping method the payer picks and answers with the amount, so the total
 * can never disagree with what it is made up of. The customer is whatever is
 * known of them: it is filled in on the checkout page and the payer is asked
 * for the rest.
 *
 * <p>Opening is idempotent per channel reference: opening again under a
 * reference that already has an open order or subscription overwrites it with
 * what is sent and answers with the one that was there, under its own token.
 * A paid order, a subscription that has been paid, or one with a payment
 * under way is not touched; the gateway says so on {@code channel_reference}.
 */
public abstract class CheckoutMessage extends ChannelMessage {

    private final String channelReference;
    private final String successUrl;
    private final List<Item> items;
    private final Customer customer;
    private final String cancelUrl;
    private final String description;
    private final Currency currency;
    private final String paymentProviderToken;
    private final Boolean requiresShippingAddress;
    private final List<ShippingMethod> shippingMethods;
    private final List<String> clear;

    protected CheckoutMessage(Builder<?> builder) {
        super(builder.channelToken);
        this.channelReference = builder.channelReference;
        this.successUrl = builder.successUrl;
        this.items = builder.items == null ? null : List.copyOf(builder.items);
        this.customer = builder.customer;
        this.cancelUrl = builder.cancelUrl;
        this.description = builder.description;
        this.currency = builder.currency;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.requiresShippingAddress = builder.requiresShippingAddress;
        this.shippingMethods = builder.shippingMethods == null ? null : List.copyOf(builder.shippingMethods);
        this.clear = List.copyOf(builder.clear);
    }

    /**
     * The key the group travels under: {@code order} or {@code subscription}.
     */
    protected abstract String group();

    /**
     * The group's fields, with what the caller left unsaid left out.
     *
     * @param channel The channel to write, or null to leave it as it is.
     */
    protected Map<String, Object> details(String channel) {
        return Fields.said(
            "channel_token", channel,
            "channel_reference", channelReference,
            "description", description,
            "payment_provider_token", paymentProviderToken,
            "currency", currency == null ? null : currency.getValue(),
            "success_url", successUrl,
            "cancel_url", cancelUrl,
            "requires_shipping_address", requiresShippingAddress,
            "items", Fields.each(items, Item::toBody),
            "shipping_methods", Fields.each(shippingMethods, ShippingMethod::toBody)
        );
    }

    /**
     * The fields a change sets to nothing, written onto the group.
     */
    protected Map<String, Object> cleared(Map<String, Object> group) {
        return Fields.cleared(group, clear);
    }

    /**
     * The body: the group and, beside it, the customer when one is given.
     */
    protected Map<String, Object> body(Map<String, Object> group) {
        return Fields.said(
            group(), group,
            "customer", customer == null ? null : customer.toBody()
        );
    }

    public abstract static class Builder<B extends Builder<B>> extends ChannelMessage.Builder<B> {

        protected String channelReference;
        protected String successUrl;
        protected List<Item> items;
        protected Customer customer;
        protected String cancelUrl;
        protected String description;
        protected Currency currency;
        protected String paymentProviderToken;
        protected Boolean requiresShippingAddress;
        protected List<ShippingMethod> shippingMethods;
        protected final List<String> clear = new ArrayList<>();

        /** The reference it is known by in the calling system. It has to carry at least one digit. */
        public B channelReference(String channelReference) {
            this.channelReference = channelReference;
            return self();
        }

        /**
         * Where the customer's browser is posted back to once it is paid, with
         * the payment's token. An http(s) address reachable from the internet.
         */
        public B successUrl(String successUrl) {
            this.successUrl = successUrl;
            return self();
        }

        /**
         * What it is for: 1 to 100 lines. Sent on a change, they replace every
         * line there was.
         */
        public B items(List<Item> items) {
            this.items = items;
            return self();
        }

        /** Who it is for, as far as it is known. Sent on a change, it is merged over what there was. */
        public B customer(Customer customer) {
            this.customer = customer;
            return self();
        }

        /** Where the customer goes if they turn back without paying; shown as a link on the checkout page. */
        public B cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return self();
        }

        public B description(String description) {
            this.description = description;
            return self();
        }

        /** Left out of a new one, the gateway takes the lira. */
        public B currency(Currency currency) {
            this.currency = currency;
            return self();
        }

        /**
         * The payment account it is paid through, by its token. Left out, the
         * merchant's Gate rules pick the account when the customer pays, and
         * its default account is used where none of them holds.
         */
        public B paymentProviderToken(String paymentProviderToken) {
            this.paymentProviderToken = paymentProviderToken;
            return self();
        }

        /** Whether the checkout page asks the payer where the goods go. */
        public B requiresShippingAddress(boolean requiresShippingAddress) {
            this.requiresShippingAddress = requiresShippingAddress;
            return self();
        }

        /**
         * How the goods may be sent, for the payer to pick from; up to twenty.
         * Sent on a change, they replace the ones there were, and an empty list
         * removes them all.
         */
        public B shippingMethods(List<ShippingMethod> shippingMethods) {
            this.shippingMethods = shippingMethods;
            return self();
        }

        /**
         * Fields of the group a change sets to nothing, by their wire names.
         * Leaving a field out of a change keeps what was there, so clearing one
         * is said on purpose.
         */
        protected B clearing(String... fields) {
            this.clear.addAll(Arrays.asList(fields));
            return self();
        }
    }
}
