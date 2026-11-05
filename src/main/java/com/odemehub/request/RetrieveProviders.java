package com.odemehub.request;

import java.util.Map;

/**
 * The providers a payment account may be added for, each with the settings
 * it asks for. Nothing is sent but the signature.
 */
public final class RetrieveProviders extends Message {

    @Override
    public String path() {
        return "retrieve-providers";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of();
    }
}
