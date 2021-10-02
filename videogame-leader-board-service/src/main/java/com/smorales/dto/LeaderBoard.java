package com.smorales.dto;

import lombok.Data;

import java.util.List;

@Data
public class LeaderBoard {

    private final List<BoardScore> scores;

}
