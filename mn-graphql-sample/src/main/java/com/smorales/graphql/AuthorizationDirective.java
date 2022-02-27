package com.smorales.graphql;

import com.smorales.utils.AuthenticationException;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import io.micronaut.security.utils.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class AuthorizationDirective implements SchemaDirectiveWiring {

    private final SecurityService securityService;

    public AuthorizationDirective(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldDefinition field = environment.getElement();
        GraphQLFieldsContainer parentType = environment.getFieldsContainer();

        DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
        DataFetcher<?> authDataFetcher = dataFetchingEnvironment -> {
            log.warn("User is authenticated? {} for field: {}", securityService.isAuthenticated(), field.getName());
            if (!securityService.isAuthenticated()) {
                throw new AuthenticationException("User must be authenticated");
            }
            return originalDataFetcher.get(dataFetchingEnvironment);
        };

        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field;
    }
}
