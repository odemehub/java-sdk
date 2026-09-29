package com.odemehub.request;

/**
 * Letting go of a kept card. It is dropped at the provider first and with
 * the gateway after: a card the provider would not let go of stays, and the
 * answer says why.
 */
public final class DeleteSavedCard extends SavedCardMessage {

    public DeleteSavedCard(NamedCustomer customer, String savedCardToken) {
        this(customer, savedCardToken, null);
    }

    public DeleteSavedCard(NamedCustomer customer, String savedCardToken, String channelToken) {
        super(customer, savedCardToken, channelToken);
    }

    @Override
    public String path() {
        return "delete-saved-card";
    }
}
