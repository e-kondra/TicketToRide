package com.andersen.TicketToRide.repository;

import com.andersen.TicketToRide.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
//    @Query("select * from routes r where r.start_point = ?1 and r.end_point not in (?2)")
//    List<Route> findAllByStartPointExcludeEndPoints (String startPoint, String excluding);

    @Query(value = "SELECT * FROM ROUTES WHERE START_POINT = ?1 AND END_POINT NOT IN (?2)", nativeQuery = true)
    List<Route> findAllByStartPointExcludeEndPoints(String startPoint, List<String> excl);

    @Query(value = "SELECT * FROM ROUTES r WHERE r.START_POINT IN (?1) AND r.END_POINT NOT IN (?2)", nativeQuery = true)
    List<Route> findAllByStartPointRange(List<String> startPoints, List<String> excluding);
    List<Route> findAllByStartPoint (String startPoint);

    Route findByStartPointAndEndPoint(String startPoint, String endPoint);
}
