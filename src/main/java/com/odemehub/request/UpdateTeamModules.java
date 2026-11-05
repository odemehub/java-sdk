package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The modules a partner's team is left with, the team named by its number
 * in the address and again in the body. The modules sent are switched on
 * and every other module a partner may switch is switched off, so an empty
 * list switches them all off; the ones only ödemehub switches stay as they
 * are.
 */
public final class UpdateTeamModules extends Message {

    private final String slug;
    private final List<String> modules;

    /**
     * @param slug    The team's ten-digit number.
     * @param modules Every module the team is to have on, as the gateway
     *                writes them: {@code api}, {@code orders},
     *                {@code subscription}, {@code payment_links},
     *                {@code saved_cards}, {@code bank_transactions}.
     */
    public UpdateTeamModules(String slug, List<String> modules) {
        this.slug = Fields.required(slug, "slug");
        this.modules = List.copyOf(Fields.required(modules, "modules"));
    }

    public UpdateTeamModules(String slug, String... modules) {
        this(slug, Arrays.asList(modules));
    }

    @Override
    public String path() {
        return "update-team-modules/" + slug;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "slug", slug,
            "modules", modules
        );
    }
}
