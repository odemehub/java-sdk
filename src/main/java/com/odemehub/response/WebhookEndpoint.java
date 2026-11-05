package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.WebhookEvent;
import java.util.List;
import java.util.Objects;

/**
 * An address of the team's the gateway posts webhooks to.
 */
public final class WebhookEndpoint {

    private final String token;
    private final String url;
    private final List<String> events;
    private final boolean isActive;
    private final String createdAt;

    private WebhookEndpoint(JsonNode webhookEndpoint) {
        this.token = Read.nonEmptyString(webhookEndpoint.path("token"));
        this.url = Read.string(webhookEndpoint.path("url"));
        this.events = Read.list(webhookEndpoint.path("events"), Read::string);
        this.isActive = Read.bool(webhookEndpoint.path("is_active"));
        this.createdAt = Read.optionalString(webhookEndpoint.path("created_at"));
    }

    public static WebhookEndpoint fromBody(JsonNode webhookEndpoint) {
        return new WebhookEndpoint(webhookEndpoint);
    }

    /**
     * The address an answer carries under this key, or null when it carries none.
     */
    static WebhookEndpoint in(JsonNode webhookEndpoint) {
        return webhookEndpoint.isObject() ? new WebhookEndpoint(webhookEndpoint) : null;
    }

    /** The address's token in the gateway, which names it again later. */
    public String getToken() {
        return token;
    }

    public String getUrl() {
        return url;
    }

    /** The events posted there, as the gateway writes them: {@code order.paid}... */
    public List<String> getEvents() {
        return events;
    }

    /** The events posted there; one this version does not know is left out. */
    public List<WebhookEvent> getWebhookEvents() {
        return events.stream().map(WebhookEvent::from).filter(Objects::nonNull).toList();
    }

    /** Whether anything is posted there. */
    public boolean isActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "WebhookEndpoint[token=" + token + ", url=" + url + ", events=" + events + ", isActive=" + isActive + "]";
    }
}
