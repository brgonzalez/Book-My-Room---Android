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



   @RequestMapping(value = "/create", method = RequestMethod.POST)

   public Map<String,Integer> createEvent (@RequestBody Event event){

       Map<String,Integer> status = new HashMap<String,Integer>();
       status.put("statusCode",office365Service.createEvent(event));

       return status;
   }
    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public List<Event> getEvents (@RequestBody EventVO eventVO){

        return office365Service.getEvents(eventVO);

    }


}
