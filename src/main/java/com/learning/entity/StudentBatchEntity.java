package com.learning.entity;

import lombok.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="studentBatch")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentBatchEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double fees;
	private Long studentId;
	private Long batchId;
	
	/* All of below boiler plate code will be replaced by lombok
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor 
	 * @Equals @Hashcode
	 *  */
	

}
