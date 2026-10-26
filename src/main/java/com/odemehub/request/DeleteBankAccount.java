package com.odemehub.request;

import java.util.Map;

/**
 * Removing a bank account, named by its token in the address and again in
 * the body. Every movement read off it goes with it, and its credentials too
 * unless another account of the team's is read with them.
 */
public final class DeleteBankAccount extends Message {

    private final String token;

    /**
     * @param token The account's token in the gateway.
     */
    public DeleteBankAccount(String token) {
        this.token = Fields.required(token, "token");
    }

    @Override
    public String path() {
        return "delete-bank-account/" + token;
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("token", token);
    }
}
