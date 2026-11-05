package com.odemehub.request;

import java.util.Map;

/**
 * How the team's checkout pages are dressed. Nothing is sent but the
 * signature.
 */
public final class RetrieveCheckoutDesign extends Message {

    @Override
    public String path() {
        return "retrieve-checkout-design";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of();
    }
}
