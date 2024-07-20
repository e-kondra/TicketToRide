package com.andersen.TicketToRide.controller;

import com.andersen.TicketToRide.model.Ticket;
import com.andersen.TicketToRide.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("api")
public class TicketController {
    private static final Logger log = LogManager.getLogger();
    @Autowired
    TicketService ticketService;

    @GetMapping(value = "/find_ticket")
    public ResponseEntity findTicket(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to){
        Optional<Ticket> ticket = ticketService.getTicketByOptimalTravel(from, to);
        if (ticket.isPresent()){
            return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
