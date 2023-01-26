package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

}
