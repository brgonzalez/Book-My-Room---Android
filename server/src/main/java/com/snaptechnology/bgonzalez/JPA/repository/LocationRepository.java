package com.snaptechnology.bgonzalez.JPA.repository;

import com.snaptechnology.bgonzalez.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by bgonzalez on 16/09/2016.
 */

@Repository
public interface LocationRepository extends CrudRepository<Location,String> {
}
