package com.odemehub.exception;

/**
 * The record a change names by its token is not there: no payment, order,
 * payment link, subscription, kept card or bank account of the team's carries
 * that token. Another team's records are answered the same way. Only the
 * calls that update, delete, refund or cancel one record throw it; asking
 * after records, by token, reference or days, answers an empty list instead.
 */
public class NotFoundException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
