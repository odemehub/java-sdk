package com.odemehub.request;

/**
 * A message that speaks for one of the team's channels: a payment, an order
 * opened for checkout, a card kept for a customer. The channel belongs to
 * the integration rather than to any one message, so it is named once on
 * the client; a merchant selling on more than one channel names another
 * here, on the single message that belongs elsewhere.
 */
public abstract class ChannelMessage extends Message {

    /**
     * Stands for the team's own ödemehub channel, which has no token of its
     * own and is only ever reached by payment links: the panel opens its
     * links there. Give it as the channel of a payment link message to reach
     * those links.
     */
    public static final String ODEMEHUB_CHANNEL = "odemehub";

    private final String channelToken;

    protected ChannelMessage(String channelToken) {
        this.channelToken = channelToken;
    }

    /**
     * The channel this message is for: the one it names, or the client's.
     */
    protected String channel(String channelToken) {
        return this.channelToken != null ? this.channelToken : channelToken;
    }

    /**
     * The channel this message names itself, or null when it leaves it to the
     * client. A change sends only this, so it never moves a record to the
     * client's channel by accident.
     */
    protected String namedChannel() {
        return channelToken;
    }

    /**
     * The channel a payment link message is for: the one it names, the
     * client's, or — for {@link #ODEMEHUB_CHANNEL} — none, which the gateway
     * reads as its own ödemehub channel.
     */
    protected String linkChannel(String channelToken) {
        return ODEMEHUB_CHANNEL.equals(this.channelToken) ? null : channel(channelToken);
    }

    public abstract static class Builder<B extends Builder<B>> {

        protected String channelToken;

        /** The channel this one message speaks for. Left out, the client's own is used. */
        public B channelToken(String channelToken) {
            this.channelToken = channelToken;
            return self();
        }

        protected abstract B self();
    }
}
