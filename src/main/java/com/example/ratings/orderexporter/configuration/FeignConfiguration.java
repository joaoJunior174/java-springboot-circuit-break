package com.example.ratings.orderexporter.configuration;

import com.example.ratings.orderexporter.Error.CustomErrorDecoder;
import feign.Request.Options;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    @Value("${feign.connectTimeout}")
    private int connectTimeout;

    @Value("${feign.readTimeout}")
    private int readTimeout;

    @Bean
    public Options options() {
        return new Options(connectTimeout, readTimeout);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
