package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A partner's teams asked after, a page of them. The answer is always a
 * list, oldest first, and an empty one when nothing matched.
 */
public final class PartnerTeamList {

    private final Result result;
    private final List<PartnerTeam> teams;
    private final Pagination pagination;

    private PartnerTeamList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.teams = Read.list(body.path("teams"), PartnerTeam::fromBody);
        this.pagination = Pagination.in(body.path("pagination"));
    }

    public static PartnerTeamList fromBody(JsonNode body) {
        return new PartnerTeamList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<PartnerTeam> getTeams() {
        return teams;
    }

    /** Where this page stands among the pages the answer is cut into. */
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public String toString() {
        return "PartnerTeamList[result=" + result + ", teams=" + teams + ", pagination=" + pagination + "]";
    }
}
