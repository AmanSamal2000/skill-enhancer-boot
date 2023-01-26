package com.learning.mongo.mongorepository;

import com.learning.mongo.collections.StudentCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentMongoRepository extends MongoRepository<StudentCollection, Long> {
}
