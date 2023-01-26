package com.learning.collection;

import com.learning.enums.BatchStatus;
import lombok.*;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name="batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer studentCount;
	private LocalDate startDate;
	private LocalDate endDate;
	private BatchStatus batchStatus;
	private Long courseId;
	private Long timeSlotId;

	/*
	 * All of below boiler plate code will be replaced by lombok
	 * 
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor
	 * 
	 * @Equals @Hashcode
	 */

		}
