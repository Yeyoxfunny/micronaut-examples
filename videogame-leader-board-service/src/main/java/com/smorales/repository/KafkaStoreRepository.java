package com.smorales.repository;

import com.smorales.util.Utils;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.util.ArrayList;
import java.util.List;

public class KafkaStoreRepository<K, V> implements ReadOnlyRepository<K, V> {

    protected final KafkaStreams streams;
    protected final ReadOnlyKeyValueStore<K, V> store;

    public KafkaStoreRepository(String storeName, KafkaStreams streams) {
        this.streams = streams;
        this.store = streams.store(StoreQueryParameters
                .fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
    }

    @Override
    public Maybe<V> findByKey(K key) {
        return Maybe.fromCallable(() -> store.get(key));
    }

    @Override
    public Flowable<V> findAll() {
        return toFlowable(store.all());
    }

    protected static <I, E> Flowable<E> toFlowable(KeyValueIterator<I, E> iteratorArg) {
        return Flowable.generate(() -> iteratorArg, (iterator, emitter) -> {
            if (iterator.hasNext()) {
                emitter.onNext(iterator.next().value);
            } else {
                emitter.onComplete();
            }
        }, KeyValueIterator::close);
    }

    protected <I, E> String toTable(KeyValueIterator<I, E> iterator) {
        StringBuilder table = new StringBuilder();

        while (iterator.hasNext()) {
            var value = iterator.next();
            table.append(value.key);
            table.append("|");
            table.append(Utils.stringifyAsJson(value.value));
            table.append("\n");
        }
        return table.toString();
    }

    protected List<KeyValue<K, V>> allAsList() {
        var all = store.all();

        List<KeyValue<K, V>> allValues = new ArrayList<>();
        while (all.hasNext()) {
            allValues.add(all.next());
        }
        return allValues;
    }

    protected <T, E> ReadOnlyKeyValueStore<T, E> getStore(String name) {
        return streams.store(
                StoreQueryParameters.fromNameAndType(
                        name,
                        QueryableStoreTypes.keyValueStore()));
    }

}
