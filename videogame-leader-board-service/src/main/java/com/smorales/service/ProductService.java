package com.smorales.service;

import com.smorales.model.Product;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ProductService {

    Flowable<Product> getAll();

    Maybe<Product> getById(String id);

    Single<Product> save(Product player);

}
