package com.smorales.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class Utils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String stringifyAsJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            return null;
        }
    }

}
