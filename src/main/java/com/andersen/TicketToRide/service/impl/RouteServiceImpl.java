package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.repository.RouteRepository;
import com.andersen.TicketToRide.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteRepository routeRepository;

    @Override
    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);

    }

    @Override
    public List<Route> findAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public List<Route> findAllRoutesByStartPointExcludeEndpoints( String from, List<String> excluding) {
        return routeRepository.findAllByStartPointExcludeEndPoints(from, excluding);

    }

    @Override
    public List<Route> findAllRoutesByStart( String from) {
        return routeRepository.findAllByStartPoint(from);
    }

    @Override
    public List<Route> findAllByStartPointRangeAndEndPointExcluding( List<String> from, List<String> excluding) {
        return routeRepository.findAllByStartPointRange(from, excluding);
    }

    @Override
    public Route findByStartPointAndEndPoint(String startPoint, String endPoint){
        return routeRepository.findByStartPointAndEndPoint(startPoint, endPoint);
    }

    @Override
    public Route saveRoute(Route route) {
        if (!hasNoMatch(route)) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT);  //to improve!!!
        }
        return routeRepository.save(route);
    }

    public boolean hasNoMatch(Route route) {
        return routeRepository.findAll().stream()
                .noneMatch(t -> !t.getId().equals(route.getId()) &&
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
            System.out.println(e);
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return routes;
    }
}
