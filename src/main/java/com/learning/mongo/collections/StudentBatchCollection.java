package com.learning.mongo.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "studentBatch")
public class StudentBatchCollection {

    @Id
    @Field("_id")
    private Long id;
    private Double fees;
    private Long studentId;
    private Long batchId;

}
