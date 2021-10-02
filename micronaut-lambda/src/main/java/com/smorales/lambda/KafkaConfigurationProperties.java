package com.smorales.lambda;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@ConfigurationProperties("kafka.bootstrap")
public class KafkaConfigurationProperties {

    private String servers;

}
