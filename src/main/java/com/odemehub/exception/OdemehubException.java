package com.odemehub.exception;

/**
 * Base class for everything this client throws, so a caller that does not
 * care which way a payment failed can catch one thing.
 */
public class OdemehubException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OdemehubException(String message) {
        super(message);
    }

    public OdemehubException(String message, Throwable cause) {
        super(message, cause);
    }
}
