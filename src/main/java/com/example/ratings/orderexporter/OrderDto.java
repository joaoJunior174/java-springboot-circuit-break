package com.example.ratings.orderexporter;

import lombok.Data;

@Data
public class OrderDto {
    private String email;
    private String status;
    private Double price;
}
