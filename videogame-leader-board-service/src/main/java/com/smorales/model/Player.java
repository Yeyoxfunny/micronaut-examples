package com.smorales.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Introspected
@Data
@NoArgsConstructor
public class Player {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

}
