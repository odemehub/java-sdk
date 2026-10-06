package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Payments at the team's links asked after. The answer is always a list,
 * oldest first, and an empty one when nothing matched. The days are the ones
 * the gateway used, when the records were asked for by the days they were
 * made on: the ones asked for, or the last seven when none were.
 */
public final class LinkPaymentList {

    private final Result result;
    private final String createdFrom;
    private final String createdTo;
    private final List<LinkPayment> linkPayments;

    private LinkPaymentList(JsonNode body) {
        this.result = Result.fromBody(body);
        this.createdFrom = Read.nonEmptyString(body.path("created_from"));
        this.createdTo = Read.nonEmptyString(body.path("created_to"));
        this.linkPayments = Read.list(body.path("link_payments"), LinkPayment::fromBody);
    }

    public static LinkPaymentList fromBody(JsonNode body) {
        return new LinkPaymentList(body);
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

    public List<LinkPayment> getLinkPayments() {
        return linkPayments;
    }

    /** The listed payments that have been paid. */
    public List<LinkPayment> paid() {
        return linkPayments.stream().filter(LinkPayment::isPaid).toList();
    }

    @Override
    public String toString() {
        return "LinkPaymentList[result=" + result + ", createdFrom=" + createdFrom + ", createdTo=" + createdTo + ", linkPayments=" + linkPayments + "]";
    }
}
