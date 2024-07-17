package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.repository.RouteRepository;
import com.andersen.TicketToRide.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
}
