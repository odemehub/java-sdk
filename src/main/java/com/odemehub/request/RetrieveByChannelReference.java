package com.odemehub.request;

import java.util.Map;

/**
 * Every record of a kind made on a channel within a span of days, oldest
 * first. The days are given as {@code YYYY-MM-DD} in the team's own
 * timezone, both ends included, and the span may be at most seven days; the
 * two are given together or not at all, and left out they mean the last
 * seven days up to today. Nothing is changed by asking.
 */
public abstract class RetrieveByChannelReference extends ChannelMessage {

    private final String createdFrom;
    private final String createdTo;

    /**
     * @param createdFrom  The first day, as {@code YYYY-MM-DD}, or null for the last seven days.
     * @param createdTo    The last day, as {@code YYYY-MM-DD}, at most six days after the first.
     * @param channelToken The channel to look on, or null for the client's own.
     */
    protected RetrieveByChannelReference(String createdFrom, String createdTo, String channelToken) {
        super(channelToken);
        this.createdFrom = createdFrom;
        this.createdTo = createdTo;
    }

    @Override
    public Map<String, Object> toBody(String channelToken) {
        return Fields.said(
            "channel_token", channel(channelToken),
            "created_from", createdFrom,
            "created_to", createdTo
        );
    }

    protected String createdFrom() {
        return createdFrom;
    }

    protected String createdTo() {
        return createdTo;
    }
}
