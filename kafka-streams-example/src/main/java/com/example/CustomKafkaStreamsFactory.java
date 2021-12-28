package com.example;

import io.micronaut.configuration.kafka.streams.AbstractKafkaStreamsConfiguration;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.configuration.kafka.streams.InteractiveQueryService;
import io.micronaut.configuration.kafka.streams.KafkaStreamsFactory;
import io.micronaut.configuration.kafka.streams.event.AfterKafkaStreamsStart;
import io.micronaut.configuration.kafka.streams.event.BeforeKafkaStreamStart;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.Closeable;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Factory
@Replaces(factory = KafkaStreamsFactory.class)
public class CustomKafkaStreamsFactory implements Closeable {

    private final Map<KafkaStreams, ConfiguredStreamBuilder> streams = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Default constructor.
     *
     * @param eventPublisher The event publisher
     */
    public CustomKafkaStreamsFactory(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Exposes the {@link ConfiguredStreamBuilder} as a bean.
     *
     * @param configuration The configuration
     * @return The streams builder
     */
    @EachBean(AbstractKafkaStreamsConfiguration.class)
    ConfiguredStreamBuilder streamsBuilder(AbstractKafkaStreamsConfiguration<?, ?> configuration) {
        return new ConfiguredStreamBuilder(configuration.getConfig());
    }

    /**
     * Get configured stream and builder for the stream.
     *
     * @return Map of streams to builders
     */
    public Map<KafkaStreams, ConfiguredStreamBuilder> getStreams() {
        return streams;
    }

    /**
     * <p>Builds the default {@link KafkaStreams} bean from the configuration and the supplied {@link ConfiguredStreamBuilder}.</p>
     *
     * <p>Allows create KafKa Streams with with KStream, KTable and GlobalKTable beans</p>
     *
     * @param name    The configuration name
     * @param builder The builder
     * @param streams The KStream definitions
     * @return The {@link KafkaStreams} bean
     */
    @EachBean(ConfiguredStreamBuilder.class)
    @Context
    KafkaStreams kafkaStreams(
            @Parameter String name,
            ConfiguredStreamBuilder builder,
            Object... streams
    ) {
        Topology topology = builder.build(builder.getConfiguration());
        KafkaStreams kafkaStreams = new KafkaStreams(
                topology,
                builder.getConfiguration()
        );
        var kStreams = Arrays.stream(streams)
                .filter(KStream.class::isInstance)
                .toArray(KStream[]::new);

        eventPublisher.publishEvent(new BeforeKafkaStreamStart(kafkaStreams, kStreams));
        this.streams.put(kafkaStreams, builder);
        if (log.isDebugEnabled()) {
            log.debug("Initializing Application {} with topology:\n{}", name, topology.describe().toString());
        }
        kafkaStreams.start();
        eventPublisher.publishEvent(new AfterKafkaStreamsStart(kafkaStreams, kStreams));
        return kafkaStreams;
    }

    /**
     * Create the interactive query service bean.
     *
     * @return Rhe {@link InteractiveQueryService} bean
     */
    @Singleton
    InteractiveQueryService interactiveQueryService() throws Exception {
        Constructor<InteractiveQueryService> constructor =
                InteractiveQueryService.class.getDeclaredConstructor(Collection.class);
        constructor.setAccessible(true);
        InteractiveQueryService interactiveQueryService = constructor.newInstance(streams.keySet());
        constructor.setAccessible(false);
        return interactiveQueryService;
    }

    @Override
    @PreDestroy
    public void close() {
        for (KafkaStreams stream : streams.keySet()) {
            try {
                stream.close(Duration.ofSeconds(3));
            } catch (Exception e) {
                // ignore
            }
        }
    }

}
