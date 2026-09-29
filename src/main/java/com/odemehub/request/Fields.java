package com.odemehub.request;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * How request bodies are written: in the snake_case the gateway speaks, in
 * the order the fields are named.
 */
final class Fields {

    private Fields() {
    }

    /**
     * A body holding every field named, empty ones too.
     */
    static Map<String, Object> of(Object... pairs) {
        Map<String, Object> body = new LinkedHashMap<>();

        for (int index = 0; index < pairs.length; index += 2) {
            body.put((String) pairs[index], pairs[index + 1]);
        }

        return body;
    }

    /**
     * A body holding only what the caller said, so an optional field is left
     * out altogether rather than sent empty.
     */
    static Map<String, Object> said(Object... pairs) {
        Map<String, Object> body = of(pairs);
        body.values().removeIf(Objects::isNull);

        return body;
    }

    static <T> T required(T value, String name) {
        return Objects.requireNonNull(value, name + " zorunludur.");
    }
}
