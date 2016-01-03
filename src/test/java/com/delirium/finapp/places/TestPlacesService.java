package com.delirium.finapp.places;

import com.delirium.finapp.tools.PlacesService;
import org.junit.Test;
import se.walkercrou.places.Place;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by style on 03/01/2016.
 */
public class TestPlacesService {
    PlacesService service = new PlacesService("AIzaSyBcJ63yGXQar1AKavg7gVwKhKLabdf8HCE");

    @Test
    public void testEmpireStateBuilding() {
        List<Place> places = service.getPlacesByQuery("Empire State Building");
        Place empireStateBuilding = null;
        for (Place place : places) {
            if (place.getName().equals("Empire State Building")) {
                empireStateBuilding = place;
                break;
            }
        }
        assertNotNull(empireStateBuilding);
        Place details = empireStateBuilding.getDetails();
        assertEquals("ChIJaXQRs6lZwokRY6EFpJnhNNE", details.getPlaceId());
        assertEquals("Empire State Building", details.getName());
        assertEquals("(212) 736-3100", details.getPhoneNumber());
    }
}
