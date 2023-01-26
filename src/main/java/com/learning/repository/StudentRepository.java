package com.learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.StudentEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    @Query(value = "select email from student" ,nativeQuery = true)
    List<String> findEmails();


}
