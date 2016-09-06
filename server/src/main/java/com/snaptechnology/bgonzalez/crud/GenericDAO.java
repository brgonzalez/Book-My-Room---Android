package com.snaptechnology.bgonzalez.crud;

/**
 * Created by bgonzalez on 06/09/2016.
 */
public interface GenericDAO<T, PK> {

    PK create(T t);
    T read(PK id);
    void update(T t);
    void delete(T t);
}
