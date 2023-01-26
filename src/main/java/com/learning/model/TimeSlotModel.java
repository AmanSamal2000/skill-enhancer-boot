package com.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotModel {

	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Long trainerId;

	}
