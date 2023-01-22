package com.learning.entity;

import lombok.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	private String name;
	private Long contactDetails;
	private String qualification;
	private String email;

	/*
	 * All of below boiler plate code will be replaced by lombok
	 * 
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor
	 * 
	 * @Equals @Hashcode
	 */

	}
