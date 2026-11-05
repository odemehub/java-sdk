package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * How the team's checkout pages are dressed. The choices read as the gateway
 * writes them, so one this version does not list still reads.
 */
public final class CheckoutDesign {

    private final String logo;
    private final String layout;
    private final boolean isReversed;
    private final String summaryColor;
    private final String accentColor;
    private final String font;
    private final String formStyle;
    private final boolean takesCoupons;
    private final boolean offersShippingMethods;
    private final String submitType;
    private final String paymentMethodStyle;
    private final boolean requiresTerms;
    private final String termsUrl;
    private final boolean takesCompany;

    private CheckoutDesign(JsonNode checkoutDesign) {
        this.logo = Read.nonEmptyString(checkoutDesign.path("logo"));
        this.layout = Read.string(checkoutDesign.path("layout"));
        this.isReversed = Read.bool(checkoutDesign.path("is_reversed"));
        this.summaryColor = Read.string(checkoutDesign.path("summary_color"));
        this.accentColor = Read.string(checkoutDesign.path("accent_color"));
        this.font = Read.string(checkoutDesign.path("font"));
        this.formStyle = Read.string(checkoutDesign.path("form_style"));
        this.takesCoupons = Read.bool(checkoutDesign.path("takes_coupons"));
        this.offersShippingMethods = Read.bool(checkoutDesign.path("offers_shipping_methods"));
        this.submitType = Read.string(checkoutDesign.path("submit_type"));
        this.paymentMethodStyle = Read.string(checkoutDesign.path("payment_method_style"));
        this.requiresTerms = Read.bool(checkoutDesign.path("requires_terms"));
        this.termsUrl = Read.nonEmptyString(checkoutDesign.path("terms_url"));
        this.takesCompany = Read.bool(checkoutDesign.path("takes_company"));
    }

    public static CheckoutDesign fromBody(JsonNode checkoutDesign) {
        return new CheckoutDesign(checkoutDesign);
    }

    /**
     * The design an answer carries under this key, or null when it carries none.
     */
    static CheckoutDesign in(JsonNode checkoutDesign) {
        return checkoutDesign.isObject() ? new CheckoutDesign(checkoutDesign) : null;
    }

    /** The logo's address; null when the page has none. */
    public String getLogo() {
        return logo;
    }

    /** {@code standard} or {@code card}. */
    public String getLayout() {
        return layout;
    }

    /** Whether the summary and the form swap sides. */
    public boolean isReversed() {
        return isReversed;
    }

    /** The summary side's colour, as {@code #rrggbb}. */
    public String getSummaryColor() {
        return summaryColor;
    }

    /** The buttons' and links' colour, as {@code #rrggbb}. */
    public String getAccentColor() {
        return accentColor;
    }

    /** The typeface: {@code default}, {@code inter}, {@code roboto}... */
    public String getFont() {
        return font;
    }

    /** {@code rounded}, {@code rectangular} or {@code pill}. */
    public String getFormStyle() {
        return formStyle;
    }

    /** Whether the payer may enter a coupon code. */
    public boolean takesCoupons() {
        return takesCoupons;
    }

    /** Whether the payer picks one of the team's shipping methods where shipping is asked for. */
    public boolean offersShippingMethods() {
        return offersShippingMethods;
    }

    /** What the pay button says: {@code auto}, {@code pay}, {@code book}, {@code donate} or {@code subscribe}. */
    public String getSubmitType() {
        return submitType;
    }

    /** {@code list} or {@code tabs}. */
    public String getPaymentMethodStyle() {
        return paymentMethodStyle;
    }

    /** Whether the payer has to accept the terms before paying. */
    public boolean requiresTerms() {
        return requiresTerms;
    }

    /** The address of the terms the payer accepts; null when there is none. */
    public String getTermsUrl() {
        return termsUrl;
    }

    /** Whether the payer may pay as a company, with its tax details. */
    public boolean takesCompany() {
        return takesCompany;
    }

    @Override
    public String toString() {
        return "CheckoutDesign[logo=" + logo + ", layout=" + layout + ", isReversed=" + isReversed
            + ", summaryColor=" + summaryColor + ", accentColor=" + accentColor + ", font=" + font
            + ", formStyle=" + formStyle + ", takesCoupons=" + takesCoupons
            + ", offersShippingMethods=" + offersShippingMethods + ", submitType=" + submitType
            + ", paymentMethodStyle=" + paymentMethodStyle + ", requiresTerms=" + requiresTerms
            + ", termsUrl=" + termsUrl + ", takesCompany=" + takesCompany + "]";
    }
}
