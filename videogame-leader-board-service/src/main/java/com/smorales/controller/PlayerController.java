package com.smorales.controller;

import com.smorales.model.Player;
import com.smorales.service.PlayerService;
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
@Controller("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Get
    public Flowable<Player> getAll() {
        return playerService.getAll();
    }

    @Get("/{id}")
    public Maybe<Player> getById(@PathVariable String id) {
        return playerService.getById(id);
    }

    @Post
    public Single<Player> save(@Body @NotNull @Valid Player player) {
        return playerService
                .save(player)
                .doOnSuccess(p -> log.info("Player saved successfully"));
    }

}
