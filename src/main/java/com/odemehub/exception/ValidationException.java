package com.odemehub.exception;

import java.util.List;
import java.util.Map;

/**
 * The request reached the gateway and was signed correctly, but its contents
 * were refused. No payment was attempted.
 */
public class ValidationException extends OdemehubException {

    private static final long serialVersionUID = 1L;

    private final transient Map<String, List<String>> errors;

    public ValidationException(String message, Map<String, List<String>> errors) {
        super(message);
        this.errors = Map.copyOf(errors);
    }

    /** The refused fields, each with the reasons it was refused. */
    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
