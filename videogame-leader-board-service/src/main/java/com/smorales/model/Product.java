package com.smorales.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Introspected
@Data
public class Product {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

}
