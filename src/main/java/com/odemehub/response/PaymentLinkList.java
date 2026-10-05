package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Payment links asked after, each with how many payments were made on it and
 * the latest fifty of them. The answer is always a list, oldest first, and an empty one when
 * nothing matched. The days are the ones the gateway used, when the records
 * were asked for by the days they were made on: the ones asked for, or the
 * last seven when none were.
 */
public final class PaymentLinkList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<PaymentLink> paymentLinks;

    private PaymentLinkList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.paymentLinks = Read.list(body.path("payment_links"), PaymentLink::fromBody);
    }

    public static PaymentLinkList fromBody(JsonNode body) {
        return new PaymentLinkList(body);
    }

    public Result getResult() {
        return result;
    }

    /** The first day listed, {@code YYYY-MM-DD} in the team's timezone; null when they were asked for by token or reference. */
    public String getCreatedFrom() {
        return createdFrom;
    }

    /** The last day listed, the same way. */
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
