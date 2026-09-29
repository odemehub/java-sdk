package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * How a request went, as every answer opens: whether it worked and, only
 * when it did not, what went wrong. Something that worked has nothing to say
 * beyond that it did.
 */
public final class Result {

    private final boolean successful;
    private final String message;

    private Result(JsonNode body) {
        JsonNode result = body.path("result");
        this.successful = Read.bool(result.path("successful"));
        this.message = Read.nonEmptyString(result.path("message"));
    }

    public static Result fromBody(JsonNode body) {
        return new Result(body);
    }

    public boolean isSuccessful() {
        return successful;
    }

    /** What went wrong, for a request that did not work; null otherwise. */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result[successful=" + successful + ", message=" + message + "]";
    }
}
