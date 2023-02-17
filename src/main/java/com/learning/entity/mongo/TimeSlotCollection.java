package com.learning.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "timeSlot")
public class TimeSlotCollection {

    @Id
    @Field("_id")
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long trainerId;

}
