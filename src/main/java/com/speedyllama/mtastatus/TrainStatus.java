package com.speedyllama.mtastatus;

public enum TrainStatus {
	GOOD_SERVICE("GOOD SERVICE"), 
	DELAYS("DELAYS"), 
	SERVICE_CHANGE("SERVICE CHANGE"), 
	PLANNED_WORK("PLANNED WORK"),
	UNKNOWN("UNKNOWN");

	private String statusString;
	private TrainStatus(String statusString) {
		this.statusString = statusString;
	}
	
	public String toString() {
		return this.statusString;
	}
	
	public static TrainStatus parse(String statusString) {
		for (TrainStatus status : TrainStatus.values()) {
			if (status.statusString.equals(statusString.trim().toUpperCase())) {
				return status;
			}
		}
		return TrainStatus.UNKNOWN;
	}
}
