package com.smorales.repository;

import com.smorales.dto.BoardScore;
import com.smorales.dto.LeaderBoard;
import com.smorales.model.HighScores;
import com.smorales.model.join.ScoreWithPlayer;
import com.smorales.util.Utils;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.smorales.definitions.MaterializedViews.LEADER_BOARDS_VIEW;
import static com.smorales.definitions.MaterializedViews.SCORE_WITH_PLAYER_VIEW;

@Singleton
public class ScoreRepository extends KafkaStoreRepository<String, HighScores> {

    public ScoreRepository(KafkaStreams kafkaStreams) {
        super(LEADER_BOARDS_VIEW, kafkaStreams);
    }

    public Flowable<ScoreWithPlayer> findAllWithPlayer() {
        ReadOnlyKeyValueStore<String, ScoreWithPlayer> localStore = getStore(SCORE_WITH_PLAYER_VIEW);
        return toFlowable(localStore.all());
    }

    public Single<String> table() {
        ReadOnlyKeyValueStore<String, ScoreWithPlayer> localStore = getStore(SCORE_WITH_PLAYER_VIEW);
        StringBuilder table = new StringBuilder();

        var iterator = localStore.all();
        while (iterator.hasNext()) {
            var value = iterator.next();
            table.append(value.key);
            table.append("|");
            table.append(Utils.stringifyAsJson(value.value));
            table.append("\n");
        }
        return Single.just(table.toString());
    }

    public Single<LeaderBoard> getLeaderBoard() {
        return Single.just(
                allAsList()
                        .stream()
                        .map(pair -> new BoardScore(pair.key, pair.value.getHighScores()))
                        .collect(Collectors.toList())
        ).map(LeaderBoard::new);
    }

    public Single<String> leaderBoardAsTable() {
        return Single.just(toTable(getStore(LEADER_BOARDS_VIEW).all()));
    }

}
