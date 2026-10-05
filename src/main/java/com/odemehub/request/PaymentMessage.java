package com.odemehub.request;

import java.util.Map;

/**
 * Something asked of a payment that has already been made. The payment is
 * named by the token the gateway gave it, and nothing else is sent: the
 * gateway holds the account, the provider and the reference
 * the provider knows the payment by.
 */
public abstract class PaymentMessage extends Message {

    private final String token;

    /**
     * @param token The payment's token in the gateway, as it answered when the payment was made.
     */
    protected PaymentMessage(String token) {
        this.token = Fields.required(token, "token");
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("transaction", Fields.of("token", token));
    }
}
