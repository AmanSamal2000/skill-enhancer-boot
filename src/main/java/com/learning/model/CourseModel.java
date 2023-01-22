package com.learning.model;

import java.util.Objects;

public class CourseModel {
	
	private Long id;
	private String name;
	private String curriculum;
	private String duration;
	
	public CourseModel() {}
	
	

	protected CourseModel(Long id, String name, String curriculum, String duration) {
		super();
		this.id = id;
		this.name = name;
		this.curriculum = curriculum;
		this.duration = duration;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}



	@Override
	public int hashCode() {
		return Objects.hash(curriculum, duration, id, name);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseModel other = (CourseModel) obj;
		return Objects.equals(curriculum, other.curriculum) && Objects.equals(duration, other.duration)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}



	@Override
	public String toString() {
		return "CourseModel [id=" + id + ", name=" + name + ", curriculum=" + curriculum + ", duration=" + duration
				+ "]";
	}
	
	
	
	
	
	
	

}
