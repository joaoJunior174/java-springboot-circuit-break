package com.example.ratings.orderexporter.fallback;

import com.example.ratings.orderexporter.OrderDto;
import com.example.ratings.orderexporter.feign.OrderFeign;
import org.springframework.stereotype.Component;

@Component
public class FallbackVurdere implements OrderFeign {
    @Override
    public String sendOrder(OrderDto orderDto) {
        System.out.println("Entrei no fallback :(");
        return null;
    }
}
