package com.odemehub.request;

import java.util.Base64;
import java.util.Map;

/**
 * A logo to be kept for the team's checkout: the picture itself, in base64
 * inside the body, so it is signed like every other body. A JPG, PNG or WEBP
 * of at most 2 MB and 3000 pixels a side. Kept, it is shown nowhere until
 * the address it comes back with is given to {@code updateCheckoutDesign} as
 * its {@code logo}.
 */
public final class CreateLogo extends Message {

    private final String content;

    /**
     * @param content The picture in base64, bare or as a data URI
     *                ({@code data:image/png;base64,...}).
     */
    public CreateLogo(String content) {
        this.content = Fields.required(content, "content");
    }

    /**
     * The picture as it is read off a file, written out in base64.
     */
    public static CreateLogo fromBytes(byte[] picture) {
        return new CreateLogo(Base64.getEncoder().encodeToString(Fields.required(picture, "picture")));
    }

    @Override
    public String path() {
        return "create-logo";
    }

    @Override
    public Map<String, Object> toBody() {
        return Fields.of("logo", Fields.of("content", content));
    }
}
