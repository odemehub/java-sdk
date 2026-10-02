package com.odemehub.exception;

/**
 * The gateway knows who is asking but will not let it do this: the team
 * cannot take payments at the moment, or its plan does not cover the
 * endpoint, or the module behind it is switched off on the panel.
 */
public class ForbiddenException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String message) {
        super(message);
    }
}
