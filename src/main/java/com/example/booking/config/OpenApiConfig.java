package com.example.booking.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingApi() {
        return new OpenAPI()
                .info(new Info()
                              .title("Booking & Blocks API")
                              .description("Technical test REST API for bookings and blocks")
                              .version("1.0.0"));
    }

}
