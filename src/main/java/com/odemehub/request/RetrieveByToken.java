package com.odemehub.request;

import java.util.Map;

/**
 * One record asked after by the token the gateway gave it, as
 * {@code GET retrieve-{resource}/{token}}. There is no body: the token
 * travels in the address and the signature is taken over the empty string.
 * A caller only ever reaches its own team's records; anybody else's is
 * answered as though it did not exist. Nothing is changed by asking.
 */
public abstract class RetrieveByToken extends Message {

    private final String token;

    /**
     * @param token The record's token in the gateway, as it was answered when the record was made.
     */
    protected RetrieveByToken(String token) {
        this.token = Fields.required(token, "token");
    }

    /**
     * The endpoint, without the token.
     */
    protected abstract String endpoint();

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public String path() {
        return endpoint() + "/" + token;
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Map.of();
    }
}
