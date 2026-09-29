package com.odemehub.request;

/**
 * Making one of a customer's kept cards the one they pay with unless they
 * say otherwise. A customer has one such card; the one that was it before
 * stops being it as this one is written down.
 */
public final class DefaultSavedCard extends SavedCardMessage {

    public DefaultSavedCard(NamedCustomer customer, String savedCardToken) {
        this(customer, savedCardToken, null);
    }

    public DefaultSavedCard(NamedCustomer customer, String savedCardToken, String channelToken) {
        super(customer, savedCardToken, channelToken);
    }

    @Override
    public String path() {
        return "default-saved-card";
    }
}
