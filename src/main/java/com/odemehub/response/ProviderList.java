package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * The providers a payment account may be added for, each with the settings
 * it asks for.
 */
public final class ProviderList {

    private final Result result;
    private final List<Provider> providers;

    private ProviderList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.providers = Read.list(body.path("providers"), Provider::fromBody);
    }

    public static ProviderList fromBody(JsonNode body) {
        return new ProviderList(body);
    }

    public Result getResult() {
        return result;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    @Override
    public String toString() {
        return "ProviderList[result=" + result + ", providers=" + providers + "]";
    }
}
