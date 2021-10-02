package com.smorales.factory;

import com.smorales.model.HighScores;
import com.smorales.model.Player;
import com.smorales.model.Product;
import com.smorales.model.ScoreEvent;
import com.smorales.model.join.Enriched;
import com.smorales.model.join.ScoreWithPlayer;
import com.smorales.serde.JsonSerdes;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.state.KeyValueStore;

import javax.inject.Named;
import javax.inject.Singleton;

import static com.smorales.definitions.KafkaTopics.PLAYERS_TOPIC;
import static com.smorales.definitions.KafkaTopics.PRODUCTS_TOPIC;
import static com.smorales.definitions.KafkaTopics.SCORE_EVENTS;
import static com.smorales.definitions.MaterializedViews.LEADER_BOARDS_VIEW;
import static com.smorales.definitions.MaterializedViews.PLAYER_VIEW;
import static com.smorales.definitions.MaterializedViews.PRODUCTS_VIEW;
import static com.smorales.definitions.MaterializedViews.SCORE_WITH_PLAYER_VIEW;

@Factory
public class KafkaStreamsFactory {

    @Singleton
    @Named("scoreWithPlayerStream")
    KStream<String, ScoreWithPlayer> scoreWithPlayerStream(ConfiguredStreamBuilder builder) {
        KStream<String, ScoreEvent> scoreEvents =
                builder.stream(SCORE_EVENTS, Consumed.with(Serdes.Void(), JsonSerdes.scoreEvent()))
                        // now marked for re-partitioning
                        .selectKey((k, v) -> v.getPlayerId().toString());

        // create the sharded players table
        KTable<String, Player> players =
                builder.table(PLAYERS_TOPIC,
                        Materialized.<String, Player, KeyValueStore<Bytes, byte[]>>as(PLAYER_VIEW)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.player())
                );

        // join params for scoreEvents -> players join
        Joined<String, ScoreEvent, Player> playerJoinParams =
                Joined.with(Serdes.String(), JsonSerdes.scoreEvent(), JsonSerdes.player());

        // join scoreEvents -> players
        ValueJoiner<ScoreEvent, Player, ScoreWithPlayer> scorePlayerJoiner =
                (score, player) -> new ScoreWithPlayer(score, player);

        KStream<String, ScoreWithPlayer> scoreWithPlayer =
                // Records on both sides must be keyed by the same field
                // For this example is playerId the key
                scoreEvents.join(players, scorePlayerJoiner, playerJoinParams);

        scoreWithPlayer.toTable(
                Materialized.<String, ScoreWithPlayer, KeyValueStore<Bytes, byte[]>>as(SCORE_WITH_PLAYER_VIEW)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(JsonSerdes.scoreWithPlayer())
        );

        return scoreWithPlayer;
    }

    @Singleton
    @Named("highScoresStream")
    KStream<String, HighScores> highScoresStream(
            ConfiguredStreamBuilder builder,
            @Named("scoreWithPlayerStream") KStream<String, ScoreWithPlayer> withPlayers) {

        // create the global product table
        GlobalKTable<String, Product> products =
                builder.globalTable(PRODUCTS_TOPIC,
                        Materialized.<String, Product, KeyValueStore<Bytes, byte[]>>as(PRODUCTS_VIEW)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.product()));

        /**
         * Specify how to map a KStream record to a Global KTable record. Map score-with-player records to products
         *
         * Regarding the KeyValueMapper param types:
         * - String is the key type for the score events stream
         * - ScoreWithPlayer is the value type for the score events stream
         * - String is the lookup key type
         *
         * In SQL syntax is as follows:
         * <pre>
         *     SELECT *
         *     FROM score_with_player s, products p
         *     JOIN products p ON s.product_id = p.id
         * </pre>
         *
         */
        KeyValueMapper<String, ScoreWithPlayer, String> keySelector =
                (leftKey, scoreWithPlayer) -> String.valueOf(scoreWithPlayer.getScoreEvent().getProductId());

        // join the withPlayers stream to the product GlobalKTable
        ValueJoiner<ScoreWithPlayer, Product, Enriched> productJoiner =
                (scoreWithPlayer, product) -> new Enriched(scoreWithPlayer, product);
        KStream<String, Enriched> withProducts = withPlayers.join(products, keySelector, productJoiner);

        // Group the enriched product stream
        KGroupedStream<String, Enriched> grouped =
                withProducts.groupBy(
                        (key, value) -> value.getProductId().toString(),
                        Grouped.with(Serdes.String(), JsonSerdes.enriched()));

        // Perform the aggregation, and materialize the underlying state store for querying
        KTable<String, HighScores> highScores =
                grouped.aggregate(
                        HighScores::new,
                        (key, value, aggregate) -> aggregate.add(value),
                        Materialized.<String, HighScores, KeyValueStore<Bytes, byte[]>>as(LEADER_BOARDS_VIEW)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.highScores()));

        return highScores.toStream();
    }
}