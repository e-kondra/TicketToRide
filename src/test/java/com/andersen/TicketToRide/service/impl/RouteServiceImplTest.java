package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    RouteRepository mockRepository;
    @InjectMocks
    RouteServiceImpl service;

    private Route route;

    @BeforeEach
    public void init(){
        this.route = new Route(1L, "LONDON", "READING", 1);
    }

    @Test
    void findAllRoutesByStartPointExcludeEndpointsPositive() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING", 1));
        when(mockRepository.findAllByStartPointExcludeEndPoints(any(), any())).thenReturn(routes);

        List<Route> routes2 =  service.findAllRoutesByStartPointExcludeEndpoints("LONDON", List.of(new String[]{"NORTHHAMPTON"}));

        assertEquals(routes2.size(), routes.size());
        assertEquals(routes2.get(0).getStartPoint(), routes.get(0).getStartPoint());
        verify(mockRepository, times(1)).findAllByStartPointExcludeEndPoints("LONDON", List.of(new String[]{"NORTHHAMPTON"}));
    }

    @Test
    void findAllRoutesByStartPointExcludeEndpointsNegative() {
        when(mockRepository.findAllByStartPointExcludeEndPoints(any(), any())).thenReturn(Collections.emptyList());
        assertTrue(service.findAllRoutesByStartPointExcludeEndpoints("LONDON", List.of(new String[]{"NORTHHAMPTON"})).isEmpty());
        verify(mockRepository, times(1)).findAllByStartPointExcludeEndPoints("LONDON", List.of(new String[]{"NORTHHAMPTON"}));
    }

    @Test
    void findAllRoutesByStartPositive() {
        String from = "LONDON";
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING", 1));
        routes.add(new Route(1L,"LONDON", "NORTHHAMPTON", 2));
        when(mockRepository.findAllByStartPoint(any())).thenReturn(routes);

        List<Route> routes2 = service.findAllRoutesByStart(from);

        assertEquals(routes2.size(),2);
        assertEquals(routes2.get(0).getStartPoint(), routes.get(0).getStartPoint());
        assertEquals(routes2.get(0).getNumberOfSegments(), routes.get(0).getNumberOfSegments());
        verify(mockRepository, times(1)).findAllByStartPoint(from);
    }

    @Test
    void findAllRoutesByStartNegative() {
        when(mockRepository.findAllByStartPoint(any())).thenReturn(Collections.emptyList());
        assertTrue(service.findAllRoutesByStart("LONDON").isEmpty());
        verify(mockRepository, times(1)).findAllByStartPoint("LONDON");
    }

    @Test
    void findAllRoutesByStartCorner() {
        String from = "VILNIUS";
        when(mockRepository.findAllByStartPoint(any())).thenReturn(Collections.emptyList());
        List<Route> routes2 = service.findAllRoutesByStart(from);
        assertEquals(routes2.size(), 0);
        verify(mockRepository, times(1)).findAllByStartPoint(from);
    }

    @Test
    void saveRoutePositive() throws Exception {
        when(mockRepository.save(route)).thenReturn(route);
        service.saveRoute(route);
        verify(mockRepository,times(1)).save(route);
    }

    @Test
    void saveRoutNegative() {
        when(mockRepository.save(any(Route.class))).thenReturn(null);
        assertThrows(RuntimeException.class, () -> {service.saveRoute(null);});
        verify(mockRepository,times(0)).save(route);
    }

}