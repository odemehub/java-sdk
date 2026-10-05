package com.odemehub.request;

import java.util.Map;

/**
 * Asking after records of one kind. They are named one of three ways: by the
 * token the gateway gave one, by the merchant's own reference for them, or by
 * the days they were made on, as {@code YYYY-MM-DD} in the team's own
 * timezone, both ends included and at most seven days apart. Asked with none
 * of these, it is the last seven days up to today. The answer is always a
 * list, oldest first, and an empty one when nothing matches. Nothing is
 * changed by asking.
 */
public abstract class Retrieve extends Message {

    private final String token;

    private final String reference;

    private final String createdFrom;

    private final String createdTo;

    protected Retrieve(String token, String reference, String createdFrom, String createdTo) {
        this.token = token;
        this.reference = reference;
        this.createdFrom = createdFrom;
        this.createdTo = createdTo;
    }

    /**
     * The field the merchant's own reference travels in.
     */
    protected String referenceField() {
        return "reference";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said(
            "token", token,
            referenceField(), reference,
            "created_from", createdFrom,
            "created_to", createdTo
        );
    }
}
