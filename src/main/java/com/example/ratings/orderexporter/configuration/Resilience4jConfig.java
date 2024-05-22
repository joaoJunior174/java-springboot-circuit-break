package com.example.ratings.orderexporter.configuration;

import feign.FeignException;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.rmi.ServerException;
import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public CircuitBreakerConfigCustomizer testCustomizer() {
        return CircuitBreakerConfigCustomizer
            .of("sendOrderCircuitBreak",
                builder -> builder
                    .slidingWindowSize(100)
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofMinutes(1))
                    .automaticTransitionFromOpenToHalfOpenEnabled(true)
                    .permittedNumberOfCallsInHalfOpenState(1)
                    .minimumNumberOfCalls(5)
                    .slidingWindowSize(10)
                    .recordExceptions(ServerException.class)
            );
    }
}
