package com.learning.model;

import java.util.Objects;

public class StudentBatchModel {
	
	private Long id;
	private Double fees;
	private Long studentId;
	private Long batchId;
	
	
	public StudentBatchModel() {}
	
	public StudentBatchModel(Long id, Double fees, Long studentId, Long batchId) {
		super();
		this.id = id;
		this.fees = fees;
		this.studentId = studentId;
		this.batchId = batchId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getFees() {
		return fees;
	}
	public void setFees(Double fees) {
		this.fees = fees;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(batchId, fees, id, studentId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentBatchModel other = (StudentBatchModel) obj;
		return Objects.equals(batchId, other.batchId) && Objects.equals(fees, other.fees)
				&& Objects.equals(id, other.id) && Objects.equals(studentId, other.studentId);
	}

	@Override
	public String toString() {
		return "StudentBatchModel [id=" + id + ", fees=" + fees + ", studentId=" + studentId + ", batchId=" + batchId
				+ "]";
	}
	
	
	
	

}
