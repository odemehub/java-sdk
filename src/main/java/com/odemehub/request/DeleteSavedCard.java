package com.odemehub.request;

import java.util.Map;

/**
 * Letting go of a kept card, named by its token in the address and again in
 * the body. It is dropped at the provider first and with the gateway after:
 * a card the provider would not let go of stays, and the answer says why.
 */
public final class DeleteSavedCard extends Message {

    private final String token;

    /**
     * @param token The card's token in the gateway.
     */
    public DeleteSavedCard(String token) {
        this.token = Fields.required(token, "token");
    }

    @Override
    public String path() {
        return "delete-saved-card/" + token;
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of("token", token);
    }
}
