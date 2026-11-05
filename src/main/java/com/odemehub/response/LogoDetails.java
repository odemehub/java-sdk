package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A logo kept for the team's checkout. It is shown nowhere until its address
 * is given to {@code updateCheckoutDesign} as the {@code logo}.
 */
public final class LogoDetails {

    private final Result result;
    private final String url;

    private LogoDetails(JsonNode body) {
        this.result = Result.fromBody(body);
        this.url = Read.string(body.path("logo").path("url"));
    }

    public static LogoDetails fromBody(JsonNode body) {
        return new LogoDetails(body);
    }

    public Result getResult() {
        return result;
    }

    /** The address the logo is kept at. */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "LogoDetails[result=" + result + ", url=" + url + "]";
    }
}
