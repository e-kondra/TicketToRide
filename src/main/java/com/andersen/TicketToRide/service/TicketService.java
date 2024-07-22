package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Ticket;

import java.util.HashMap;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> getTicketByOptimalTravel(String from, String to);

    Ticket saveTicket(Ticket ticket);

    HashMap<String, String> checkAmount(Float amount, Ticket ticket);
}
