package com.learning.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.mysql.BatchEntity;



public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

}
