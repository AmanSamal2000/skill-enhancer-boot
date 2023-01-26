package com.learning.entity;

import com.learning.enums.BatchStatus;
import lombok.*;

import java.time.LocalDate;

import javax.persistence.*;


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
	@Enumerated(value = EnumType.STRING)
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
