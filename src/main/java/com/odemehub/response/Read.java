package com.odemehub.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * How answers are read. A field the gateway left out reads as an empty
 * string, or as null where the answer may genuinely not carry it.
 */
final class Read {

    private Read() {
    }

    static boolean said(JsonNode value) {
        return value != null && !value.isNull() && !value.isMissingNode();
    }

    static String string(JsonNode value) {
        return said(value) ? text(value) : "";
    }

    static String optionalString(JsonNode value) {
        return said(value) ? text(value) : null;
    }

    /**
     * A field the gateway left empty reads as nothing rather than as an empty
     * string, so there is one way of asking whether it was said.
     */
    static String nonEmptyString(JsonNode value) {
        return value != null && value.isTextual() && !value.textValue().isEmpty() ? value.textValue() : null;
    }

    static boolean bool(JsonNode value) {
        if (!said(value)) {
            return false;
        }

        if (value.isBoolean()) {
            return value.booleanValue();
        }

        if (value.isNumber()) {
            return value.doubleValue() != 0;
        }

        if (value.isTextual()) {
            return !value.textValue().isEmpty() && !value.textValue().equals("0");
        }

        return value.size() > 0;
    }

    static Integer optionalInteger(JsonNode value) {
        return said(value) ? integer(value) : null;
    }

    /**
     * The object an answer carries under a key, or null when it carries none.
     */
    static JsonNode object(JsonNode value) {
        return value != null && value.isObject() ? value : null;
    }

    static Boolean optionalBool(JsonNode value) {
        return said(value) ? bool(value) : null;
    }

    static int integer(JsonNode value) {
        if (value != null && value.isNumber()) {
            return value.intValue();
        }

        try {
            return said(value) ? (int) Double.parseDouble(value.asText()) : 0;
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    static <T> List<T> list(JsonNode value, Function<JsonNode, T> read) {
        List<T> items = new ArrayList<>();

        if (value != null && value.isContainerNode()) {
            value.elements().forEachRemaining(item -> items.add(read.apply(item)));
        }

        return List.copyOf(items);
    }

    private static String text(JsonNode value) {
        if (value.isBoolean()) {
            return value.booleanValue() ? "1" : "";
        }

        return value.isValueNode() ? value.asText() : value.toString();
    }
}
