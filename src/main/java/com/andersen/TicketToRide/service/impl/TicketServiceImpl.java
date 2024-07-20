package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.model.Ticket;
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


    public Optional<Ticket> getTicketByOptimalTravel(String from, String to){
        ArrayList<Route> routes = findOptimalTravel(from, to);
        if (routes != null && routes.size() > 0 ) {
            Integer countOfSegments = getCountOfSegments(routes);
            Ticket ticket = new Ticket(getTravelPoints(routes),
                    countOfSegments, calculatePrice(countOfSegments));
            return Optional.of(ticket);
        } else return Optional.empty();
    }


    private boolean isPointsValid(String[] points) {
        log.info("Input cities validation");
        for(String point: points){
            if(!(routeService.findAllRoutesByStart(point.toUpperCase()).size() > 0)){
                log.info("Incorrect value of city " + point);
                return false;
            }
        }
        return true;
    }

    private BigDecimal  calculatePrice(Integer numberOfSegments){
        log.info("Calculate price");
        int mod = 0;
        if (numberOfSegments % 3 > 0){
            mod = (numberOfSegments % 3== 2) ? 7 : 5;
        }
        int intPrice = (numberOfSegments / 3) * 10 + mod;
        log.info("Number of segments = " + numberOfSegments + ", price = " + intPrice);
        return BigDecimal.valueOf(intPrice);
    }

    private Integer getCountOfSegments(ArrayList<Route> routes){
        return routes.stream().mapToInt(Route::getNumberOfSegments).sum();
    }

    private String getTravelPoints(ArrayList<Route> routes){
        StringBuilder points = new StringBuilder();
        for(Route r: routes){
            points.append(r.getStartPoint()).append(" - ");
        }
        points.append(routes.get(routes.size() - 1).getEndPoint());
        return points.toString();
    }

    private ArrayList<Route> findOptimalTravel(String from, String to) {
        if (isPointsValid(new String[]{from, to})){
            log.info("Find Optimal travel from " + from + " to " + to );
            optimalRoutes = new ArrayList<>();
            optimalCount = 100;
            ArrayList<String> traversedCities = new ArrayList<>();
            traversedCities.add(from.toUpperCase());
            getRoute(from.toUpperCase(), to.toUpperCase(), traversedCities, new ArrayList<>());
        } else {
            log.info("Invalid input values");
        }
        return optimalRoutes;
    }

    private void getRoute(String from, String to, ArrayList<String> traversedCities, ArrayList<Route> routeForTicket) {
        ArrayList<Route> routes = (ArrayList<Route>) routeService
                .findAllRoutesByStartPointExcludeEndpoints(from, traversedCities);
        for (Route r : routes) {
            ArrayList<Route> routeTicket = new ArrayList<>(routeForTicket);
            Optional<Route> optionalRoute = routes.stream()
                    .filter(x -> x.getEndPoint().equals(to)).findFirst();
            if (optionalRoute.isPresent()) {
                routeTicket.add(optionalRoute.get());
                if (getCountOfSegments(routeTicket) < optimalCount) {
                    optimalCount = getCountOfSegments(routeTicket);
                    optimalRoutes = routeTicket;
                }
            } else {
                routeTicket.add(r);
                traversedCities.add(r.getStartPoint());
                getRoute(r.getEndPoint(), to, traversedCities, routeTicket);
            }
        }
    }


}
