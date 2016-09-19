package com.snaptechnology.bgonzalez.JPA.impl;

import com.snaptechnology.bgonzalez.JPA.repository.LocationRepository;
import com.snaptechnology.bgonzalez.model.Location;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by bgonzalez on 19/09/2016.
 */
public class LocationRepositoryImpl  {

    private LocationRepository locationRepository;

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }


    public Iterable<Location> listAllLocations() {
        return locationRepository.findAll();
    }

    public static void main(String[] args){
        LocationRepositoryImpl l = new LocationRepositoryImpl();
        l.listAllLocations();
    }
}
