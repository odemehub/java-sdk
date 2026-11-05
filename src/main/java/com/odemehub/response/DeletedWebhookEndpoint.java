package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A webhook address taken away.
 */
public final class DeletedWebhookEndpoint {

    private final Result result;
    private final String webhookEndpointToken;

    private DeletedWebhookEndpoint(JsonNode body) {
        this.result = Result.fromBody(body);
        this.webhookEndpointToken = Read.string(body.path("webhook_endpoint").path("token"));
    }

    public static DeletedWebhookEndpoint fromBody(JsonNode body) {
        return new DeletedWebhookEndpoint(body);
    }

    public Result getResult() {
        return result;
    }

    /** The address's token. */
    public String getWebhookEndpointToken() {
        return webhookEndpointToken;
    }

    @Override
    public String toString() {
        return "DeletedWebhookEndpoint[result=" + result + ", webhookEndpointToken=" + webhookEndpointToken + "]";
    }
}
