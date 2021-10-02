package com.smorales.controller;

import com.smorales.model.Product;
import com.smorales.service.ProductService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Controller("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Get
    public Flowable<Product> getAll() {
        return productService.getAll();
    }

    @Get("/{id}")
    public Maybe<Product> getById(@PathVariable String id) {
        log.info("Get product by id {}", id);
        return productService.getById(id);
    }

    @Post
    public Single<Product> save(@Body @NotNull @Valid Product product) {
        return productService
                .save(product)
                .doOnSuccess(p -> log.info("Product {} saved successfully", product.getName()));
    }

}
