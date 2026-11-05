package com.odemehub.request;

import java.util.Map;

/**
 * Removing a payment account, named by its token in the address and again in
 * the body.
 */
public final class DeletePaymentProvider extends Message {

    private final String token;

    /**
     * @param token The account's token in the gateway.
     */
    public DeletePaymentProvider(String token) {
        this.token = Fields.required(token, "token");
    }

    @Override
    public String path() {
        return "delete-payment-provider/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("token", token);
    }
}
