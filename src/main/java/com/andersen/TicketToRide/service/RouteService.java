package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;


import java.io.File;
import java.util.List;
import java.util.Optional;

public interface RouteService {

    List<Route> loadTicketsFromFile(File file);
    Optional<Route> findRouteById(Long id);
    List<Route> findAllRoutes();
    List<Route> findAllRoutesByStartPointExcludeEndpoints( String from, List<String>  excluding);
    List<Route> findAllRoutesByStart( String from);
    Route saveRoute(Route route);
    boolean hasNoMatch(Route route);

    Route findByStartPointAndEndPoint(String startPoint, String endPoint);


}
