package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A change to how the team's checkout pages are dressed. Only what is given
 * is written; everything left out stays as it is, and a team that has never
 * been dressed starts from the defaults. The choices travel as the gateway
 * writes them, so a value this version does not list is sent all the same.
 *
 * <p>The logo and the terms address can be taken away: {@code logo(null)}
 * and {@code termsUrl(null)} send them empty, which is not the same as
 * leaving them out.
 */
public final class UpdateCheckoutDesign extends Message {

    private final Map<String, Object> checkoutDesign;

    private UpdateCheckoutDesign(Builder builder) {
        this.checkoutDesign = new LinkedHashMap<>(builder.checkoutDesign);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String path() {
        return "update-checkout-design";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("checkout_design", new LinkedHashMap<>(checkoutDesign));
    }

    public static final class Builder {

        private final Map<String, Object> checkoutDesign = new LinkedHashMap<>();

        private Builder() {
        }

        /**
         * The logo, by the address {@code createLogo} answered with; null
         * takes the logo away.
         */
        public Builder logo(String logo) {
            checkoutDesign.put("logo", logo);
            return this;
        }

        /** {@code standard} or {@code card}. */
        public Builder layout(String layout) {
            return given("layout", layout);
        }

        /** Whether the summary and the form swap sides. */
        public Builder isReversed(boolean isReversed) {
            return given("is_reversed", isReversed);
        }

        /** The summary side's colour, as {@code #rrggbb}. */
        public Builder summaryColor(String summaryColor) {
            return given("summary_color", summaryColor);
        }

        /** The buttons' and links' colour, as {@code #rrggbb}. */
        public Builder accentColor(String accentColor) {
            return given("accent_color", accentColor);
        }

        /** The typeface, as the gateway writes it: {@code default}, {@code inter}, {@code roboto}... */
        public Builder font(String font) {
            return given("font", font);
        }

        /** {@code rounded}, {@code rectangular} or {@code pill}. */
        public Builder formStyle(String formStyle) {
            return given("form_style", formStyle);
        }

        /** Whether the payer may enter a coupon code. */
        public Builder takesCoupons(boolean takesCoupons) {
            return given("takes_coupons", takesCoupons);
        }

        /** Whether the payer picks one of the team's shipping methods where shipping is asked for. */
        public Builder offersShippingMethods(boolean offersShippingMethods) {
            return given("offers_shipping_methods", offersShippingMethods);
        }

        /** What the pay button says: {@code auto}, {@code pay}, {@code book}, {@code donate} or {@code subscribe}. */
        public Builder submitType(String submitType) {
            return given("submit_type", submitType);
        }

        /** {@code list} or {@code tabs}. */
        public Builder paymentMethodStyle(String paymentMethodStyle) {
            return given("payment_method_style", paymentMethodStyle);
        }

        /** Whether the payer has to accept the terms before paying. */
        public Builder requiresTerms(boolean requiresTerms) {
            return given("requires_terms", requiresTerms);
        }

        /** The address of the terms the payer accepts; null takes it away. */
        public Builder termsUrl(String termsUrl) {
            checkoutDesign.put("terms_url", termsUrl);
            return this;
        }

        /** Whether the payer may pay as a company, with its tax details. */
        public Builder takesCompany(boolean takesCompany) {
            return given("takes_company", takesCompany);
        }

        public UpdateCheckoutDesign build() {
            return new UpdateCheckoutDesign(this);
        }

        /**
         * A field that cannot be taken away: given as null, it is left out
         * rather than sent empty.
         */
        private Builder given(String field, Object value) {
            if (value == null) {
                checkoutDesign.remove(field);
            } else {
                checkoutDesign.put(field, value);
            }

            return this;
        }
    }
}
