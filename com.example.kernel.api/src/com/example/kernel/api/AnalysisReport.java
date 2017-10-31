package com.example.kernel.api;

public class AnalysisReport {


	private float occupancy;
	private String message;
	
	
	public AnalysisReport() {
		this.occupancy = 0;
		this.message = "";
	}
	public float getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(float occupancy) {
		this.occupancy = occupancy;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
		
	}
	
	
}
