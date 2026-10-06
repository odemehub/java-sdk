package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The answer to opening or changing a payment link: the link as it now
 * stands, and nothing else. Its latest attempts are on the link when it is
 * asked after with {@code retrievePaymentLinks()}; what each payer paid is
 * asked after with {@code retrieveLinkPayments()}.
 */
public final class PaymentLinkDetails {

    private final Result result;
    private final PaymentLink paymentLink;

    private PaymentLinkDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.paymentLink = PaymentLink.fromBody(body.path("payment_link"));
    }

    public static PaymentLinkDetails fromBody(JsonNode body) {
        return new PaymentLinkDetails(body);
    }

    public Result getResult() {
        return result;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    @Override
    public String toString() {
        return "PaymentLinkDetails[result=" + result + ", paymentLink=" + paymentLink + "]";
    }
}
