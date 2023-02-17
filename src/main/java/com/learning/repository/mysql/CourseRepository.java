package com.learning.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.mysql.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

}
