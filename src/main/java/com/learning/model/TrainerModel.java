package com.learning.model;

import java.util.Objects;

public class TrainerModel {

	private Long id;
	private String name;
	private String specialization;

	public TrainerModel() {
	}

	public TrainerModel(Long id, String name, String specialization) {
		super();
		this.id = id;
		this.name = name;
		this.specialization = specialization;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, specialization);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrainerModel other = (TrainerModel) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(specialization, other.specialization);
	}

	@Override
	public String toString() {
		return "TrainerModel [id=" + id + ", name=" + name + ", specialization=" + specialization + "]";
	}
	

}
