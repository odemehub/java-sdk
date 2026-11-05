package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one of a partner's teams: opened just now, or its modules
 * switched.
 */
public final class PartnerTeamDetails {

    private final Result result;
    private final PartnerTeam team;

    private PartnerTeamDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.team = PartnerTeam.in(body.path("team"));
    }

    public static PartnerTeamDetails fromBody(JsonNode body) {
        return new PartnerTeamDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The team as it now stands, with its credentials. */
    public PartnerTeam getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "PartnerTeamDetails[result=" + result + ", team=" + team + "]";
    }
}
