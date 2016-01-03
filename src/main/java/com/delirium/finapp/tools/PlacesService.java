package com.delirium.finapp.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.GooglePlacesInterface;
import se.walkercrou.places.Place;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by style on 03/01/2016.
 */
@Service
public class PlacesService {

    @Value("${finapp.places.key}")
    private String key;

    private GooglePlaces client;

    public PlacesService() {
    }

    public PlacesService(String key) {
        this.key = key;
        init();
    }

    @PostConstruct
    public void init() {
        client = new GooglePlaces(key);
    }

    public List<Place> getNearbyPlaces(double lat, double lng, double radius) {
        return client.getNearbyPlaces(lat, lng, radius, GooglePlacesInterface.MAXIMUM_RESULTS);
    }

    public List<Place> getPlacesByQuery(String q) {
        return client.getPlacesByQuery(q, GooglePlacesInterface.MAXIMUM_RESULTS);
    }
}
