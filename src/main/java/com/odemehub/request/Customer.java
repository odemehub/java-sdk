package com.odemehub.request;

import java.util.Map;

/**
 * The customer a payment is made for, an order is opened for or a card is
 * kept for. The merchant names them by its own key for them on the channel
 * they came in on: the same key twice is the same customer, and what is said
 * of them here becomes the latest the gateway knows.
 */
public final class Customer {

    private final String channelReference;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phone;
    private final String address;
    private final String district;
    private final String province;
    private final String country;
    private final TaxDetails tax;

    private Customer(Builder builder) {
        this.channelReference = Fields.required(builder.channelReference, "channelReference");
        this.firstname = Fields.required(builder.firstname, "firstname");
        this.lastname = Fields.required(builder.lastname, "lastname");
        this.email = Fields.required(builder.email, "email");
        this.phone = Fields.required(builder.phone, "phone");
        this.address = Fields.required(builder.address, "address");
        this.district = Fields.required(builder.district, "district");
        this.province = Fields.required(builder.province, "province");
        this.country = Fields.required(builder.country, "country");
        this.tax = builder.tax;
    }

    public static Builder builder() {
        return new Builder();
    }

    Map<String, Object> toBody() {
        Map<String, Object> body = Fields.of(
            "channel_reference", channelReference,
            "firstname", firstname,
            "lastname", lastname,
            "email", email,
            "phone", phone,
            "address", address,
            "district", district,
            "province", province,
            "country", country
        );

        if (tax != null) {
            body.put("tax", tax.toBody());
        }

        return body;
    }

    public static final class Builder {

        private String channelReference;
        private String firstname;
        private String lastname;
        private String email;
        private String phone;
        private String address;
        private String district;
        private String province;
        private String country;
        private TaxDetails tax;

        private Builder() {
        }

        /** The key the merchant keeps this customer under in its own system. */
        public Builder channelReference(String channelReference) {
            this.channelReference = channelReference;
            return this;
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

        /** The company they are billed as, for a customer buying for one. */
        public Builder tax(TaxDetails tax) {
            this.tax = tax;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
