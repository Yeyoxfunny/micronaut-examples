package com.smorales.utils;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import io.micronaut.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AuthenticationException extends RuntimeException implements GraphQLError {

    public AuthenticationException() {
        super("User must be authenticated");
    }

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Map.of("code", HttpStatus.UNAUTHORIZED.name());
    }

    @Override
    public List<SourceLocation> getLocations() {
        return Collections.emptyList();
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
