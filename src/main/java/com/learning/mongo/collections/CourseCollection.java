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
@Document(collection ="course")
public class CourseCollection {

    @Id
    @Field("_id")
    private Long id;
    private String name;
    private String curriculum;
    private String duration;
}
