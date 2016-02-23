package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.users.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import se.walkercrou.places.Place;

import javax.persistence.*;
import java.util.List;

/**
 * Created by morand3 on 12/23/2015.
 */
@Entity
@Table(name = "F_LOCATION")
public class Location {

    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue
    private Long id;
    @Column
    private String name;

    @Column
    private double longitude;
    @Column
    private double latitude;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @JsonIgnore
    private User createdBy;

    @Column(unique = true)
    private String googleId;

    @Column(length = 255)
    private String types;

    public Location(String name, double latitude, double longitude, User user) {
        this.createdBy = user;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    public Location(Place place) {
        this.name = place.getName();
        this.longitude = place.getLongitude();
        this.latitude = place.getLatitude();
        this.googleId = place.getPlaceId();
        this.types = StringUtils.join(place.getTypes(), ",");
    }

    public Location(String name, User user) {
        this(name, 0, 0, user);
    }

    public String getGoogleId() {
        return googleId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTypes() {
        return types;
    }

    public double distanceTo(Location that) {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        double KM_PER_NAUTICAL_MILE = 1.852;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(that.latitude);
        double lon2 = Math.toRadians(that.longitude);

        // great circle distance in radians, using law of cosines formula
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // each degree on a great circle of Earth is 60 nautical miles
        double nauticalMiles = 60 * Math.toDegrees(angle);
        double km = KM_PER_NAUTICAL_MILE * nauticalMiles;
        return km;
    }

    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }
}
