package com.learning.repository.mongo;

import com.learning.entity.mongo.BatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatchMongoRepository extends MongoRepository<BatchCollection, Long> {

}
