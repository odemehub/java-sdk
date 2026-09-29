package com.odemehub;

import java.time.Duration;
import java.util.Objects;

/**
 * The address the gateway is reached at, the credentials it is reached with
 * and the channel the caller speaks for. A credential pair belongs to a
 * single team, and the team is part of the address, so a pair only ever
 * opens its own team's endpoints.
 */
public final class Options {

    /** The header the API key travels in. */
    public static final String API_KEY_HEADER = "X-Api-Key";

    private final String baseUrl;
    private final String team;
    private final String channelToken;
    private final String apiKey;
    private final String apiSecret;
    private final Duration timeout;

    private Options(Builder builder) {
        this.baseUrl = Objects.requireNonNull(builder.baseUrl, "baseUrl zorunludur.");
        this.team = Objects.requireNonNull(builder.team, "team zorunludur.");
        this.channelToken = Objects.requireNonNull(builder.channelToken, "channelToken zorunludur.");
        this.apiKey = Objects.requireNonNull(builder.apiKey, "apiKey zorunludur.");
        this.apiSecret = Objects.requireNonNull(builder.apiSecret, "apiSecret zorunludur.");
        this.timeout = builder.timeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** The address the application is served from, e.g. https://odeme.gurmehub.com. */
    public String getBaseUrl() {
        return baseUrl;
    }

    /** The team the payments are made on behalf of, as the Entegrasyon page names it. */
    public String getTeam() {
        return team;
    }

    /**
     * The channel every request speaks for: the shop, the marketplace or the
     * branch the customer reached the merchant through, by the token the
     * team's own Kanallar page gives it. A merchant selling on more than one
     * channel may still name another on a single request.
     */
    public String getChannelToken() {
        return channelToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    /** How long a request may take. */
    public Duration getTimeout() {
        return timeout;
    }

    /**
     * The full address of a gateway endpoint for this team.
     */
    public String url(String path) {
        return baseUrl.replaceAll("/+$", "") + "/api/" + team + "/gateway/" + path;
    }

    @Override
    public String toString() {
        return "Options[baseUrl=" + baseUrl + ", team=" + team + ", channelToken=" + channelToken + ", timeout=" + timeout + "]";
    }

    public static final class Builder {

        private String baseUrl;
        private String team;
        private String channelToken;
        private String apiKey;
        private String apiSecret;
        private Duration timeout = Duration.ofMinutes(1);

        private Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder team(String team) {
            this.team = team;
            return this;
        }

        public Builder channelToken(String channelToken) {
            this.channelToken = channelToken;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        /** How long a request may take. Left out, a minute. */
        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Options build() {
            return new Options(this);
        }
    }
}
