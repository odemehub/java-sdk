package com.odemehub.request;

import java.util.Map;

/**
 * Something done to one of a customer's kept cards. The card is named by the
 * token the gateway gave it, and the customer alongside it, so a card can
 * only ever be reached through the customer it belongs to.
 */
public abstract class SavedCardMessage extends ChannelMessage {

    private final NamedCustomer customer;
    private final String savedCardToken;

    /**
     * @param savedCardToken The card's token in the gateway, as a listing of the customer's cards gave it.
     * @param channelToken   The channel this one message speaks for, or null for the client's own.
     */
    protected SavedCardMessage(NamedCustomer customer, String savedCardToken, String channelToken) {
        super(channelToken);
        this.customer = Fields.required(customer, "customer");
        this.savedCardToken = Fields.required(savedCardToken, "savedCardToken");
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "customer", customer.toBody(channel(channelToken)),
            "saved_card", Fields.of("token", savedCardToken)
        );
    }
}
