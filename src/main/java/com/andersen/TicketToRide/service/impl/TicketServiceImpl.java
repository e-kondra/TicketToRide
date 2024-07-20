package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.service.RouteService;
import com.andersen.TicketToRide.service.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;


@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LogManager.getLogger();
    static Integer optimalCount;
    static ArrayList<Route> optimalRoutes;
    @Autowired
    RouteService routeService;




    public BigDecimal  calculatePrice(Integer numberOfSegments){
        log.info("Calculate price");
        int mod = 0;
        if (numberOfSegments % 3 > 0){
            mod = (numberOfSegments % 3== 2) ? 7 : 5;
        }
        int intPrice = (numberOfSegments / 3) * 10 + mod;
        log.info("Number of segments = " + numberOfSegments + ", price = " + intPrice);
        return BigDecimal.valueOf(intPrice);
    }

    public Integer getCountOfSegments(ArrayList<Route> routes){
        return routes.stream().mapToInt(r -> r.getNumberOfSegments()).sum();
    }

    public String getTravelPoints(ArrayList<Route> routes){
        String points = "";
        for(Route r: routes){
            points += r.getStartPoint() + " - ";
        }
        points += routes.get(routes.size()-1).getEndPoint();
        return points;
    }

    public ArrayList<Route> findOptimalTravel(String from, String to) {
        log.info("Find Optimal travel from " + from + " to " + to );
        optimalRoutes = new ArrayList<>();
        optimalCount = 100;
        ArrayList<String> traversedCities = new ArrayList<>();
        traversedCities.add(from);
        return getRoute(from, to, traversedCities, new ArrayList<>());
    }

//todo void
    private ArrayList<Route> getRoute(String from, String to, ArrayList<String> traversedCities, ArrayList<Route> routeForTicket) {
        ArrayList<Route> routes = (ArrayList<Route>) routeService
                .findAllRoutesByStartPointExcludeEndpoints(from, traversedCities);
        for (Route r : routes) {
            ArrayList<Route> routeTicket = new ArrayList<Route>();
            routeTicket.addAll(routeForTicket);
            Optional<Route> optionalRoute = routes.stream()
                    .filter(x -> x.getEndPoint().equals(to)).findFirst();
            if (optionalRoute.isPresent()) {
                routeTicket.add(optionalRoute.get());
                if (getCountOfSegments(routeForTicket) < optimalCount) {
                    optimalCount = getCountOfSegments(routeTicket);
                    optimalRoutes = routeTicket;
                }
            } else {
                routeTicket.add(r);
                traversedCities.add(r.getStartPoint());
                getRoute(r.getEndPoint(), to, traversedCities, routeTicket);
            }
        }
        return optimalRoutes;
    }


}
