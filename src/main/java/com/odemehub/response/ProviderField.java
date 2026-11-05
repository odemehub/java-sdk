package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One setting a provider asks for before a payment account may be added for
 * it. It is sent twice in {@code settings}: the live one under its key, the
 * test one under the key with a {@code test_} prefix.
 */
public final class ProviderField {

    private final String key;
    private final boolean required;
    private final String type;
    private final String label;
    private final Map<String, String> options;

    private ProviderField(JsonNode field) {
        this.key = Read.string(field.path("key"));
        this.required = Read.bool(field.path("required"));
        this.type = Read.string(field.path("type"));
        this.label = Read.string(field.path("label"));

        Map<String, String> options = new LinkedHashMap<>();
        JsonNode given = field.path("options");

        if (given.isObject()) {
            given.properties().forEach(option -> options.put(option.getKey(), Read.string(option.getValue())));
        }

        this.options = Collections.unmodifiableMap(options);
    }

    public static ProviderField fromBody(JsonNode field) {
        return new ProviderField(field);
    }

    /** The key the setting is sent under in {@code settings}: "api_key". */
    public String getKey() {
        return key;
    }

    /** Whether the setting must be given for the environment the team is in. */
    public boolean isRequired() {
        return required;
    }

    /** {@code text} for a value typed in, {@code select} for one of {@link #getOptions()}. */
    public String getType() {
        return type;
    }

    /** What the provider calls it: "API Anahtarı". */
    public String getLabel() {
        return label;
    }

    /** For a {@code select}, the values it takes, each with its label, in order; empty otherwise. */
    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "ProviderField[key=" + key + ", required=" + required + ", type=" + type + ", label=" + label
            + ", options=" + options + "]";
    }
}
