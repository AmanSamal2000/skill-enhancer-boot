package com.learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

}
