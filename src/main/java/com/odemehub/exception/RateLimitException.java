package com.odemehub.exception;

/**
 * Too many requests in too short a time. Nothing was done; the same request
 * may be sent again once the gateway's wait is over.
 */
public class RateLimitException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    private final Integer retryAfter;

    public RateLimitException(String message, Integer retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }

    /** How long the gateway asked to wait before trying again, in seconds; null when it did not say. */
    public Integer getRetryAfter() {
        return retryAfter;
    }
}
