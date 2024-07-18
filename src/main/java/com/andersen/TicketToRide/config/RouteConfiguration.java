package com.andersen.TicketToRide.config;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class RouteConfiguration {

    public void getRoutes(RouteService routeService) {

        try {
            List<Route> routes = routeService.getTicketsFromFile(
                    new File(getClass().getClassLoader().getResource("routes.txt").getFile()));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
