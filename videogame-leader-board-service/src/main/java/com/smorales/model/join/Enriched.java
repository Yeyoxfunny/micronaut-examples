package com.smorales.model.join;


import com.smorales.model.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Enriched implements Comparable<Enriched> {

    private Long playerId;
    private Long productId;
    private String playerName;
    private String gameName;
    private Double score;

    public Enriched(ScoreWithPlayer scoreEventWithPlayer, Product product) {
        this.playerId = scoreEventWithPlayer.getPlayer().getId();
        this.productId = product.getId();
        this.playerName = scoreEventWithPlayer.getPlayer().getName();
        this.gameName = product.getName();
        this.score = scoreEventWithPlayer.getScoreEvent().getScore();
    }

    @Override
    public int compareTo(Enriched o) {
        return Double.compare(o.score, score);
    }

}
