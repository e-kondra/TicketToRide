package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.repository.RouteRepository;
import com.andersen.TicketToRide.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class RouteServiceImpl implements RouteService {
    private static final Logger log = LogManager.getLogger();
    @Autowired
    RouteRepository routeRepository;

    @Override
    public List<Route> findAllRoutesByStartPointExcludeEndpoints( String from, List<String> excluding) {
        return routeRepository.findAllByStartPointExcludeEndPoints(from, excluding);
    }

    @Override
    public List<Route> findAllRoutesByStart( String from) {
        return routeRepository.findAllByStartPoint(from);
    }


    @Override
    public Route saveRoute(Route route) throws Exception {
        if (!hasNoMatch(route)) {
            throw new Exception(route + " is already exist");
        } else {
            return routeRepository.save(route);
        }
    }

    public boolean hasNoMatch(Route route) {
        return routeRepository.findAll().stream()
                .noneMatch(t ->
                        t.getEndPoint().equals(route.getEndPoint()) &&
                        t.getStartPoint().equals(route.getStartPoint()) &&
                        t.getNumberOfSegments() == route.getNumberOfSegments());
    }

    @Override
    public List<Route> loadTicketsFromFile(File file) {
        List<Route> routes = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            for (String s : lines) {
                Route route = new ObjectMapper().readValue(s, Route.class);
                routeRepository.save(route);
                routes.add(route);
            }
        } catch (NoSuchFileException e) {
            log.warn("File is not found! " + e);
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return routes;
    }
}
