package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.BatchEntity;



public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

}
