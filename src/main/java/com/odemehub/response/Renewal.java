package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.Currency;

/**
 * The stretch of a subscription it is on now: what it costs, when it starts
 * and ends, and when it was paid, if it has been.
 */
public final class Renewal {

    private final String token;
    private final String amount;
    private final Currency currency;
    private final String startsAt;
    private final String endsAt;
    private final String paidAt;

    private Renewal(JsonNode renewal) {
        this.token = Read.string(renewal.path("token"));
        this.amount = Read.string(renewal.path("amount"));
        this.currency = Currency.from(Read.optionalString(renewal.path("currency")));
        this.startsAt = Read.nonEmptyString(renewal.path("starts_at"));
        this.endsAt = Read.nonEmptyString(renewal.path("ends_at"));
        this.paidAt = Read.nonEmptyString(renewal.path("paid_at"));
    }

    static Renewal in(JsonNode renewal) {
        return Read.object(renewal) == null ? null : new Renewal(renewal);
    }

    public String getToken() {
        return token;
    }

    /** What the stretch costs, with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    /** When it begins, ISO 8601 in UTC; null until it is paid for. */
    public String getStartsAt() {
        return startsAt;
    }

    /** When it runs out; null until it is paid for. */
    public String getEndsAt() {
        return endsAt;
    }

    /** When it was paid for; null while it is owed. */
    public String getPaidAt() {
        return paidAt;
    }

    @Override
    public String toString() {
        return "Renewal[token=" + token + ", amount=" + amount + ", currency=" + currency + ", startsAt=" + startsAt + ", endsAt=" + endsAt + ", paidAt=" + paidAt + "]";
    }
}
