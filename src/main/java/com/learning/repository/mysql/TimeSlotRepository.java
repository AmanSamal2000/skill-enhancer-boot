package com.learning.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.mysql.TimeSlotEntity;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long>{

}
