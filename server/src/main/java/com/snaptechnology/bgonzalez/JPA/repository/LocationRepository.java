package com.snaptechnology.bgonzalez.JPA.repository;

import com.snaptechnology.bgonzalez.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Location repository is a interface that implements CrudRepository
 * to use all methods to access to database
 * @author bgonzalez
 * @since 16/09/2016.
 */

@Repository
public interface LocationRepository extends CrudRepository<Location,String> {
}
