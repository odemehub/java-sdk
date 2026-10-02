package com.odemehub.exception;

/**
 * The gateway did not accept the credentials: either the API key is not the
 * one issued to the team in the address, or the request was not signed with
 * the matching secret, or it was signed too long ago (the clock is more than
 * five minutes off).
 */
public class AuthenticationException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
