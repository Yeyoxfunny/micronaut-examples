package com.smorales.graphql;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;


@Factory
public class GraphQLFactory {

    @Bean
    @Singleton
    public GraphQL graphQL(ResourceResolver resourceResolver,
                           GraphQLDataFetchers graphQLDataFetchers,
                           AuthenticationInstrumentation authenticationInstrumentation,
                           AuthorizationDirective authorizationDirective) {

        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        Optional<InputStream> graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls");

        SchemaParser schemaParser = new SchemaParser();
        typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(graphqlSchema.get()))));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .directive("authenticated", authorizationDirective)
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                        .dataFetcher("getBook", graphQLDataFetchers.getBookByIdDataFetcher()))
                //.type(newTypeWiring("Book")
                //        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        return GraphQL.newGraphQL(graphQLSchema)
                .instrumentation(authenticationInstrumentation)
                .build();
    }
}