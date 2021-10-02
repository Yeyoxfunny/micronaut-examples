package com.smorales.lambda;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class SecretsManagerFacade {

    private final SecretsManagerClient secretsClient;

    public SecretsManagerFacade(SecretsManagerClient secretsClient) {
        this.secretsClient = secretsClient;
    }

    public String getSecrets(String secretName) {
        GetSecretValueRequest request = GetSecretValueRequest.builder().secretId(secretName).build();
        log.info("Get Secret Value: {}", secretName);
        GetSecretValueResponse response = secretsClient.getSecretValue(request);
        return response.secretString();
    }

}
