package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Payment accounts asked after: the ones whose tokens are given, at most
 * fifty, or every account of the team's when none is. A token that is not
 * the team's finds nothing. The answer is always a list, and an empty one
 * when nothing matches; it is not cut into pages. Nothing is changed by
 * asking.
 */
public final class RetrievePaymentProviders extends Message {

    private final List<String> tokens;

    private RetrievePaymentProviders(List<String> tokens) {
        this.tokens = tokens;
    }

    /**
     * Every account of the team's.
     */
    public static RetrievePaymentProviders all() {
        return new RetrievePaymentProviders(List.of());
    }

    /**
     * The accounts with the tokens.
     */
    public static RetrievePaymentProviders byTokens(List<String> tokens) {
        return new RetrievePaymentProviders(List.copyOf(Fields.required(tokens, "tokens")));
    }

    public static RetrievePaymentProviders byTokens(String... tokens) {
        return byTokens(Arrays.asList(tokens));
    }

    @Override
    public String path() {
        return "retrieve-payment-providers";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said("tokens", tokens.isEmpty() ? null : tokens);
    }
}
