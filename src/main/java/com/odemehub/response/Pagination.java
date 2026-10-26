package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Where a page stands among the pages a list answer is cut into.
 */
public final class Pagination {

    private final int page;
    private final int perPage;
    private final int total;
    private final int lastPage;

    private Pagination(JsonNode pagination) {
        this.page = Read.integer(pagination.path("page"));
        this.perPage = Read.integer(pagination.path("per_page"));
        this.total = Read.integer(pagination.path("total"));
        this.lastPage = Read.integer(pagination.path("last_page"));
    }

    public static Pagination fromBody(JsonNode pagination) {
        return new Pagination(pagination);
    }

    /**
     * Where the page stands, or null for an answer that carries none; the
     * gateway answers every question a page at a time, so it always should.
     */
    static Pagination in(JsonNode pagination) {
        return pagination.isObject() ? new Pagination(pagination) : null;
    }

    /** This page, from 1. */
    public int getPage() {
        return page;
    }

    /** How many records a page holds. */
    public int getPerPage() {
        return perPage;
    }

    /** How many records there are on every page together. */
    public int getTotal() {
        return total;
    }

    /** The last page; 1 when there is nothing. */
    public int getLastPage() {
        return lastPage;
    }

    /** Whether another page follows this one. */
    public boolean hasMorePages() {
        return page < lastPage;
    }

    @Override
    public String toString() {
        return "Pagination[page=" + page + ", perPage=" + perPage + ", total=" + total + ", lastPage=" + lastPage + "]";
    }
}
