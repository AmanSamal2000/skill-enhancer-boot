package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.TrainerEntity;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>{

}
