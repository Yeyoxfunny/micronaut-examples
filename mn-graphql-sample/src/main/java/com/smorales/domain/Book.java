package com.smorales.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Book {

    private final String id;
    private final String name;
    private final int pageCount;
    private final Author author;
}
