package com.odemehub.request;

import com.odemehub.enums.WebhookEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A change to a webhook address, named by its token in the address and again
 * in the body: the address itself, the events posted there, or whether
 * anything is, the rest staying as they are. Events sent replace every event
 * there was.
 */
public final class UpdateWebhookEndpoint extends Message {

    private final String token;
    private final String url;
    private final List<String> events;
    private final Boolean isActive;

    private UpdateWebhookEndpoint(Builder builder) {
        this.token = Fields.required(builder.token, "token");
        this.url = builder.url;
        this.events = builder.events == null ? null : List.copyOf(builder.events);
        this.isActive = builder.isActive;
    }

    /**
     * @param token The address's token in the gateway.
     */
    public static Builder builder(String token) {
        return new Builder(token);
    }

    @Override
    public String path() {
        return "update-webhook-endpoint/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "token", token,
            "webhook_endpoint", Fields.said(
                "url", url,
                "events", events,
                "is_active", isActive
            )
        );
    }

    public static final class Builder {

        private final String token;
        private String url;
        private List<String> events;
        private Boolean isActive;

        private Builder(String token) {
            this.token = token;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /** Every event to be posted there from now on, as the gateway writes them. */
        public Builder events(List<String> events) {
            this.events = events;
            return this;
        }

        public Builder events(String... events) {
            return events(Arrays.asList(events));
        }

        public Builder events(WebhookEvent... events) {
            return events(Arrays.stream(events).map(WebhookEvent::getValue).toList());
        }

        /** Switch the address off, or back on. */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public UpdateWebhookEndpoint build() {
            return new UpdateWebhookEndpoint(this);
        }
    }
}
