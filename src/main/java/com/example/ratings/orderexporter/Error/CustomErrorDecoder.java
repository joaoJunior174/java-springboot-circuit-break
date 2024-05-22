package com.example.ratings.orderexporter.Error;

import com.example.ratings.orderexporter.OrderExporterApplication;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

public class CustomErrorDecoder implements ErrorDecoder {

    Logger logger = LoggerFactory.getLogger(OrderExporterApplication.class);

    @Override
    public Exception decode(String s, Response response)  {
        String message = "";
        System.out.println("O status retornado é " + response.status());
        try {
            message = IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8);
            Integer a = 10;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return switch (response.status()) {
            case 400 -> new BadRequestException(message);
            case 404 -> new ClassNotFoundException(message);
            case 500 -> new ServerException(message);
            default -> new Exception(message);
        };
    }
}
