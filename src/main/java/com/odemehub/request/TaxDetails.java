package com.odemehub.request;

import java.util.Map;

/**
 * Who a customer is billed as when they buy for a company. The three are
 * always given together: the gateway turns down a customer that names one
 * of them without the others.
 */
public final class TaxDetails {

    private final String companyTitle;
    private final String taxNumber;
    private final String taxOffice;

    public TaxDetails(String companyTitle, String taxNumber, String taxOffice) {
        this.companyTitle = Fields.required(companyTitle, "companyTitle");
        this.taxNumber = Fields.required(taxNumber, "taxNumber");
        this.taxOffice = Fields.required(taxOffice, "taxOffice");
    }

    Map<String, Object> toBody() {
        return Fields.of(
            "company_title", companyTitle,
            "tax_number", taxNumber,
            "tax_office", taxOffice
        );
    }
}
