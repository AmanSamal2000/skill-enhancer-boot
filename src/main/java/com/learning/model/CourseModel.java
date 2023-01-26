package com.learning.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseModel {

	private Long id;
	private String name;
	private String curriculum;
	private String duration;

}