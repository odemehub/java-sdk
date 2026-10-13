package com.odemehub.exception;

/**
 * The gateway knows who is asking but will not let it do this: the team
 * cannot take payments at the moment (a balance left unpaid), or the module
 * behind the endpoint is off for it. ödemehub turns modules on; get in touch
 * to have one opened.
 */
public class ForbiddenException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String message) {
        super(message);
    }
}
