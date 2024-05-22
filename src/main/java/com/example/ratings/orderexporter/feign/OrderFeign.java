package com.example.ratings.orderexporter.feign;

import com.example.ratings.orderexporter.OrderDto;
import com.example.ratings.orderexporter.configuration.FeignConfiguration;
import com.example.ratings.orderexporter.fallback.FallbackVurdere;
import feign.Headers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(value = "${feign.placeholder}", url = "${feign.host}", fallback = FallbackVurdere.class, configuration = FeignConfiguration.class)
public interface OrderFeign {

    @Headers("Content-Type: application/json")
    @RequestMapping(method = RequestMethod.POST, value = "api/vurdere/sample")
    @CircuitBreaker(name = "sendOrderCircuitBreak", fallbackMethod = "sendOrderFallback")
    String sendOrder(@RequestBody OrderDto orderDto);

    default String sendOrderFallback(Exception exception) {
        System.out.println("circuit breaker default method");
        return "";
    }
}
