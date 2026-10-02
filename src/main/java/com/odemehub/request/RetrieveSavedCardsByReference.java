package com.odemehub.request;

import java.util.Map;

/**
 * The cards kept for a customer, named by what a card is kept under: the
 * channel and the merchant's reference for the customer. The card they pay
 * with unless they say otherwise comes first, the rest oldest first; a
 * customer with none answers an empty list.
 */
public final class RetrieveSavedCardsByReference extends ChannelMessage {

    private final String customerReference;

    /**
     * @param customerReference The key the merchant keeps the customer under.
     */
    public RetrieveSavedCardsByReference(String customerReference) {
        this(customerReference, null);
    }

    public RetrieveSavedCardsByReference(String customerReference, String channelToken) {
        super(channelToken);
        this.customerReference = Fields.required(customerReference, "customerReference");
    }

    @Override
    public String path() {
        return "retrieve-saved-cards-by-reference";
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.of(
            "channel_token", channel(channelToken),
            "customer_reference", customerReference
        );
    }
}
