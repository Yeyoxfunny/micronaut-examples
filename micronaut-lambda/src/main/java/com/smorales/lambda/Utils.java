package com.smorales.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String stringifyAsJson(Object value) {
        try {
            return new ObjectMapper()
                    .writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
