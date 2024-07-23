package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.model.Ticket;
import com.andersen.TicketToRide.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {
    @Mock
    TicketRepository mockRepository;
    @Mock
    RouteServiceImpl routeService;
    @InjectMocks
    TicketServiceImpl service;

    private Ticket ticket;
    List<Route> routes;

    @BeforeEach
    public void init(){
        Ticket ticket = new Ticket(1L, "LONDON - READING - SWINDON - BRISTOL", 7, BigDecimal.valueOf(25));

    }

    @Test
    void saveTicketPositive() {
        when(mockRepository.save(any())).thenReturn(ticket);
        service.saveTicket(ticket);
        verify(mockRepository,times(1)).save(ticket);
    }
    @Test
    public void saveTicketNegative() {
        when(mockRepository.save(any(Ticket.class))).thenReturn(null);
        assertThrows(RuntimeException.class, ()->{
            service.saveTicket(null);
        });
    }

    @Test
    public void testCheckAmount_SufficientAmount() {
        Float amount = 27.00f;
        Ticket ticket1 =  new Ticket(1L, "LONDON - READING - SWINDON - BRISTOL", 7, BigDecimal.valueOf(25));

        HashMap<String, String> result = service.checkAmount(amount, ticket1);

        assertEquals("success", result.get("result"));
        assertEquals("2.00", result.get("change"));
        assertEquals("GBP", result.get("currency"));
    }

    @Test
    public void testCheckAmount_InsufficientAmount() {
        Float amount = 20.00f;
        Ticket ticket1 =  new Ticket(1L, "LONDON - READING - SWINDON - BRISTOL", 7, BigDecimal.valueOf(25));

        HashMap<String, String> result = service.checkAmount(amount, ticket1);

        assertEquals("failure", result.get("result"));
        assertEquals("5.00", result.get("lackOf"));
        assertEquals("GBP", result.get("currency"));
    }

    @Test
    public void testCheckAmount_ZeroAmount() {
        Float amount = 0.0f;
        Ticket ticket1 =  new Ticket(1L, "LONDON - READING - SWINDON - BRISTOL", 7, BigDecimal.valueOf(25));

        HashMap<String, String> result = service.checkAmount(amount, ticket1);

        assertEquals("failure", result.get("result"));
        assertEquals("25.00", result.get("lackOf"));
        assertEquals("GBP", result.get("currency"));
    }

    @Test
    public void testCheckAmount_NegativeAmount() {
        Float amount = -5.0f;
        Ticket ticket1 =  new Ticket(1L, "LONDON - READING - SWINDON - BRISTOL", 7, BigDecimal.valueOf(25));

        HashMap<String, String> result = service.checkAmount(amount, ticket1);

        assertEquals("failure", result.get("result"));
        assertEquals("30.00", result.get("lackOf"));
        assertEquals("GBP", result.get("currency"));
    }


    @Test
    void isPointsValidPositive() {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING",1));
        routes.add(new Route(2L,"LONDON", "NORTHHAMPTON",2));
        String[] points = new String[]{"LONDON", "READING", "NORTHHAMPTON"};
        when(routeService.findAllRoutesByStart(any())).thenReturn(routes);

        boolean result = service.isPointsValid(points);
        assertTrue(result);
    }

    @Test
    void isPointsValidNegative() {
        List<Route> routes = new ArrayList<>();
        String[] points = new String[]{ "SWINDON"};
        when(routeService.findAllRoutesByStart(any())).thenReturn(routes);

        boolean result = service.isPointsValid(points);
        assertFalse(result);
    }

    @Test
    void isPointsValidNull() {
        List<Route> routes = new ArrayList<>();
        String[] points = new String[]{""};
        when(routeService.findAllRoutesByStart(any())).thenReturn(routes);

        boolean result = service.isPointsValid(points);
        assertFalse(result);
    }

    @Test
    void calculatePricePositive() {
        Integer numberOfSegments = 7;

        BigDecimal price = service.calculatePrice(numberOfSegments);

        assertEquals(BigDecimal.valueOf(25), price);
    }
    @Test
    void calculatePriceNegative() {
        Integer numberOfSegments = 0;

        BigDecimal price = service.calculatePrice(numberOfSegments);

        assertEquals(BigDecimal.valueOf(0), price);
    }

    @Test
    void getTravelPointsPositive() {
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING",1));
        routes.add(new Route(2L,"READING", "SWINDON",2));

        String roadPoints = service.getTravelPoints(routes);

        assertEquals(roadPoints, "LONDON - READING - SWINDON");
    }

    @Test
    void getTravelPointsNegative() {
        ArrayList<Route> routes = new ArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> service.getTravelPoints(routes));
    }

    @Test
    void getCountOfSegmentsPositive() {
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING",1));
        routes.add(new Route(2L,"READING", "SWINDON",2));

        Integer result = service.getCountOfSegments(routes);

        assertEquals(result, 3);
    }
    @Test
    void getCountOfSegmentsNegative() {
        ArrayList<Route> routes = new ArrayList<>();

        Integer result = service.getCountOfSegments(routes);

        assertEquals(result, 0);
    }
    @Test
    void getCountOfSegmentsCorner() {
        ArrayList<Route> routes = new ArrayList<>();
        routes.add(new Route(1L,"LONDON", "READING",1));
        routes.add(new Route(2L,"READING", "SWINDON",-2));

        Integer result = service.getCountOfSegments(routes);

        assertEquals(result, -1);
    }

}