package com.odemehub.request;

import java.util.Map;

/**
 * The most installments the payer is offered on the checkout page, said apart
 * for cards issued to people ({@code individual}) and to companies
 * ({@code commercial}): each 1 to 12, where 1 is a single payment only. A
 * kind of card left without a number has no limit.
 *
 * <p>Sent on a change, it takes the place of the limits there were; left out,
 * they stay. One with neither number set lifts every limit.
 */
public final class MaxInstallments {

    private final Integer individual;
    private final Integer commercial;

    private MaxInstallments(Builder builder) {
        this.individual = builder.individual;
        this.commercial = builder.commercial;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Both kinds of card, a number or not: a limit left unset goes as nothing,
     * which is how a change lifts it.
     */
    Map<String, Object> toBody() {
        return Fields.of(
            "individual", individual,
            "commercial", commercial
        );
    }

    @Override
    public String toString() {
        return "MaxInstallments[individual=" + individual + ", commercial=" + commercial + "]";
    }

    public static final class Builder {

        private Integer individual;
        private Integer commercial;

        private Builder() {
        }

        /** The most installments on a card issued to a person, 1 to 12; null for no limit. */
        public Builder individual(Integer individual) {
            this.individual = individual;
            return this;
        }

        /** The most installments on a card issued to a company, 1 to 12; null for no limit. */
        public Builder commercial(Integer commercial) {
            this.commercial = commercial;
            return this;
        }

        public MaxInstallments build() {
            return new MaxInstallments(this);
        }
    }
}
