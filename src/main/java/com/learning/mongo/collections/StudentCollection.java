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
@Document(collection = "student")
public class StudentCollection {

    @Id
    @Field("_id")
    private Long id;
    private String name;
    private Long contactDetails;
    private String qualification;
    private String email;
}
