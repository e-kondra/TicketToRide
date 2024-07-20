package com.andersen.TicketToRide.repository;

import com.andersen.TicketToRide.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query(value = "SELECT * FROM ROUTES WHERE START_POINT = ?1 AND END_POINT NOT IN (?2)", nativeQuery = true)
    List<Route> findAllByStartPointExcludeEndPoints(String startPoint, List<String> excl);
    List<Route> findAllByStartPoint (String startPoint);
    Route findByStartPointAndEndPoint(String startPoint, String endPoint);
}
