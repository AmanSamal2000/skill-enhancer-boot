package com.learning.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.mysql.TrainerEntity;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>{

}
