package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Webhook addresses asked after. The answer is always a list, and an empty
 * one when nothing matched; it is not cut into pages.
 */
public final class WebhookEndpointList {

    private final Result result;
    private final List<WebhookEndpoint> webhookEndpoints;

    private WebhookEndpointList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.webhookEndpoints = Read.list(body.path("webhook_endpoints"), WebhookEndpoint::fromBody);
    }

    public static WebhookEndpointList fromBody(JsonNode body) {
        return new WebhookEndpointList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<WebhookEndpoint> getWebhookEndpoints() {
        return webhookEndpoints;
    }

    @Override
    public String toString() {
        return "WebhookEndpointList[result=" + result + ", webhookEndpoints=" + webhookEndpoints + "]";
    }
}
