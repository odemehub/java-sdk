package com.odemehub.request;

/**
 * A message that speaks for one of the team's channels: a payment, an order
 * opened for checkout, a card kept for a customer. The channel belongs to
 * the integration rather than to any one message, so it is named once on
 * the client; a merchant selling on more than one channel names another
 * here, on the single message that belongs elsewhere.
 */
public abstract class ChannelMessage extends Message {

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
