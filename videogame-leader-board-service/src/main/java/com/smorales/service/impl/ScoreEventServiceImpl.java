package com.smorales.service.impl;

import com.smorales.dto.LeaderBoard;
import com.smorales.kafka.KafkaProducer;
import com.smorales.model.ScoreEvent;
import com.smorales.model.join.ScoreWithPlayer;
import com.smorales.repository.ScoreRepository;
import com.smorales.service.ScoreEventService;
import io.reactivex.Flowable;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
public class ScoreEventServiceImpl implements ScoreEventService {

    private final KafkaProducer kafkaProducer;
    private final ScoreRepository scoreRepository;

    public ScoreEventServiceImpl(KafkaProducer kafkaProducer, ScoreRepository scoreRepository) {
        this.kafkaProducer = kafkaProducer;
        this.scoreRepository = scoreRepository;
    }

    @Override
    public Single<ScoreEvent> save(ScoreEvent scoreEvent) {
        return kafkaProducer.sendScoreEvent(scoreEvent);
    }

    @Override
    public Flowable<ScoreWithPlayer> findAllWithPlayer() {
        return scoreRepository.findAllWithPlayer();
    }

    @Override
    public Single<LeaderBoard> leaderboards() {
        return scoreRepository.getLeaderBoard();
    }

    @Override
    public Single<String> allWithPlayerTable() {
        return scoreRepository.table();
    }

    @Override
    public Single<String> leaderboardsAsTable() {
        return scoreRepository.leaderBoardAsTable();
    }

}
