package com.odemehub.exception;

/**
 * The gateway could not be reached at all. Whether the payment was made is
 * unknown; the payment record on the gateway says what actually happened.
 */
public class TransportException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
