package com.learning.model;

import java.time.LocalTime;
import java.util.Objects;

public class TimeSlotModel {

	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

	public TimeSlotModel() {
	}

	public TimeSlotModel(Long id, LocalTime startTime, LocalTime endTime, Long trainerId) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.trainerId = trainerId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public Long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Long trainerId) {
		this.trainerId = trainerId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(endTime, id, startTime, trainerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSlotModel other = (TimeSlotModel) obj;
		return Objects.equals(endTime, other.endTime) && Objects.equals(id, other.id)
				&& Objects.equals(startTime, other.startTime) && Objects.equals(trainerId, other.trainerId);
	}

	@Override
	public String toString() {
		return "TimeSlotModel [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", trainerId="
				+ trainerId + "]";
	}
	
	

}
