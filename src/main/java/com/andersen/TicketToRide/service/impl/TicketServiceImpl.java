package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.service.RouteService;
import com.andersen.TicketToRide.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    RouteService routeService;

    static Integer optimalCount;
    static ArrayList<Route> optimalRoutes;

//    public ArrayList<Route> findOptimalTravel(String from, String to) {
//
//        ArrayList<Route> routes = (ArrayList<Route>) routeService.findAllRoutesByStart(from);
//
//        ArrayList<Route> optimalRoutes = new ArrayList<>();
//        optimalCount = 100;
//        for (Route r : routes) {
//            ArrayList<Route> routeForTicket = new ArrayList<Route>();
//            ArrayList<String> excl = new ArrayList<>();
//            Optional<Route> route = routes.stream().filter(x -> x.getEndPoint().equals(to)).findFirst();
//            if (route.isPresent()) {
//                routeForTicket.add(route.get());
//                if (getCountOfSegments(routeForTicket) < optimalCount) {
//                    optimalCount = getCountOfSegments(routeForTicket);
//                    optimalRoutes = routeForTicket;
//                }
//            } else {
//                routeForTicket.add(r);
//                excl.add(r.getStartPoint());
//                ArrayList<Route> routes1 = (ArrayList<Route>) routeService.findAllRoutesByStartPointExcludeEndpoints(r.getEndPoint(), excl);
//                Optional<Route> route1 = routes1.stream().filter(x -> x.getEndPoint().equals(to)).findFirst();
//                if (route1.isPresent()) {
//                    routeForTicket.add(route1.get());
//                    if (getCountOfSegments(routeForTicket) < optimalCount) {
//                        optimalCount = getCountOfSegments(routeForTicket);
//                        optimalRoutes = routeForTicket;
//                    }
//                } else {
//                    for (Route ro : routes1) {
//                        ArrayList<Route> routeTicket = new ArrayList<Route>();
//                        routeTicket.addAll(routeForTicket);
//                        routeTicket.add(ro);
//                        excl.add(ro.getStartPoint());
//                        ArrayList<Route> routes2 = (ArrayList<Route>) routeService.findAllRoutesByStartPointExcludeEndpoints(ro.getEndPoint(), excl);
//                        Optional<Route> route2 = routes2.stream().filter(x -> x.getEndPoint().equals(to)).findFirst();
//                        if (route2.isPresent()) {
//                            routeTicket.add(route2.get());
//                            if (getCountOfSegments(routeTicket) < optimalCount) {
//                                optimalCount = getCountOfSegments(routeTicket);
//                                optimalRoutes = routeTicket;
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//         return optimalRoutes;
//
//    }

    private Integer getCountOfSegments(ArrayList<Route> routes){
        return routes.stream().mapToInt(r -> r.getNumberOfSegments()).sum();
    }

    public ArrayList<Route> findOptimalTravel2(String from, String to) {
        ArrayList<Route> routes = (ArrayList<Route>) routeService.findAllRoutesByStart(from);
        optimalRoutes = new ArrayList<>();
        optimalCount = 100;
        ArrayList<String> excl = new ArrayList<>();
        excl.add(from);
        return getRoute(from, to,excl,new ArrayList<>());

    }


    public ArrayList<Route> getRoute(String from, String to, ArrayList<String> excl,/* Integer optimalCount,*/ ArrayList<Route> routeForTicket) {
        ArrayList<Route> routes = (ArrayList<Route>) routeService
                .findAllRoutesByStartPointExcludeEndpoints(from, excl);
        for (Route r : routes) {
            ArrayList<Route> routeTicket = new ArrayList<Route>();
            routeTicket.addAll(routeForTicket);
            Optional<Route> optionalRoute = routes.stream().filter(x -> x.getEndPoint().equals(to)).findFirst();
            if (optionalRoute.isPresent()) {
                routeTicket.add(optionalRoute.get());
                if (getCountOfSegments(routeForTicket) < optimalCount) {
                    optimalCount = getCountOfSegments(routeTicket);
                    optimalRoutes = routeTicket;
                }
            } else {
                routeTicket.add(r);
                excl.add(r.getStartPoint());
                getRoute(r.getEndPoint(), to, excl, routeTicket);
            }
        }
        return optimalRoutes;
    }
}
