package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Where a customer is billed, or where the goods go, as it was written: the
 * fields given, and none of the ones still missing, which read as null. The
 * company fields are only ever on a billing address.
 */
public final class Address {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phone;
    private final String address;
    private final String district;
    private final String province;
    private final String country;
    private final String companyTitle;
    private final String taxNumber;
    private final String taxOffice;

    private Address(JsonNode address) {
        this.firstname = Read.optionalString(address.path("firstname"));
        this.lastname = Read.optionalString(address.path("lastname"));
        this.email = Read.optionalString(address.path("email"));
        this.phone = Read.optionalString(address.path("phone"));
        this.address = Read.optionalString(address.path("address"));
        this.district = Read.optionalString(address.path("district"));
        this.province = Read.optionalString(address.path("province"));
        this.country = Read.optionalString(address.path("country"));
        this.companyTitle = Read.optionalString(address.path("company_title"));
        this.taxNumber = Read.optionalString(address.path("tax_number"));
        this.taxOffice = Read.optionalString(address.path("tax_office"));
    }

    public static Address fromBody(JsonNode address) {
        return new Address(address);
    }

    /**
     * The address an answer carries under a key, or null when it carries none.
     */
    static Address in(JsonNode address) {
        return Read.object(address) == null ? null : new Address(address);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    @Override
    public String toString() {
        return "Address[firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", province=" + province + ", country=" + country + ", companyTitle=" + companyTitle + "]";
    }
}
