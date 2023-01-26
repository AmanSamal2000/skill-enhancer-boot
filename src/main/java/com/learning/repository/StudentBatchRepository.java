package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.StudentBatchEntity;

public interface StudentBatchRepository extends JpaRepository<StudentBatchEntity, Long>{

}
