package com.smorales.definitions;

public final class KafkaTopics {

    public static final String PLAYERS_TOPIC = "players";
    public static final String PRODUCTS_TOPIC = "products";
    public static final String SCORE_EVENTS = "score-events";

    private KafkaTopics() {
        throw new UnsupportedOperationException();
    }
}
