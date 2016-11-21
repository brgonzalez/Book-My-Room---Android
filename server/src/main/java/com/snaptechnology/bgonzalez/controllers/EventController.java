package com.snaptechnology.bgonzalez.controllers;

import com.snaptechnology.bgonzalez.model.Event;
import com.snaptechnology.bgonzalez.model.VO.EventVO;
import com.snaptechnology.bgonzalez.services.Office365Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * EventController is a controller to map requests related with events,
 * this requests are going to be from devices and this class is gong to connect to a service to manipulate API Office 365
 *
 * @author Brayan González Chaves
 * @since 20/09/2016.
 */

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private Office365Service office365Service;

    /**
     * Request Method POST to get all events using the restful controller
     * @param eventVO Value object of event with fields location, startDate and endDate
     * @return list of events according to request
     */
    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public List<Event> getEvents (@RequestBody EventVO eventVO){

        return office365Service.getEvents(eventVO);

    }

    /**
     * Request Method POST to create a event using the restful controller
     * @param event
     * @return Operation status code
     */

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String,Integer> createEvent (@RequestBody Event event){
        Map<String,Integer> status = new HashMap<String,Integer>();
        status.put("statusCode",office365Service.createEvent(event));

        return status;
    }

    /**
     * Request Method POST to update a event using the restful controller
     * @param event
     * @return Operation status code
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Integer> updateEvent(@RequestBody Event event){

        Map<String,Integer> status = new HashMap<String,Integer>();
        status.put("statusCode",office365Service.updateEvent(event));

        return status;
    }

    /**
     * Request Method POST to update a event using the restful controller
     * @param event which is going to be eliminated
     * @return Operation status code
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Integer> deleteEvent(@RequestBody Event event){

        Map<String,Integer> status = new HashMap<String,Integer>();
        status.put("statusCode",office365Service.deleteEvent(event));

        return status;
    }


}
