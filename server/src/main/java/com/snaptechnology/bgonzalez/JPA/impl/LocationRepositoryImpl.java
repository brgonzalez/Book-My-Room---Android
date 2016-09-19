package com.snaptechnology.bgonzalez.JPA.impl;

import com.snaptechnology.bgonzalez.JPA.repository.LocationRepository;
import com.snaptechnology.bgonzalez.model.Location;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * LocationRepositoryImpl is the way to access to LocationRepository
 * @author bgonzalez
 * @since 19/09/2016
 *
 *
 */
public class LocationRepositoryImpl  {

    private LocationRepository locationRepository;

    /**
     * Set the object locationRepository
     * @param locationRepository
     */
    @Autowired
    public void setLocationRepository(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    /**
     * Get all locations from LocationRepository class
     * @return Iterable list with all locations
     */
    public Iterable<Location> listAllLocations() {
        return locationRepository.findAll();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args){
        LocationRepositoryImpl l = new LocationRepositoryImpl();
        l.listAllLocations();
    }
}
