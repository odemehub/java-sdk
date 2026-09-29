package com.odemehub.request;

import java.util.Map;

/**
 * The cards a customer has let the merchant keep, asked for by naming the
 * customer. The card that is theirs by default comes first.
 */
public final class SavedCards extends ChannelMessage {

    private final NamedCustomer customer;

    public SavedCards(NamedCustomer customer) {
        this(customer, null);
    }

    public SavedCards(NamedCustomer customer, String channelToken) {
        super(channelToken);
        this.customer = Fields.required(customer, "customer");
    }

    @Override
    public String path() {
        return "saved-cards";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of("customer", customer.toBody(channel(channelToken)));
    }
}
