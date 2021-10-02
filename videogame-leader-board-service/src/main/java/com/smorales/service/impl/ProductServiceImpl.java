package com.smorales.service.impl;

import com.smorales.kafka.KafkaProducer;
import com.smorales.model.Product;
import com.smorales.repository.ProductRepository;
import com.smorales.service.ProductService;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaProducer kafkaProducer;

    public ProductServiceImpl(ProductRepository productRepository, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Flowable<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Maybe<Product> getById(String id) {
        return productRepository.findByKey(id);
    }

    @Override
    public Single<Product> save(Product product) {
        return kafkaProducer.sendProduct(String.valueOf(product.getId()), product);
    }
}
