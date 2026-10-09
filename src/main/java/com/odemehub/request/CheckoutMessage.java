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
 * shipping method the payer picks from the team's own list and answers with
 * the amount, so the total
 * can never disagree with what it is made up of. The customer is whatever is
 * known of them: it is filled in on the checkout page and the payer is asked
 * for the rest.
 *
 * <p>The reference is the merchant's own and need not be unique: every
 * opening is a new order or subscription under a new token, even under a
 * reference sent before, and nothing already there is written over. Keep the
 * token each answer comes back with; that is what names it from then on.
 */
public abstract class CheckoutMessage extends Message {

    private final String reference;
    private final String successUrl;
    private final List<Item> items;
    private final Customer customer;
    private final String cancelUrl;
    private final String description;
    private final Currency currency;
    private final String paymentProviderToken;
    private final Boolean requiresShipping;
    private final Boolean locksCustomer;
    private final Boolean emailsCustomer;
    private final List<String> clear;

    protected CheckoutMessage(Builder<?> builder) {
        this.reference = builder.reference;
        this.successUrl = builder.successUrl;
        this.items = builder.items == null ? null : List.copyOf(builder.items);
        this.customer = builder.customer;
        this.cancelUrl = builder.cancelUrl;
        this.description = builder.description;
        this.currency = builder.currency;
        this.paymentProviderToken = builder.paymentProviderToken;
        this.requiresShipping = builder.requiresShipping;
        this.locksCustomer = builder.locksCustomer;
        this.emailsCustomer = builder.emailsCustomer;
        this.clear = List.copyOf(builder.clear);
    }

    /**
     * The key the group travels under: {@code order} or {@code subscription}.
     */
    protected abstract String group();

    /**
     * The group's fields, with what the caller left unsaid left out.
     */
    protected Map<String, Object> details() {
        return Fields.said(
            "reference", reference,
            "description", description,
            "payment_provider_token", paymentProviderToken,
            "currency", currency == null ? null : currency.getValue(),
            "success_url", successUrl,
            "cancel_url", cancelUrl,
            "requires_shipping", requiresShipping,
            "locks_customer", locksCustomer,
            "emails_customer", emailsCustomer,
            "items", Fields.each(items, Item::toBody)
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

    public abstract static class Builder<B extends Builder<B>> {
        protected String reference;
        protected String successUrl;
        protected List<Item> items;
        protected Customer customer;
        protected String cancelUrl;
        protected String description;
        protected Currency currency;
        protected String paymentProviderToken;
        protected Boolean requiresShipping;
        protected Boolean locksCustomer;
        protected Boolean emailsCustomer;
        protected final List<String> clear = new ArrayList<>();

        protected abstract B self();

        /** The reference it is known by in the calling system. It has to carry at least one digit, and may be sent again. */
        public B reference(String reference) {
            this.reference = reference;
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

        /**
         * Whether the checkout page asks the payer where the goods go. One who
         * is picks a way of sending from the team's own list, of those that
         * send there, and its price is added to the amount.
         */
        public B requiresShipping(boolean requiresShipping) {
            this.requiresShipping = requiresShipping;
            return self();
        }

        /**
         * Whether the customer stays as sent: the checkout page asks the payer
         * nothing about who they are and only shows it. Takes a customer with
         * a whole billing address, and a whole shipping address too when the
         * goods are sent.
         */
        public B locksCustomer(boolean locksCustomer) {
            this.locksCustomer = locksCustomer;
            return self();
        }

        /**
         * Whether the customer is sent an e-mail at their billing address: on
         * an order once it is paid, on a subscription whenever where it stands
         * changes.
         */
        public B emailsCustomer(boolean emailsCustomer) {
            this.emailsCustomer = emailsCustomer;
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
