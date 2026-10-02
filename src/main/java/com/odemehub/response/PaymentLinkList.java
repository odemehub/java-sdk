package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Every payment link opened on a channel within a span of days, oldest
 * first.
 */
public final class PaymentLinkList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<PaymentLink> paymentLinks;

    private PaymentLinkList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.string(body.path("created_from"));
        this.createdTo = Read.string(body.path("created_to"));
        this.paymentLinks = Read.list(body.path("payment_links"), PaymentLink::fromBody);
    }

    public static PaymentLinkList fromBody(JsonNode body) {
        return new PaymentLinkList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day read, {@code YYYY-MM-DD} in the team's timezone. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day read. */
    public String getCreatedTo() {
        return createdTo;
    }

    public List<PaymentLink> getPaymentLinks() {
        return paymentLinks;
    }

    @Override
    public String toString() {
        return "PaymentLinkList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", paymentLinks=" + paymentLinks + "]";
    }
}
