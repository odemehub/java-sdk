package com.odemehub.request;

import java.util.Map;

/**
 * Money given back out of a payment the provider has already settled, whole
 * or in part.
 */
public final class RefundPayment extends PaymentMessage {

    private final String amount;

    /**
     * Give back everything the payment has left in it.
     */
    public RefundPayment(String token) {
        this(token, null);
    }

    /**
     * @param amount How much goes back, as digits with the kurus behind a
     *               point: "35.50". It is never more than the payment has
     *               left: the gateway turns down anything larger.
     */
    public RefundPayment(String token, String amount) {
        super(token);
        this.amount = amount;
    }

    @Override
    public String path() {
        return "refund-payment";
    }

    /**
     * The body. An amount that is not named is left out of the request
     * altogether rather than sent empty.
     */
    @Override
    public Map<String, Object> toBody() {
        Map<String, Object> body = super.toBody();

        if (amount != null) {
            body.put("amount", amount);
        }

        return body;
    }
}
