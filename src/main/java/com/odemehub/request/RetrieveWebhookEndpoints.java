package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Webhook addresses asked after: the ones whose tokens are given, at most
 * fifty, or every address of the team's when none is. A token that is not
 * the team's finds nothing. The answer is always a list, and an empty one
 * when nothing matches; it is not cut into pages. Nothing is changed by
 * asking.
 */
public final class RetrieveWebhookEndpoints extends Message {

    private final List<String> tokens;

    private RetrieveWebhookEndpoints(List<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * Every address of the team's.
     */
    public static RetrieveWebhookEndpoints all() {
        return new RetrieveWebhookEndpoints(List.of());
    }

    /**
     * The addresses with the tokens.
     */
    public static RetrieveWebhookEndpoints byTokens(List<String> tokens) {
        return new RetrieveWebhookEndpoints(List.copyOf(Fields.required(tokens, "tokens")));
    }

    public static RetrieveWebhookEndpoints byTokens(String... tokens) {
        return byTokens(Arrays.asList(tokens));
    }

    @Override
    public String path() {
        return "retrieve-webhook-endpoints";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said("tokens", tokens.isEmpty() ? null : tokens);
    }
}
