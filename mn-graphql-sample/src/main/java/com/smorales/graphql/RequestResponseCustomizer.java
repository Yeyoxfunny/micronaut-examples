package com.smorales.graphql;

import com.smorales.utils.AuthenticationException;
import graphql.ExecutionInput;
import graphql.GraphQLContext;
import io.micronaut.configuration.graphql.GraphQLExecutionInputCustomizer;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.utils.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;

@Singleton
@Primary
public class RequestResponseCustomizer implements GraphQLExecutionInputCustomizer {

    public static final String HTTP_REQUEST_CTX = "httpRequest";
    public static final String HTTP_RESPONSE_CTX = "httpResponse";

    @Override
    public Publisher<ExecutionInput> customize(ExecutionInput executionInput, HttpRequest httpRequest,
                                               MutableHttpResponse<String> httpResponse) {
        GraphQLContext graphQLContext = (GraphQLContext) executionInput.getContext();
        graphQLContext.put(HTTP_REQUEST_CTX, httpRequest);
        graphQLContext.put(HTTP_RESPONSE_CTX, httpResponse);
        return Publishers.just(executionInput);
    }
}
