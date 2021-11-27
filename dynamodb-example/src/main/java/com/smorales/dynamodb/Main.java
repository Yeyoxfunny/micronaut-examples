package com.smorales.dynamodb;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        String event = "{\"paymentStatusCode\": \"SUCCESS\"}";
        event = Base64.getEncoder().encodeToString(event.getBytes(StandardCharsets.UTF_8));
        event = Base64.getEncoder().encodeToString(event.getBytes(StandardCharsets.UTF_8));
        System.out.println(event);
    }

}
