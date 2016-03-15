package com.delirium.finapp.tools;

import com.delirium.finapp.finance.domain.Location;
import com.delirium.finapp.finance.domain.LocationRepository;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by style on 03/01/2016.
 */
@Service
public class PlacesService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserService userService;


    public PlacesService() {
    }


    public List<Location> getNearbyPlaces(double lat, double lng) {
        return locationRepository.findNearBy(lat, lng, 100);
//        List<Place> places = client.getNearbyPlaces(lat, lng, RADIUS, GooglePlacesInterface.MAXIMUM_PAGE_RESULTS);
//        List<Location> locations = new LinkedList<>();
//        for (Place place : places) {
//            Location location = new Location(place);
//            locations.add(location);
//        }
//        return locations;
    }

    public List<Location> getPlacesByQuery(String q) {
        return locationRepository.queryLocations(q);
//        return client.getPlacesByQuery(q, GooglePlacesInterface.MAXIMUM_PAGE_RESULTS);
    }

    public void saveNearByLocations(Double latitude, Double longitude) {
        List<Location> locations = getNearbyPlaces(latitude, longitude);
        for (Location location : locations) {
            if (null == locationRepository.findByGoogleId(location.getGoogleId())) {
                locationRepository.save(location);
            }
        }
        locationRepository.flush();
    }

    public Location getOrCreateExactLocation(double lat, double lng) {
        Location res = null;
        List<Location> locations = locationRepository.findNearBy(lat, lng, 5);
        if (locations.isEmpty()) {
            res = new Location("TBD", lat, lng, userService.findCurrentUser());
            locationRepository.save(res);
            return res;
        }
        return locations.get(0);
    }
}
