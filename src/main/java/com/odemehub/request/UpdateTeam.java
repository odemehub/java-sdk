package com.odemehub.request;

import java.util.Map;

/**
 * A change to the team itself: its name, its timezone, or both, the rest
 * staying as it is. The timezone is the one the team's days are counted in,
 * where a question names days. A change that sends neither is turned down.
 */
public final class UpdateTeam extends Message {

    private final String name;
    private final String timezone;

    private UpdateTeam(Builder builder) {
        this.name = builder.name;
        this.timezone = builder.timezone;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "update-team";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "team", Fields.said(
                "name", name,
                "timezone", timezone
            )
        );
    }

    public static final class Builder {

        private String name;
        private String timezone;

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

        public UpdateTeam build() {
            return new UpdateTeam(this);
        }
    }
}
