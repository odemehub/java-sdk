package com.odemehub.request;

import java.util.Map;

/**
 * Where a customer is billed, or where the goods go. A payment and a kept
 * card need the whole of the eight person fields on the billing address; an
 * order or a subscription takes whatever is known and asks the payer for the
 * rest on the checkout page.
 *
 * <p>The three company fields are read on the billing address only, and
 * always together: the gateway turns down an address that names one of them
 * without the others.
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

    private Address(Builder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.phone = builder.phone;
        this.address = builder.address;
        this.district = builder.district;
        this.province = builder.province;
        this.country = builder.country;
        this.companyTitle = builder.companyTitle;
        this.taxNumber = builder.taxNumber;
        this.taxOffice = builder.taxOffice;
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        return Fields.said(
            "firstname", firstname,
            "lastname", lastname,
            "email", email,
            "phone", phone,
            "address", address,
            "district", district,
            "province", province,
            "country", country,
            "company_title", companyTitle,
            "tax_number", taxNumber,
            "tax_office", taxOffice
        );
    }

    public static final class Builder {

        private String firstname;
        private String lastname;
        private String email;
        private String phone;
        private String address;
        private String district;
        private String province;
        private String country;
        private String companyTitle;
        private String taxNumber;
        private String taxOffice;

        private Builder() {
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        /** The street address. */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder district(String district) {
            this.district = district;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /** The company the customer is billed as. Billing address only, together with the tax number and office. */
        public Builder companyTitle(String companyTitle) {
            this.companyTitle = companyTitle;
            return this;
        }

        public Builder taxNumber(String taxNumber) {
            this.taxNumber = taxNumber;
            return this;
        }

        public Builder taxOffice(String taxOffice) {
            this.taxOffice = taxOffice;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
