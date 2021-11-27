package com.example;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.configuration.kafka.streams.KafkaStreamsFactory;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.event.ApplicationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KTable;

@Slf4j
@Factory
public class CustomKafkaStreamsFactory extends KafkaStreamsFactory {

    public CustomKafkaStreamsFactory(ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
    }


    @EachBean(ConfiguredStreamBuilder.class)
    @Context
    KafkaStreams kafkaStreams(
            @Parameter String name,
            ConfiguredStreamBuilder builder,
            KTable<?, ?>... globalKTables
    ) {
        log.info("##############################################");
        log.info("Creating global ktable");
        Topology topology = builder.build(builder.getConfiguration());
        KafkaStreams kafkaStreams = new KafkaStreams(
                topology,
                builder.getConfiguration()
        );
        //streams.put(kafkaStreams, builder);
        kafkaStreams.start();
        return kafkaStreams;
    }

}
