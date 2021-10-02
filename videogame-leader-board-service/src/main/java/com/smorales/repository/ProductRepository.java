package com.smorales.repository;

import com.smorales.definitions.MaterializedViews;
import com.smorales.model.Product;
import org.apache.kafka.streams.KafkaStreams;

import javax.inject.Singleton;

@Singleton
public class ProductRepository extends KafkaStoreRepository<String, Product> {

    public ProductRepository(KafkaStreams kafkaStreams) {
        super(MaterializedViews.PRODUCTS_VIEW, kafkaStreams);
    }
}
