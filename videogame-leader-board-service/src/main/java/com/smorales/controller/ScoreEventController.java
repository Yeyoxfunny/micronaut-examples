package com.smorales.controller;

import com.smorales.dto.LeaderBoard;
import com.smorales.model.ScoreEvent;
import com.smorales.model.join.ScoreWithPlayer;
import com.smorales.service.ScoreEventService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@Slf4j
@Controller("/scores")
public class ScoreEventController {

    private final ScoreEventService scoreEventService;

    public ScoreEventController(ScoreEventService scoreEventService) {
        this.scoreEventService = scoreEventService;
    }

    @Get
    public Flowable<ScoreWithPlayer> getAll() {
        return scoreEventService.findAllWithPlayer();
    }

    @Get(value = "/table", processes = MediaType.TEXT_PLAIN)
    public Single<String> getTable() {
        return scoreEventService.allWithPlayerTable();
    }

    @Post
    public Single<ScoreEvent> save(@Body @Valid ScoreEvent scoreEvent) {
        return scoreEventService.save(scoreEvent)
                .doOnSuccess(e -> log.info("Score event {} saved successfully", scoreEvent));
    }

    @Get("/leaderboards")
    public Single<LeaderBoard> leaderboards() {
        return scoreEventService.leaderboards();
    }

    @Get(value = "/leaderboards/table", produces = MediaType.TEXT_PLAIN)
    public Single<String> leaderboardsAsTable() {
        return scoreEventService.leaderboardsAsTable();
    }

}
