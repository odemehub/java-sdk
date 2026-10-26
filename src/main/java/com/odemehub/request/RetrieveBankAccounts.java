package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Bank accounts asked after: the ones whose tokens are given, at most fifty,
 * or every account of the team's when none is. A token that is not the
 * team's finds nothing. The answer is always a list, oldest first, and an
 * empty one when nothing matches. Nothing is changed by asking.
 */
public final class RetrieveBankAccounts extends Message {

    private final List<String> tokens;

    private RetrieveBankAccounts(List<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * Every account of the team's.
     */
    public static RetrieveBankAccounts all() {
        return new RetrieveBankAccounts(List.of());
    }

    /**
     * The accounts with the tokens.
     */
    public static RetrieveBankAccounts byTokens(List<String> tokens) {
        return new RetrieveBankAccounts(List.copyOf(Fields.required(tokens, "tokens")));
    }

    public static RetrieveBankAccounts byTokens(String... tokens) {
        return byTokens(Arrays.asList(tokens));
    }

    @Override
    public String path() {
        return "retrieve-bank-accounts";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said("tokens", tokens.isEmpty() ? null : tokens);
    }
}
