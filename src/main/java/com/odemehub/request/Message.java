package com.odemehub.request;

import java.util.Map;

/**
 * Something handed to the gateway. Everything sent there is plain JSON,
 * signed as a whole by the client, so what is common to all of them is the
 * endpoint it goes to and the body it is sent as.
 *
 * <p>The body is built with the client's channel handed in, because a
 * message that speaks for a channel puts it where its own endpoint expects
 * it; one that does not, such as a refund, simply never reads it.
 */
public abstract class Message {

    /**
     * The endpoint this is sent to, under the team's gateway.
     */
    public abstract String path();

    /**
     * The request body, in the snake_case the gateway speaks.
     *
     * @param channelToken The client's channel, by its token, for the messages that speak for one.
     */
    public abstract Map<String, Object> toBody(String channelToken);
}
