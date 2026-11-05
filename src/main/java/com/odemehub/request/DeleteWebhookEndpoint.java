package com.odemehub.request;

import java.util.Map;

/**
 * Taking a webhook address away, named by its token in the address and again
 * in the body. Nothing is posted there after.
 */
public final class DeleteWebhookEndpoint extends Message {

    private final String token;

    /**
     * @param token The address's token in the gateway.
     */
    public DeleteWebhookEndpoint(String token) {
        this.token = Fields.required(token, "token");
    }

    @Override
    public String path() {
        return "delete-webhook-endpoint/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("token", token);
    }
}
