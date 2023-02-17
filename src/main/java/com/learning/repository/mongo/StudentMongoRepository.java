package com.learning.repository.mongo;

import com.learning.entity.mongo.StudentCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentMongoRepository extends MongoRepository<StudentCollection, Long> {

}
