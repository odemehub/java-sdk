package com.odemehub;

import java.time.Duration;
import java.util.Objects;

/**
 * The address a partner's own endpoints are reached at and the partner's
 * credentials. They are the partner's, not a team's: they open, ask after,
 * switch the modules of and delete the teams the partner opened, and never
 * reach into a team's gateway. Everything done inside a team is done with
 * that team's own credentials, through a {@link Client}.
 */
public final class PartnerOptions {

    private final String baseUrl;
    private final String apiKey;
    private final String apiSecret;
    private final Duration timeout;

    private PartnerOptions(Builder builder) {
        this.baseUrl = Objects.requireNonNull(builder.baseUrl, "baseUrl zorunludur.");
        this.apiKey = Objects.requireNonNull(builder.apiKey, "apiKey zorunludur.");
        this.apiSecret = Objects.requireNonNull(builder.apiSecret, "apiSecret zorunludur.");
        this.timeout = builder.timeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** The address the application is served from, e.g. https://sandbox.odemehub.com while building and https://app.odemehub.com once live. */
    public String getBaseUrl() {
        return baseUrl;
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
     * The path of a partner endpoint, as it is signed: with the leading
     * slash and nothing in front of it.
     */
    public String path(String endpoint) {
        return "/api/partner/" + endpoint;
    }

    /**
     * The full address of a partner endpoint.
     */
    public String url(String endpoint) {
        return baseUrl.replaceAll("/+$", "") + path(endpoint);
    }

    @Override
    public String toString() {
        return "PartnerOptions[baseUrl=" + baseUrl + ", timeout=" + timeout + "]";
    }

    public static final class Builder {

        private String baseUrl;
        private String apiKey;
        private String apiSecret;
        private Duration timeout = Duration.ofMinutes(1);

        private Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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

        public PartnerOptions build() {
            return new PartnerOptions(this);
        }
    }
}
