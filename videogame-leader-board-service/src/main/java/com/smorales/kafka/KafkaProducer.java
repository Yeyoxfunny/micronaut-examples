package com.smorales.kafka;

import com.smorales.definitions.KafkaTopics;
import com.smorales.model.Player;
import com.smorales.model.Product;
import com.smorales.model.ScoreEvent;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.MessageBody;
import io.reactivex.Single;

@KafkaClient
public interface KafkaProducer {

    @Topic(KafkaTopics.PLAYERS_TOPIC)
    Single<Player> sendPlayer(@KafkaKey String key, @MessageBody Player player);

    @Topic(KafkaTopics.PRODUCTS_TOPIC)
    Single<Product> sendProduct(@KafkaKey String key, @MessageBody Product product);

    @Topic(KafkaTopics.SCORE_EVENTS)
    Single<ScoreEvent> sendScoreEvent(ScoreEvent scoreEvent);

}
