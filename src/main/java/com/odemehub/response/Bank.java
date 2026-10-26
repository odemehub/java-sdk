package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A bank whose accounts may be added for their movements to be read, with
 * the credentials it asks for.
 */
public final class Bank {

    private final String id;
    private final String title;
    private final List<BankField> fields;

    private Bank(JsonNode bank) {
        this.id = Read.string(bank.path("id"));
        this.title = Read.string(bank.path("title"));
        this.fields = Read.list(bank.path("fields"), BankField::fromBody);
    }

    public static Bank fromBody(JsonNode bank) {
        return new Bank(bank);
    }

    /** The bank's id, which an account is added under as its {@code provider}: "enpara". */
    public String getId() {
        return id;
    }

    /** The bank's name: "Enpara". */
    public String getTitle() {
        return title;
    }

    /** The credentials the bank asks for, in the order it lists them. */
    public List<BankField> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "Bank[id=" + id + ", title=" + title + ", fields=" + fields + "]";
    }
}
