package com.learning.model;

import com.learning.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchModel {


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