package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.server.exceptions.InternalServerException;
import io.reactivex.Flowable;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

@Controller("/api")
public class ScoreEventsController {

    //private final InteractiveQueryService interactiveQueryService;
    private final KafkaStreams kafkaStreams;

    public ScoreEventsController(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }


    @Get("/all")
    public Flowable<Object> all() {
        return Optional.of(getStore())
            .map(ReadOnlyKeyValueStore::all)
            .map(iter ->
                Flowable.generate(() -> iter, (iterator, emitter) -> {
                    if (iterator.hasNext()) {
                        var next = iterator.next();
                        emitter.onNext(new SimpleEntry<>(next.key, next.value));
                    } else {
                        emitter.onComplete();
                    }
                }, KeyValueIterator::close)
            )
            .orElseGet(() -> Flowable.error(new InternalServerException("Store does not exist")));
    }

    private ReadOnlyKeyValueStore<Object, Object> getStore() {
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(
                "mv-score-events", QueryableStoreTypes.keyValueStore()
        ));
    }

}
