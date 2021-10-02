package com.smorales.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Introspected
@Data
public class ScoreEvent {

    @NotNull
    private Long playerId;

    @NotNull
    private Long productId;

    @NotNull
    private Double score;

}
