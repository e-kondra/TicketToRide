package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.model.Ticket;
import com.andersen.TicketToRide.repository.TicketRepository;
import com.andersen.TicketToRide.service.RouteService;
import com.andersen.TicketToRide.service.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LogManager.getLogger();
    /**
     * The list of routes representing the optimal route.
     */
    static ArrayList<Route> optimalRoutes;
    /**
     * The count of segments in the optimal route.
     */
    static Integer optimalCount;

    @Autowired
    RouteService routeService;
    @Autowired
    TicketRepository ticketRepository;

    public Ticket saveTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    /**
     * Retrieves a ticket based on the optimal travel route from a starting city to a destination city.
     *
     * This method finds the optimal travel route between the specified cities. If a valid route is found,
     * it creates a Ticket object containing the travel points, the number of segments, and the calculated price.
     * The ticket is then wrapped in an Optional and returned. If no valid route is found, an empty Optional is returned.
     *
     * @param from the starting city
     * @param to the destination city
     *
     * @return an Optional containing the Ticket object if a valid route is found, or an empty Optional if not
     */
    public Optional<Ticket> getTicketByOptimalTravel(String from, String to){
        ArrayList<Route> routes = findOptimalTravel(from, to);
        if (routes != null && routes.size() > 0 ) {
            Integer countOfSegments = getCountOfSegments(routes);
            Ticket ticket = new Ticket(getTravelPoints(routes),
                    countOfSegments, calculatePrice(countOfSegments));
            return Optional.of(ticket);
        } else return Optional.empty();
    }

    /**
     * Checks if the provided amount is sufficient to cover the price of a ticket.
     *
     * This method compares the given amount with the price of the ticket based on its segments.
     * If the amount is sufficient, the result will indicate success and provide the change.
     * If the amount is insufficient, the result will indicate failure and specify the lacking amount.
     *
     * @param amount the amount of money provided by the user
     * @param ticket the Ticket object containing the details of the travel segments and price
     *
     * @return a HashMap containing the result ("success" or "failure"), the change or lacking amount, and the currency ("GBP")
     */
    public HashMap<String, String> checkAmount(Float amount, Ticket ticket){
        HashMap<String, String> result = new HashMap<>();
        if (BigDecimal.valueOf(amount).compareTo(calculatePrice(ticket.getSegments())) >= 0){
            result.put("result", "success");
            result.put("change", BigDecimal.valueOf(amount).subtract(ticket.getPrice()).setScale(2).toString());
            result.put("currency", "GBP");
        } else{
            result.put("result", "failure");
            result.put("lackOf", ticket.getPrice().subtract(BigDecimal.valueOf(amount)).setScale(2).toString());
            result.put("currency", "GBP");
        }
        return result;
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

    /**
     * Calculates the price based on the number of segments in a travel route.
     *
     * The pricing scheme charges 10 units for every 3 segments, with an additional modifier
     * if there are remaining segments. If there are 2 remaining segments, an extra 7 units is added;
     * if there is 1 remaining segment, an extra 5 units is added.
     *
     * @param numberOfSegments the number of segments in the travel route
     *
     * @return the calculated price as a BigDecimal
     */
    private BigDecimal calculatePrice(Integer numberOfSegments){
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

    /**
     * Constructs a string representing the travel points from a list of routes.
     *
     * This method iterates over the provided list of Route objects, appending each start point followed by " - ".
     * It concludes by appending the end point of the last route in the list.
     *
     * @param routes the list of Route objects representing the travel path
     *
     * @return a String representing the travel points in the format "StartPoint1 - StartPoint2 - ... - EndPoint"
     */
    private String getTravelPoints(ArrayList<Route> routes){
        StringBuilder points = new StringBuilder();
        for(Route r: routes){
            points.append(r.getStartPoint()).append(" - ");
        }
        points.append(routes.get(routes.size() - 1).getEndPoint());
        return points.toString();
    }

    /**
     * Finds the optimal travel route from a starting city to a destination city.
     *
     * This method first validates the input points. If the points are valid, it initializes the necessary
     * variables and invokes a recursive method to find the optimal route. The result is a list of routes
     * representing the optimal travel path.
     *
     * @param from the starting city
     * @param to the destination city
     *
     * @return an ArrayList of Route objects representing the optimal travel route
     */
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

    /**
     * Recursively finds the optimal route from a starting city to a destination city.
     *
     *  This method is called by findOptimalTravel and uses depth-first search to explore all possible routes,
     *  updating the optimal route and its count of segments as it finds shorter paths.
     *
     * @param from             the starting city
     * @param to               the destination city
     * @param traversedCities  a list of cities that have already been traversed
     * @param routeForTicket   the current route being constructed for the ticket
     */
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
