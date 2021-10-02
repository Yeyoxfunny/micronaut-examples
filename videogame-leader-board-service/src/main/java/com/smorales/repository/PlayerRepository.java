package com.smorales.repository;

import com.smorales.definitions.MaterializedViews;
import com.smorales.model.Player;
import org.apache.kafka.streams.KafkaStreams;

import javax.inject.Singleton;

@Singleton
public class PlayerRepository extends KafkaStoreRepository<String, Player> {

    public PlayerRepository(KafkaStreams kafkaStreams) {
        super(MaterializedViews.PLAYER_VIEW, kafkaStreams);
    }
}
