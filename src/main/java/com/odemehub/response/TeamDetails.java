package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about the team, once changed.
 */
public final class TeamDetails {

    private final Result result;
    private final Team team;

    private TeamDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.team = Team.in(body.path("team"));
    }

    public static TeamDetails fromBody(JsonNode body) {
        return new TeamDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The team as it now stands. */
    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "TeamDetails[result=" + result + ", team=" + team + "]";
    }
}
