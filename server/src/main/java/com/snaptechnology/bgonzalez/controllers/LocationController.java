package com.snaptechnology.bgonzalez.controllers;

import com.snaptechnology.bgonzalez.JPA.repository.LocationRepository;
import com.snaptechnology.bgonzalez.services.LocationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LocationController is a controller to manipulate request of location,
 * In this project the request are going to be from a devices android
 *
 * @author Brayan González Chaves
 * @since 19/09/2016
 *
 */
@RestController
public class LocationController {
    @Autowired
    LocationRepository repository;

    @Autowired
    LocationService locationService ;

    @RequestMapping("/location/findall")
    public String findAll(){
        JSONArray jsonArray = new JSONArray();

        locationService.getAllLocations().forEach(jsonArray::put);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("locations", jsonArray);
        return jsonObject.toString() ;
    }
}
