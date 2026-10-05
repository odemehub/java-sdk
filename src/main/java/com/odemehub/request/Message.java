package com.odemehub.request;

import java.util.Map;

/**
 * Something handed to the gateway. Everything sent there is plain JSON,
 * signed as a whole by the client, so what is common to all of them is the
 * endpoint it goes to, the method it goes with and the body it is sent as.
 *
 */
public abstract class Message {

    /**
     * The endpoint this is sent to, under the team's gateway, with the token
     * in the address where the endpoint takes one.
     */
    public abstract String path();

    /**
     * The HTTP method this goes with: every endpoint is posted to.
     */
    public final String method() {
        return "POST";
    }

    /**
     * The request body, in the snake_case the gateway speaks.
     */
    public abstract Map<String, Object> toBody();
}
