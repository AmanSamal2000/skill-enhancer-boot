package com.learning.mongorepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.collection.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

}
