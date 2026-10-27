package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The most installments the payer is offered on the checkout page, said apart
 * for cards issued to people and to companies; 1 is a single payment only.
 */
public final class MaxInstallments {

    private final Integer individual;
    private final Integer commercial;

    private MaxInstallments(JsonNode limits) {
        this.individual = Read.optionalInteger(limits.path("individual"));
        this.commercial = Read.optionalInteger(limits.path("commercial"));
    }

    /**
     * The limits a thing carries; null for one that has none.
     */
    static MaxInstallments in(JsonNode limits) {
        return Read.object(limits) == null ? null : new MaxInstallments(limits);
    }

    /** The most installments on a card issued to a person; null for no limit. */
    public Integer getIndividual() {
        return individual;
    }

    /** The most installments on a card issued to a company; null for no limit. */
    public Integer getCommercial() {
        return commercial;
    }

    @Override
    public String toString() {
        return "MaxInstallments[individual=" + individual + ", commercial=" + commercial + "]";
    }
}
