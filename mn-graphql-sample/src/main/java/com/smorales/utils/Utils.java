package com.smorales.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String stringifyAsJson(Object value) {
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }

}
