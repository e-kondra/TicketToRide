package com.andersen.TicketToRide;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.service.RouteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

@SpringBootApplication
public class TicketToRideApplication {

	public static void main(String[] args) {

		SpringApplication.run(TicketToRideApplication.class, args);

	}

}
