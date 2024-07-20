package com.andersen.TicketToRide.controller;

import com.andersen.TicketToRide.model.Route;
import com.andersen.TicketToRide.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping(value = "/find_ticket")
    public ResponseEntity findTicket(){
        ArrayList<Route> routes = ticketService.findOptimalTravel("READING", "BIRGMINGHAM");
        Integer countOfSegments = ticketService.getCountOfSegments(routes);
        HashMap ticketInfo = new HashMap<>();
        ticketInfo.put("Travel points", ticketService.getTravelPoints(routes));
        ticketInfo.put("currency", "GBP");
        ticketInfo.put("price", ticketService.calculatePrice(countOfSegments));
        ticketInfo.put("segments", countOfSegments);

        return new ResponseEntity<>(ticketInfo, HttpStatus.OK);
    }
}
