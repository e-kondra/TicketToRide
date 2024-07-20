package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.Ticket;

import java.util.Optional;

public interface TicketService {
    Optional<Ticket> getTicketByOptimalTravel(String from, String to);

    Ticket saveTicket(Ticket ticket);
}
