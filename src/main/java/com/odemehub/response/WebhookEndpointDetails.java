package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer about one webhook address: put down just now, or changed.
 */
public final class WebhookEndpointDetails {

    private final Result result;
    private final WebhookEndpoint webhookEndpoint;

    private WebhookEndpointDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.webhookEndpoint = WebhookEndpoint.in(body.path("webhook_endpoint"));
    }

    public static WebhookEndpointDetails fromBody(JsonNode body) {
        return new WebhookEndpointDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The address as it now stands. */
    public WebhookEndpoint getWebhookEndpoint() {
        return webhookEndpoint;
    }

    @Override
    public String toString() {
        return "WebhookEndpointDetails[result=" + result + ", webhookEndpoint=" + webhookEndpoint + "]";
    }
}
