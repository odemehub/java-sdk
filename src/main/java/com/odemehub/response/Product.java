package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A product in the merchant's catalogue at the gateway.
 */
public final class Product {

    private final Result result;
    private final String channelToken;
    private final String channelReference;
    private final String name;
    private final String image;
    private final String type;
    private final String amount;
    private final String currency;
    private final String taxRate;
    private final String period;
    private final boolean isActive;

    private Product(JsonNode body) {
        JsonNode product = body.path("product");

        this.result = Result.fromBody(body);
        this.channelToken = Read.string(product.path("channel_token"));
        this.channelReference = Read.string(product.path("channel_reference"));
        this.name = Read.string(product.path("name"));
        this.image = Read.optionalString(product.path("image"));
        this.type = Read.string(product.path("type"));
        this.amount = Read.string(product.path("amount"));
        this.currency = Read.string(product.path("currency"));
        this.taxRate = Read.string(product.path("tax_rate"));
        this.period = Read.optionalString(product.path("period"));
        this.isActive = Read.bool(product.path("is_active"));
    }

    public static Product fromBody(JsonNode body) {
        return new Product(body);
    }

    public Result getResult() {
        return result;
    }

    /** The channel the product is sold on. */
    public String getChannelToken() {
        return channelToken;
    }

    /** The key the product is known by in the calling system. */
    public String getChannelReference() {
        return channelReference;
    }

    public String getName() {
        return name;
    }

    /** The address of the picture the checkout shows it with; null when it has none. */
    public String getImage() {
        return image;
    }

    /** simple or recurring. */
    public String getType() {
        return type;
    }

    /** The price of one, as digits with the kurus behind a point. */
    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    /** The tax included in the price, as a percentage. */
    public String getTaxRate() {
        return taxRate;
    }

    /** monthly or annually for a recurring product; null for a simple one. */
    public String getPeriod() {
        return period;
    }

    /** Whether it is on sale. */
    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Product[result=" + result + ", channelReference=" + channelReference + ", name=" + name + ", image=" + image + ", type=" + type
            + ", amount=" + amount + ", currency=" + currency + ", period=" + period + ", isActive=" + isActive + "]";
    }
}
