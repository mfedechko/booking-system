package com.example.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigInteger;

@SpringBootApplication
public class BookingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}
