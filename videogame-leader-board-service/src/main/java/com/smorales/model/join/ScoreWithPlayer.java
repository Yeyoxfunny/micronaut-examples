package com.smorales.model.join;

import com.smorales.model.Player;
import com.smorales.model.ScoreEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreWithPlayer {

    private ScoreEvent scoreEvent;
    private Player player;

}
