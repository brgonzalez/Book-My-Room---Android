package com.snaptechnology.bgonzalez.services;

import com.snaptechnology.bgonzalez.JPA.repository.LocationRepository;
import com.snaptechnology.bgonzalez.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bgonzalez on 19/09/2016.
 */
@Service
public class LocationService {
    @Autowired
    LocationRepository repository;

    public List<Location> getAllLocations(){
        List<Location> locations = new ArrayList<Location>();
        for(Location location : repository.findAll()){
            locations.add(location);
        }
        return locations;
    }



}
