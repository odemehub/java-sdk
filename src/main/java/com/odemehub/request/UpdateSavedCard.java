package com.odemehub.request;

import java.util.Map;

/**
 * A change to a kept card, named by its token in the address and again in
 * the body. The one thing that may be changed is whether it is the
 * customer's default: a card is made the default here, and stops being one
 * when another card of the customer's is made the default instead.
 */
public final class UpdateSavedCard extends Message {

    private final String token;
    private final boolean isDefault;

    /**
     * Make the card the customer's default.
     *
     * @param token The card's token in the gateway.
     */
    public UpdateSavedCard(String token) {
        this(token, true);
    }

    /**
     * @param isDefault Has to be true; the gateway turns down anything else.
     */
    public UpdateSavedCard(String token, boolean isDefault) {
        this.token = Fields.required(token, "token");
        this.isDefault = isDefault;
    }

    @Override
    public String path() {
        return "update-saved-card/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "token", token,
            "saved_card", Fields.of("is_default", isDefault)
        );
    }
}
