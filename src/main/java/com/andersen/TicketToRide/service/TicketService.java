package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Route;

import java.util.ArrayList;

public interface TicketService {
    //ArrayList<Route> findOptimalTravel(String from, String to);
    ArrayList<Route> findOptimalTravel2(String from, String to);
}
