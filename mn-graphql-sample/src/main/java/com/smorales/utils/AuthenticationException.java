package com.smorales.utils;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

public class AuthenticationException extends RuntimeException implements GraphQLError {

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Map.of("error", true);
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
