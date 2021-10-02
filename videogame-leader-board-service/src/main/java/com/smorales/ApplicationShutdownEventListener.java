package com.smorales;

import io.micronaut.configuration.kafka.streams.event.BeforeKafkaStreamStart;
import io.micronaut.context.event.ShutdownEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class ApplicationShutdownEventListener {

    @EventListener
    public void onBeforeKafkaStreamStart(BeforeKafkaStreamStart event) {
        log.info("Before Kafka Streams Start");
        // event.getKafkaStreams().cleanUp();
    }

    @EventListener
    public void onShutdownEvent(ShutdownEvent event) {
        log.info("Shutting Down");
    }
}
