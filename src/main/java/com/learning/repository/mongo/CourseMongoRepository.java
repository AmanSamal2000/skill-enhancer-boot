package com.learning.repository.mongo;


import com.learning.entity.mongo.CourseCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseMongoRepository extends MongoRepository<CourseCollection, Long> {
}
