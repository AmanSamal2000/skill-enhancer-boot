package com.learning.repository.mongo;

import com.learning.entity.mongo.TrainerCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainerMongoRepository extends MongoRepository<TrainerCollection, Long> {
}
