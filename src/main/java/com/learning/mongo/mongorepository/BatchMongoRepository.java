package com.learning.mongo.mongorepository;

import com.learning.mongo.collections.BatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatchMongoRepository extends MongoRepository<BatchCollection, Long> {

}
