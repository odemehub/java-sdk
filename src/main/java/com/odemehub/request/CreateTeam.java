package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A team a partner opens. Everything in it may be left out: a team left
 * unnamed is named the way any new team is, one left without a timezone
 * keeps Europe/Istanbul, one sent without modules starts with none. In the
 * sandbox a team starts with every module on, whatever is sent. A partner
 * with a limit on its teams is turned down once it reaches it.
 */
public final class CreateTeam extends Message {

    private final String name;
    private final String timezone;
    private final List<String> modules;

    private CreateTeam(Builder builder) {
        this.name = builder.name;
        this.timezone = builder.timezone;
        this.modules = builder.modules == null ? null : List.copyOf(builder.modules);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-team";
    }

    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> team = Fields.said(
            "name", name,
            "timezone", timezone,
            "modules", modules
        );

        return team.isEmpty() ? Fields.of() : Fields.of("team", team);
    }

    public static final class Builder {

        private String name;
        private String timezone;
        private List<String> modules;

        private Builder() {
        }

        /** What the team is called in the panel. */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /** An IANA timezone: "Europe/Istanbul". */
        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        /**
         * The modules the team starts with, as the gateway writes them:
         * {@code api}, {@code orders}, {@code subscription},
         * {@code payment_links}, {@code saved_cards},
         * {@code bank_transactions}.
         */
        public Builder modules(List<String> modules) {
            this.modules = modules;
            return this;
        }

        public Builder modules(String... modules) {
            return modules(Arrays.asList(modules));
        }

        public CreateTeam build() {
            return new CreateTeam(this);
        }
    }
}
