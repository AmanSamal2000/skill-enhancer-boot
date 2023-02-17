package com.learning.entity.mysql;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="trainer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String specialization;
	
	/* All of below boiler plate code will be replaced by lombok
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor 
	 * @Equals @Hashcode
	 *  */
	}