package com.smorales.service;

import com.smorales.model.Player;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface PlayerService {

    Flowable<Player> getAll();

    Maybe<Player> getById(String id);

    Single<Player> save(Player player);

}
