package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The movements on one or more bank accounts, named by their tokens, booked
 * between two days as {@code YYYY-MM-DD} in the team's own timezone, both
 * ends included and at most seven days apart; the last seven up to today
 * when none are given. The days are the bank's: the day it booked the
 * movement, not the day the gateway read it. The answer comes a page at a
 * time, oldest first, the movements of every account named woven together.
 * A token that is not the team's finds nothing. Nothing is changed by asking.
 */
public final class RetrieveBankTransactions extends Message {

    private final List<String> bankAccountTokens;
    private final String createdFrom;
    private final String createdTo;
    private final Integer page;
    private final Integer perPage;

    private RetrieveBankTransactions(Builder builder) {
        this.bankAccountTokens = List.copyOf(Fields.required(builder.bankAccountTokens, "bankAccountTokens"));
        this.createdFrom = builder.createdFrom;
        this.createdTo = builder.createdTo;
        this.page = builder.page;
        this.perPage = builder.perPage;
    }

    /**
     * @param bankAccountTokens The accounts' tokens in the gateway, one to fifty.
     */
    public static Builder builder(List<String> bankAccountTokens) {
        return new Builder(bankAccountTokens);
    }

    public static Builder builder(String... bankAccountTokens) {
        return builder(Arrays.asList(bankAccountTokens));
    }

    @Override
    public String path() {
        return "retrieve-bank-transactions";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said(
            "bank_account_tokens", bankAccountTokens,
            "created_from", createdFrom,
            "created_to", createdTo,
            "page", page,
            "per_page", perPage
        );
    }

    public static final class Builder {

        private final List<String> bankAccountTokens;
        private String createdFrom;
        private String createdTo;
        private Integer page;
        private Integer perPage;

        private Builder(List<String> bankAccountTokens) {
            this.bankAccountTokens = bankAccountTokens;
        }

        /** The movements the bank booked between two days, both included, as {@code YYYY-MM-DD}. Left out, the last seven days. */
        public Builder between(String createdFrom, String createdTo) {
            this.createdFrom = Fields.required(createdFrom, "createdFrom");
            this.createdTo = Fields.required(createdTo, "createdTo");
            return this;
        }

        /** The page, from 1; the first unless another is named. */
        public Builder page(int page) {
            this.page = page;
            return this;
        }

        /** How many movements a page holds, at most 250. Left out, 50 when a page is named and 250 when none is. */
        public Builder perPage(int perPage) {
            this.perPage = perPage;
            return this;
        }

        public RetrieveBankTransactions build() {
            return new RetrieveBankTransactions(this);
        }
    }
}
