package com.smorales.repository;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface ReadOnlyRepository<K, V> {

    Maybe<V> findByKey(K key);

    Flowable<V> findAll();

}
