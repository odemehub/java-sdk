package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.enums.CardScheme;
import com.odemehub.enums.CardType;
import java.util.List;

/**
 * What the gateway's provider knows about a card by the head of its number,
 * and how an amount may be paid off on it.
 */
public final class Bin {

    private final Result result;
    private final String bin;
    private final String issuerName;
    private final String issuerCode;
    private final CardScheme scheme;
    private final CardType type;
    private final String program;
    private final Boolean isCommercial;
    private final List<Installment> installments;

    private Bin(JsonNode body) {
        JsonNode card = body.path("card");

        this.result = Result.fromBody(body);
        this.bin = Read.string(card.path("bin"));
        this.issuerName = Read.optionalString(card.path("issuer_name"));
        this.issuerCode = Read.optionalString(card.path("issuer_code"));
        this.scheme = CardScheme.from(Read.optionalString(card.path("scheme")));
        this.type = CardType.from(Read.optionalString(card.path("type")));
        this.program = Read.optionalString(card.path("program"));
        this.isCommercial = Read.optionalBool(card.path("is_commercial"));
        this.installments = Read.list(body.path("installments"), Installment::fromBody);
    }

    public static Bin fromBody(JsonNode body) {
        return new Bin(body);
    }

    public Result getResult() {
        return result;
    }

    /** The digits the question was asked with. */
    public String getBin() {
        return bin;
    }

    /** The institution that issued the card. */
    public String getIssuerName() {
        return issuerName;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    /** The scheme the card is issued on, as the issuer reports it; null when it is not known. */
    public CardScheme getScheme() {
        return scheme;
    }

    /** Whether the money is lent, drawn from an account or loaded beforehand; null when it is not known. */
    public CardType getType() {
        return type;
    }

    /** The programme the card is sold under, such as Bonus or Maximum. */
    public String getProgram() {
        return program;
    }

    /** Whether the card belongs to a company rather than to a person; null when it is not known. */
    public Boolean isCommercial() {
        return isCommercial;
    }

    /** The ways the amount may be paid off, a single payment first. */
    public List<Installment> getInstallments() {
        return installments;
    }

    @Override
    public String toString() {
        return "Bin[result=" + result + ", bin=" + bin + ", issuerName=" + issuerName + ", scheme=" + scheme + ", type=" + type
            + ", program=" + program + ", isCommercial=" + isCommercial + ", installments=" + installments + "]";
    }
}
