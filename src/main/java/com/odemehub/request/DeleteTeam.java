package com.odemehub.request;

import java.util.Map;

/**
 * Deleting a partner's team for good, with everything in it, the team named
 * by its number in the address and again in the body. Its credentials stop
 * working at once.
 */
public final class DeleteTeam extends Message {

    private final String slug;

    /**
     * @param slug The team's ten-digit number.
     */
    public DeleteTeam(String slug) {
        this.slug = Fields.required(slug, "slug");
    }

    @Override
    public String path() {
        return "delete-team/" + slug;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("slug", slug);
    }
}
