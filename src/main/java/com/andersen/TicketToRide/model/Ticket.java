package com.andersen.TicketToRide.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ticket_id")
    private Long id;

    @Column(name="travel_points")
    private String routesChain;

    @Column(name="segments")
    private Integer segments;

    @Column(name="price")
    private BigDecimal price;

    public Ticket(String routesChain, Integer segments, BigDecimal price) {
        this.routesChain = routesChain;
        this.segments = segments;
        this.price = price;
    }
}
