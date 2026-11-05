package com.odemehub.request;

import com.odemehub.enums.WebhookEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * An address the gateway is to post webhooks to, for the events named. The
 * same address is put down once per team; what is posted there is signed
 * with the team's own secret. An address is named by the token the gateway gives it
 * from then on.
 */
public final class CreateWebhookEndpoint extends Message {

    private final String url;
    private final List<String> events;
    private final Boolean isActive;

    private CreateWebhookEndpoint(Builder builder) {
        this.url = Fields.required(builder.url, "url");
        this.events = List.copyOf(Fields.required(builder.events, "events"));
        this.isActive = builder.isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "create-webhook-endpoint";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of(
            "webhook_endpoint", Fields.said(
                "url", url,
                "events", events,
                "is_active", isActive
            )
        );
    }

    public static final class Builder {

        private String url;
        private List<String> events;
        private Boolean isActive;

        private Builder() {
        }

        /** The address, {@code http} or {@code https}, reachable from the internet. */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /** The events posted there, as the gateway writes them: {@code order.paid}, {@code transaction.successful}... */
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

        /** Whether anything is posted there yet. Left out, it is. */
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CreateWebhookEndpoint build() {
            return new CreateWebhookEndpoint(this);
        }
    }
}
