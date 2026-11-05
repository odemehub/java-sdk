package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The team itself, as its own gateway tells of it.
 */
public final class Team {

    private final String slug;
    private final String name;
    private final String timezone;

    private Team(JsonNode team) {
        this.slug = Read.string(team.path("slug"));
        this.name = Read.string(team.path("name"));
        this.timezone = Read.string(team.path("timezone"));
    }

    public static Team fromBody(JsonNode team) {
        return new Team(team);
    }

    /**
     * The team an answer carries under this key, or null when it carries none.
     */
    static Team in(JsonNode team) {
        return team.isObject() ? new Team(team) : null;
    }

    /** The team's ten-digit number, the one its gateway address carries. */
    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    /** The timezone the team's days are counted in: "Europe/Istanbul". */
    public String getTimezone() {
        return timezone;
    }

    @Override
    public String toString() {
        return "Team[slug=" + slug + ", name=" + name + ", timezone=" + timezone + "]";
    }
}
