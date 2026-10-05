package com.odemehub;

import java.time.Duration;
import java.util.Objects;

/**
 * The address the gateway is reached at and the credentials it is reached
 * with. A credential pair belongs to a single team, and the team is part of
 * the address, so a pair only ever opens its own team's endpoints.
 */
public final class Options {

    /** The header the API key travels in. */
    public static final String API_KEY_HEADER = "X-Api-Key";

    private final String baseUrl;
    private final String team;
    private final String apiKey;
    private final String apiSecret;
    private final Duration timeout;

    private Options(Builder builder) {
        this.baseUrl = Objects.requireNonNull(builder.baseUrl, "baseUrl zorunludur.");
        this.team = Objects.requireNonNull(builder.team, "team zorunludur.");
        this.apiKey = Objects.requireNonNull(builder.apiKey, "apiKey zorunludur.");
        this.apiSecret = Objects.requireNonNull(builder.apiSecret, "apiSecret zorunludur.");
        this.timeout = builder.timeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** The address the application is served from, e.g. https://app.odemehub.com. */
    public String getBaseUrl() {
        return baseUrl;
    }

    /** The team the payments are made on behalf of: the ten-digit workspace id the Entegrasyon page shows. */
    public String getTeam() {
        return team;
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
     * The path of a gateway endpoint for this team, as it is signed: with the
     * leading slash and nothing in front of it.
     */
    public String path(String endpoint) {
        return "/api/" + team + "/gateway/" + endpoint;
    }

    /**
     * The full address of a gateway endpoint for this team.
     */
    public String url(String endpoint) {
        return baseUrl.replaceAll("/+$", "") + path(endpoint);
    }

    @Override
    public String toString() {
        return "Options[baseUrl=" + baseUrl + ", team=" + team + ", timeout=" + timeout + "]";
    }

    public static final class Builder {

        private String baseUrl;
        private String team;
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
