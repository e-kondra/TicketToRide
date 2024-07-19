package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;


import java.io.File;
import java.util.List;
import java.util.Optional;

public interface RouteService {

    List<Route> loadTicketsFromFile(File file);
    Optional<Route> findRouteById(Long id);
    List<Route> findAllRoutes();
    public List<Route> findAllRoutesByStartPointExcludeEndpoints( String from, List<String>  excluding);
    public List<Route> findAllRoutesByStart( String from);
    Route saveRoute(Route route);
    boolean hasNoMatch(Route route);
    List<Route> findAllByStartPointRangeAndEndPointExcluding(List<String> from, List<String> excluding);

    public Route findByStartPointAndEndPoint(String startPoint, String endPoint);


}
