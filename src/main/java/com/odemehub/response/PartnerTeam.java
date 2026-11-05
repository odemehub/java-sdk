package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A team a partner opened, as the partner is told of it: its number, what it
 * is called, the modules it uses, and everything the partner's application
 * needs to reach the team's own gateway — the address, whether payments
 * there are test ones, and the team's credentials.
 */
public final class PartnerTeam {

    private final String slug;
    private final String name;
    private final String timezone;
    private final boolean isTest;
    private final List<String> modules;
    private final String gatewayUrl;
    private final String apiKey;
    private final String apiSecret;
    private final String createdAt;

    private PartnerTeam(JsonNode team) {
        this.slug = Read.string(team.path("slug"));
        this.name = Read.string(team.path("name"));
        this.timezone = Read.string(team.path("timezone"));
        this.isTest = Read.bool(team.path("is_test"));
        this.modules = Read.list(team.path("modules"), Read::string);
        this.gatewayUrl = Read.string(team.path("gateway_url"));
        this.apiKey = Read.string(team.path("api_key"));
        this.apiSecret = Read.string(team.path("api_secret"));
        this.createdAt = Read.optionalString(team.path("created_at"));
    }

    public static PartnerTeam fromBody(JsonNode team) {
        return new PartnerTeam(team);
    }

    /**
     * The team an answer carries under this key, or null when it carries none.
     */
    static PartnerTeam in(JsonNode team) {
        return team.isObject() ? new PartnerTeam(team) : null;
    }

    /** The team's ten-digit number: the {@code team} of its {@code Options}, and what the partner names it by. */
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

    /** Whether payments made at the team are test ones. */
    public boolean isTest() {
        return isTest;
    }

    /**
     * The modules the team uses, as the gateway writes them: {@code api},
     * {@code orders}... Ones only ödemehub switches may be among them.
     */
    public List<String> getModules() {
        return modules;
    }

    /** The address of the team's gateway: https://app.odemehub.com/api/4829301756/gateway. */
    public String getGatewayUrl() {
        return gatewayUrl;
    }

    /** The team's API key. */
    public String getApiKey() {
        return apiKey;
    }

    /** The team's secret; keep it as safe as the partner's own. */
    public String getApiSecret() {
        return apiSecret;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PartnerTeam[slug=" + slug + ", name=" + name + ", timezone=" + timezone + ", isTest=" + isTest
            + ", modules=" + modules + ", gatewayUrl=" + gatewayUrl + ", apiSecret=*****]";
    }
}
