package com.delirium.finapp.finance.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by style on 03/01/2016.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select l from Location l where l.googleId=:googleId")
    public Location findByGoogleId(@Param("googleId") String googleId);

    @Query("select l from Location l " +
        "where l.latitude between (:lat - (:rad/111045.0)) and (:lat + (:rad/111045.0))" +
        "and l.longitude between (:lng - (:rad/(111045.0 * cos(radians(:lat)))))" +
        "                    and (:lng + (:rad/(111045.0 * cos(radians(:lat)))))")
    List<Location> findNearBy(@Param("lat") double lat, @Param("lng") double lng, @Param("rad") double rad);

    @Query("select l from Location l where l.name like '%:q%'")
    List<Location> queryLocations(@Param("q") String q);
}

