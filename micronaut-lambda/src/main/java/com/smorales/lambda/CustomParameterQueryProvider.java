package com.smorales.lambda;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.env.Environment;
import io.micronaut.context.env.EnvironmentPropertySource;
import io.micronaut.discovery.aws.parameterstore.AWSParameterQueryProvider;
import io.micronaut.discovery.aws.parameterstore.AWSParameterStoreConfiguration;
import io.micronaut.discovery.aws.parameterstore.ParameterQuery;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Slf4j
//@Singleton
//@BootstrapContextCompatible
//@Replaces(AWSParameterQueryProvider.class)
public class CustomParameterQueryProvider implements AWSParameterQueryProvider {

    private static final String SHARED_CONTEXT = "commons";
    private static final String SERVICE_CONTEXT = "config-manager";
    private static final int BASE_PRIORITY = EnvironmentPropertySource.POSITION + 100;


    @Override
    public List<ParameterQuery> getParameterQueries(
            Environment environment,
            Optional<String> serviceId,
            AWSParameterStoreConfiguration configuration) {
        String path = configuration.getRootHierarchyPath();
        String normalizedPath = !path.endsWith("/") ? path + "/" : path;
        String commonConfigPath = normalizedPath + SHARED_CONTEXT;
        String applicationSpecificPath = normalizedPath + SERVICE_CONTEXT;

        List<ParameterQuery> queries = new ArrayList<>();
        addNameAndPathQueries(queries, commonConfigPath, SHARED_CONTEXT, BASE_PRIORITY + 1);
        addNameAndPathQueries(queries, applicationSpecificPath, SERVICE_CONTEXT, BASE_PRIORITY + 2);
        return queries;
    }

    private void addNameAndPathQueries(
            List<ParameterQuery> queries, String value, String propertySourceName, int priority) {
        queries.add(new ParameterQuery(value, propertySourceName, priority, true));
        queries.add(new ParameterQuery(value, propertySourceName, priority, false));
    }
}

