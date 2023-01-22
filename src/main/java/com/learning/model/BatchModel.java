package com.learning.model;

import java.time.LocalDate;
import java.util.Objects;

import com.learning.entity.BatchEntity;

public class BatchModel {

	
	private Long id;
	private Integer studentCount;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long courseId;
	private Long timeSlotId;

	/*
	 * All of below boiler plate code will be replaced by lombok
	 * 
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor
	 * 
	 * @Equals @Hashcode
	 */

	public BatchModel() {
	}



	protected BatchModel(Long id, Integer studentCount, LocalDate startDate, LocalDate endDate, Long courseId,
			Long timeSlotId) {
		super();
		this.id = id;
		this.studentCount = studentCount;
		this.startDate = startDate;
		this.endDate = endDate;
		this.courseId = courseId;
		this.timeSlotId = timeSlotId;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}



	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getTimeSlotId() {
		return timeSlotId;
	}

	public void setTimeSlotId(Long timeSlotId) {
		this.timeSlotId = timeSlotId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(courseId, endDate, id, startDate, studentCount, timeSlotId);
	}
	
	
	

		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchModel other = (BatchModel) obj;
		return Objects.equals(courseId, other.courseId) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(id, other.id) && Objects.equals(startDate, other.startDate)
				&& Objects.equals(studentCount, other.studentCount) && Objects.equals(timeSlotId, other.timeSlotId);
	}



		@Override
	public String toString() {
		return "BatchEntity [id=" + id + ", studentCount=" + studentCount + ", startDate=" + startDate + ", endDate="
				+ endDate + ", courseId=" + courseId + ", timeSlotId=" + timeSlotId + "]";
	}
	

	}
