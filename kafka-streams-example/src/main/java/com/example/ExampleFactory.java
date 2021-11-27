package com.example;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import javax.inject.Named;
import javax.inject.Singleton;

@Slf4j
@Factory
public class ExampleFactory {

    private static final String STREAM_SCORE_EVENT_NAME = "score-events";

    /*@Singleton
    @Named("players-stream")
    KStream<String, String> exampleStream2(@Named("default") ConfiguredStreamBuilder builder) {
        builder.globalTable("score-events",
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(
                                "mv-score-events")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));
        return builder.stream("players");
    }*/

    @Singleton
    @Named(STREAM_SCORE_EVENT_NAME)
    KTable<String, String> exampleStream(@Named("default") ConfiguredStreamBuilder builder) {
        log.info("##############################################");
        log.info("Config: {}", builder.getConfiguration());
        return builder.table("score-events",
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(
                        "mv-score-events")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));
    }

}