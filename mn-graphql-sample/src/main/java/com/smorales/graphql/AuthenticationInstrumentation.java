package com.smorales.graphql;

import com.smorales.utils.AuthenticationException;
import graphql.GraphQLContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.utils.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

import static com.smorales.graphql.RequestResponseCustomizer.HTTP_REQUEST_CTX;

@Singleton
@Slf4j
public class AuthenticationInstrumentation extends SimpleInstrumentation {

    private static final DataFetcher<?> UNAUTHORIZED_DATA_FETCHER = environment -> {
        throw new AuthenticationException();
    };

    private final SecurityService securityService;

    public AuthenticationInstrumentation(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        DataFetchingEnvironment environment = parameters.getEnvironment();
        GraphQLContext context = environment.getContext();
        HttpRequest<?> request = context.get(HTTP_REQUEST_CTX);
        boolean expiredAuth = request.getHeaders().contains(HttpHeaders.AUTHORIZATION) &&
                !securityService.isAuthenticated();
        if (expiredAuth) {
            log.info("Unauthorized, maybe the authentication is expired");
        }
        return expiredAuth ? UNAUTHORIZED_DATA_FETCHER : dataFetcher;
    }
}
