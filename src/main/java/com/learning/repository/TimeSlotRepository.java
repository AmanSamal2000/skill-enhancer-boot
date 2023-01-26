package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.TimeSlotEntity;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long>{

}
