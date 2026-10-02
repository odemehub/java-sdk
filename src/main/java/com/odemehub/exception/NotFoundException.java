package com.odemehub.exception;

/**
 * The record the request names is not there: no payment, order, payment
 * link, subscription or kept card of the team's carries that token or that
 * reference. Another team's records are answered the same way.
 */
public class NotFoundException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
