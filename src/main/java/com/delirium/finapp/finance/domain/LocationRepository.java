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

    @Query("select l from Location l where ABS(l.latitude-:lat)<0.2 and ABS(l.longitude-:lng)<0.2")
    List<Location> findNearBy(@Param("lat") double lat, @Param("lng") double lng);

    @Query("select l from Location l where l.name like '%:q%'")
    List<Location> queryLocations(@Param("q") String q);
}

