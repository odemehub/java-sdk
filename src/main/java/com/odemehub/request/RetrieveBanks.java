package com.odemehub.request;

import java.util.Map;

/**
 * The banks whose accounts may be added for their movements to be read,
 * each with the credentials it asks for. Nothing is sent but the signature.
 */
public final class RetrieveBanks extends Message {

    @Override
    public String path() {
        return "retrieve-banks";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of();
    }
}
