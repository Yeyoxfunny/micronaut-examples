package com.smorales.service.impl;

import com.smorales.kafka.KafkaProducer;
import com.smorales.model.Player;
import com.smorales.repository.PlayerRepository;
import com.smorales.service.PlayerService;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final KafkaProducer kafkaProducer;

    public PlayerServiceImpl(PlayerRepository playerRepository, KafkaProducer kafkaProducer) {
        this.playerRepository = playerRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public Flowable<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Maybe<Player> getById(String id) {
        return playerRepository.findByKey(id);
    }

    @Override
    public Single<Player> save(Player player) {
        return kafkaProducer.sendPlayer(String.valueOf(player.getId()), player);
    }
}
