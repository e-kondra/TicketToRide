package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;


import java.io.File;
import java.util.List;

public interface RouteService {

    List<Route> loadTicketsFromFile(File file);
    List<Route> findAllRoutesByStartPointExcludeEndpoints( String from, List<String>  excluding);
    List<Route> findAllRoutesByStart( String from);
    Route saveRoute(Route route) throws Exception;

}
