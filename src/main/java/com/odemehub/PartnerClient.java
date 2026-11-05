package com.odemehub;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.request.Message;
import com.odemehub.response.PartnerTeam;
import java.net.http.HttpClient;

/**
 * A partner's own endpoints, as the partner's application talks to them:
 * the teams it opens, asks after, switches the modules of and deletes. Every
 * request leaves signed with the partner's secret and every answer is
 * checked against it, exactly as the team's gateway does it with the team's.
 *
 * <p>Everything done inside a team — its payment accounts, its webhook
 * addresses, its checkout, its payments — goes through that team's own
 * gateway with the credentials each team comes back with:
 * {@link #teamClient(PartnerTeam)} makes the {@link Client} for it.
 *
 * <p>A client holds no state beyond its options, so one can be shared across
 * threads for the life of the application.
 */
public final class PartnerClient {

    private final PartnerOptions options;
    private final HttpClient http;
    private final Transport transport;

    public PartnerClient(PartnerOptions options) {
        this(options, HttpClient.newBuilder().connectTimeout(options.getTimeout()).build());
    }

    /**
     * @param http The HTTP client the requests go through, for a partner that
     *             already configures one (a proxy, an executor). The team
     *             clients {@link #teamClient(PartnerTeam)} makes go through
     *             it too.
     */
    public PartnerClient(PartnerOptions options, HttpClient http) {
        this.options = options;
        this.http = http;
        this.transport = new Transport(http, new Signature(options.getApiSecret()), options.getApiKey(), options.getTimeout());
    }

    /**
     * Open a team the partner owns, with the name, timezone and modules
     * given. It comes back with everything the partner's application needs
     * to work inside it: its number, its gateway's address and its
     * credentials. Keep the credentials; {@code retrieveTeams} hands them
     * over again.
     */
    public com.odemehub.response.PartnerTeamDetails createTeam(com.odemehub.request.CreateTeam team) {
        return com.odemehub.response.PartnerTeamDetails.fromBody(send(team));
    }

    /**
     * The teams the partner opened — by number, or every one of them — a
     * page at a time, oldest first, each with its credentials.
     */
    public com.odemehub.response.PartnerTeamList retrieveTeams(com.odemehub.request.RetrieveTeams teams) {
        return com.odemehub.response.PartnerTeamList.fromBody(send(teams));
    }

    /**
     * Leave a team with just the modules sent switched on; the ones only
     * ödemehub switches stay as they are.
     */
    public com.odemehub.response.PartnerTeamDetails updateTeamModules(com.odemehub.request.UpdateTeamModules team) {
        return com.odemehub.response.PartnerTeamDetails.fromBody(send(team));
    }

    /**
     * Delete a team for good, with everything in it. Its credentials stop
     * working at once.
     */
    public com.odemehub.response.DeletedTeam deleteTeam(com.odemehub.request.DeleteTeam team) {
        return com.odemehub.response.DeletedTeam.fromBody(send(team));
    }

    /**
     * The client for a team's own gateway, reached at this client's address
     * with the team's number and credentials, through the same HTTP client
     * and with the same timeout.
     */
    public Client teamClient(PartnerTeam team) {
        return new Client(Options.builder()
            .baseUrl(options.getBaseUrl())
            .team(team.getSlug())
            .apiKey(team.getApiKey())
            .apiSecret(team.getApiSecret())
            .timeout(options.getTimeout())
            .build(), http);
    }

    /**
     * Hand a message to the partner's endpoints and read the answer back,
     * signed and checked with the partner's secret.
     */
    private JsonNode send(Message message) {
        return transport.send(message, options.path(message.path()), options.url(message.path()));
    }
}
