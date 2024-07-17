package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;


import java.util.List;
import java.util.Optional;

public interface RouteService {

    Optional<Route> findRouteById(Long id);
    List<Route> findAllRoutes();
    Route saveRoute(Route route);
    boolean hasNoMatch(Route route);


}
