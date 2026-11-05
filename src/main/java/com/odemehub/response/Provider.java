package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A provider a payment account may be added for, with the settings it asks
 * for.
 */
public final class Provider {

    private final String id;
    private final String title;
    private final boolean isAlternative;
    private final List<ProviderField> fields;

    private Provider(JsonNode provider) {
        this.id = Read.string(provider.path("id"));
        this.title = Read.string(provider.path("title"));
        this.isAlternative = Read.bool(provider.path("is_alternative"));
        this.fields = Read.list(provider.path("fields"), ProviderField::fromBody);
    }

    public static Provider fromBody(JsonNode provider) {
        return new Provider(provider);
    }

    /** The provider's id, which an account is added under as its {@code provider}: "iyzico". */
    public String getId() {
        return id;
    }

    /** The provider's name: "iyzico". */
    public String getTitle() {
        return title;
    }

    /**
     * Whether it is offered beside the card on the checkout page (Garanti
     * Pay, Multinet...) rather than taking the card itself.
     */
    public boolean isAlternative() {
        return isAlternative;
    }

    /** The settings the provider asks for, in the order it lists them. */
    public List<ProviderField> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "Provider[id=" + id + ", title=" + title + ", isAlternative=" + isAlternative + ", fields=" + fields + "]";
    }
}
