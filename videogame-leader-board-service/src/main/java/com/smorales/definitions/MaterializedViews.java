package com.smorales.definitions;

public final class MaterializedViews {

    public static final String PLAYER_VIEW = "player-mv";
    public static final String SCORE_WITH_PLAYER_VIEW = "score-with-player-mv";
    public static final String LEADER_BOARDS_VIEW = "leader-boards-mv";
    public static final String PRODUCTS_VIEW = "products-mv";

    private MaterializedViews() {
        throw new UnsupportedOperationException();
    }
}
