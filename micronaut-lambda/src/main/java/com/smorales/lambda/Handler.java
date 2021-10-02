package com.smorales.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

import static com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3Entity;

@Slf4j
@Introspected
public class Handler extends MicronautRequestHandler<S3Event, Void> {

    //@Inject
    //private KafkaConfigurationProperties kafkaConfigurationProperties;

    @Inject
    private SecretsManagerFacade secretsManagerFacade;

    @Override
    public Void execute(S3Event input) {
        //log.info("Config Properties: {}", Utils.stringifyAsJson(kafkaConfigurationProperties));
        log.info("Input is null? {}", (input == null));
        log.info("S3 Event Notification: {}", input);
        log.info("Secrets: {}", secretsManagerFacade.getSecrets("/secret/config-manager"));
        input.getRecords()
                .forEach(record -> {
                    S3Entity s3Entity = record.getS3();
                    String bucket = s3Entity.getBucket().getName();
                    String key = s3Entity.getObject().getKey();
                    log.info("S3 Bucket: {} Key: {}", bucket, key);
                });
        return null;
    }
}
