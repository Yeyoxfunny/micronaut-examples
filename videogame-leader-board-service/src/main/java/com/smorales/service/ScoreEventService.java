package com.smorales.service;

import com.smorales.dto.LeaderBoard;
import com.smorales.model.ScoreEvent;
import com.smorales.model.join.ScoreWithPlayer;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ScoreEventService {

    Single<ScoreEvent> save(ScoreEvent scoreEvent);

    Flowable<ScoreWithPlayer> findAllWithPlayer();

    Single<LeaderBoard> leaderboards();

    Single<String> allWithPlayerTable();

    Single<String> leaderboardsAsTable();
}
