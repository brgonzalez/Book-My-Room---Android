package com.snaptechnology.bgonzalez.crud;

import com.snaptechnology.bgonzalez.model.Location;

import java.util.List;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public interface LocationDAO extends GenericDAO<Location,String> {
    public List<Location> findAll();

}
