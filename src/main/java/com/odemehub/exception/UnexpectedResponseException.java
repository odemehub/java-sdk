package com.odemehub.exception;

/**
 * The gateway answered with something that is neither a payment outcome nor
 * a refusal this client knows how to read.
 */
public class UnexpectedResponseException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    private final int status;

    public UnexpectedResponseException(String message, int status) {
        super(message);
        this.status = status;
    }

    /** The HTTP status the gateway answered with. */
    public int getStatus() {
        return status;
    }
}
