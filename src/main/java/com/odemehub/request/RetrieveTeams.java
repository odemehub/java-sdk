package com.odemehub.request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The teams a partner opened, asked after: the ones whose numbers are given,
 * or every one of them when none is. A number that is not one of the
 * partner's teams finds nothing. The answer is always a list, oldest first,
 * and an empty one when nothing matches. Nothing is changed by asking.
 *
 * <p>The answer comes a page at a time and says where it stands. Asked as it
 * is, it is the first page of up to 250 teams; ask for the pages after it
 * while there are.
 */
public final class RetrieveTeams extends Message {

    private final List<String> slugs;
    private final Integer page;
    private final Integer perPage;

    private RetrieveTeams(List<String> slugs, Integer page, Integer perPage) {
        this.slugs = slugs;
        this.page = page;
        this.perPage = perPage;
    }

    /**
     * Every team the partner opened.
     */
    public static RetrieveTeams all() {
        return new RetrieveTeams(List.of(), null, null);
    }

    /**
     * The teams with the numbers, at most 250.
     */
    public static RetrieveTeams bySlugs(List<String> slugs) {
        return new RetrieveTeams(List.copyOf(Fields.required(slugs, "slugs")), null, null);
    }

    public static RetrieveTeams bySlugs(String... slugs) {
        return bySlugs(Arrays.asList(slugs));
    }

    /**
     * The same question asked for another page: this one, from 1, of
     * {@code perPage} teams, 50 unless another size is named. The answer
     * says where it stands in {@code getPagination()}.
     */
    public RetrieveTeams page(int page) {
        return new RetrieveTeams(slugs, page, perPage);
    }

    /**
     * The same question asked with this many teams to a page, at most 250;
     * the first page unless another is named.
     */
    public RetrieveTeams perPage(int perPage) {
        return new RetrieveTeams(slugs, page, perPage);
    }

    @Override
    public String path() {
        return "retrieve-teams";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.said(
            "slugs", slugs.isEmpty() ? null : slugs,
            "page", page,
            "per_page", perPage
        );
    }
}
