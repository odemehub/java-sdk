package com.odemehub.exception;

/**
 * The answer did not carry the signature it should have. It was not signed
 * with the secret this client holds, so it cannot be shown to have come from
 * the gateway and must not be acted on.
 */
public class SignatureException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public SignatureException(String message) {
        super(message);
    }
}
