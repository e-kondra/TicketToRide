package com.andersen.TicketToRide.config;

import com.andersen.TicketToRide.service.RouteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MainConfiguration {
    @Bean
    CommandLineRunner userCommandLineRunner(RouteService routeService) {
        return args -> {
            RouteConfiguration routConfig = new RouteConfiguration();
            routConfig.getRoutes(routeService);
        };
    }
}
