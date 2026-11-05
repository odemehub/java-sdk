package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A partner's team deleted for good, with everything in it.
 */
public final class DeletedTeam {

    private final Result result;
    private final String slug;

    private DeletedTeam(JsonNode body) {
        this.result = Result.fromBody(body);
        this.slug = Read.string(body.path("team").path("slug"));
    }

    public static DeletedTeam fromBody(JsonNode body) {
        return new DeletedTeam(body);
    }

    public Result getResult() {
        return result;
    }

    /** The team's number. */
    public String getSlug() {
        return slug;
    }

    @Override
    public String toString() {
        return "DeletedTeam[result=" + result + ", slug=" + slug + "]";
    }
}
