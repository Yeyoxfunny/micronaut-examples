package com.smorales.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Author {

    private final String id;
    private final String firstName;
    private final String lastName;
}
