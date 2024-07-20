package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface TicketService {
    ArrayList<Route> findOptimalTravel(String from, String to);
    BigDecimal calculatePrice(Integer numberOfSegments);
    Integer getCountOfSegments(ArrayList<Route> routes);
    String getTravelPoints(ArrayList<Route> routes);

}
