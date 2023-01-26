package com.learning.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String curriculum;
	private String duration;
	
	/* All of below boiler plate code will be replaced by lombok
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor 
	 * @Equals @Hashcode
	 *  */


}
