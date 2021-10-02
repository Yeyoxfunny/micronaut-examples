package com.smorales.dto;

import com.smorales.model.join.Enriched;
import lombok.Data;

import java.util.SortedSet;

@Data
public class BoardScore {

    private final String productId;

    private final SortedSet<Enriched> highScores;

}
