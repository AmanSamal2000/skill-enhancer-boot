package com.learning.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.mysql.StudentBatchEntity;

public interface StudentBatchRepository extends JpaRepository<StudentBatchEntity, Long>{

}
