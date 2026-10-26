package com.odemehub.request;

import java.util.Map;

/**
 * Asking after records of one kind. They are named one of three ways: by the
 * token the gateway gave one, by the merchant's own reference for them, or by
 * the days they were made on, as {@code YYYY-MM-DD} in the team's own
 * timezone, both ends included and at most seven days apart. Asked with none
 * of these, it is the last seven days up to today. The answer is always a
 * list, oldest first, and an empty one when nothing matches. Nothing is
 * changed by asking.
 *
 * <p>The answer always comes a page at a time and says where it stands.
 * Asked as it is, it is the first page of up to 250 records; over a busy
 * week there may be more, so ask for the pages after it while there are.
 */
public abstract class Retrieve<R extends Retrieve<R>> extends Message {

    /**
     * How a kind of question is asked again the same way, with a page named.
     */
    @FunctionalInterface
    protected interface Maker<R> {
        R make(String token, String reference, String createdFrom, String createdTo, Integer page, Integer perPage);
    }

    private final String token;

    private final String reference;

    private final String createdFrom;

    private final String createdTo;

    private final Integer page;

    private final Integer perPage;

    private final Maker<R> maker;

    protected Retrieve(String token, String reference, String createdFrom, String createdTo, Integer page, Integer perPage, Maker<R> maker) {
        this.token = token;
        this.reference = reference;
        this.createdFrom = createdFrom;
        this.createdTo = createdTo;
        this.page = page;
        this.perPage = perPage;
        this.maker = maker;
    }

    /**
     * The same question asked for another page: this one, from 1, of
     * {@code perPage} records, 50 unless another size is named. The answer
     * says where it stands in {@code getPagination()}.
     */
    public final R page(int page) {
        return maker.make(token, reference, createdFrom, createdTo, page, perPage);
    }

    /**
     * The same question asked with this many records to a page, at most
     * 250; the first page unless another is named.
     */
    public final R perPage(int perPage) {
        return maker.make(token, reference, createdFrom, createdTo, page, perPage);
    }

    /**
     * The field the merchant's own reference travels in.
     */
    protected String referenceField() {
        return "reference";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said(
            "token", token,
            referenceField(), reference,
            "created_from", createdFrom,
            "created_to", createdTo,
            "page", page,
            "per_page", perPage
        );
    }
}
