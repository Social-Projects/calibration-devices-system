package com.softserve.edu.dto;

public class ArchiveVerificationsFilterAndSort {
	
	private String id;
	private String date;
	private String endDate;
	private String client_full_name;
	private String street;
	private String region;
	private String district;
	private String locality;
	private String status;
	private String employee_last_name;

	private Long protocol_id;
	private String protocol_status;

	private Long measurement_device_id;
	private String measurement_device_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmployee_last_name() {
		return employee_last_name;
	}
	public void setEmployee_last_name(String employee_last_name) {
		this.employee_last_name = employee_last_name;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getClient_full_name() {
		return client_full_name;
	}

	public void setClient_full_name(String client_full_name) {
		this.client_full_name = client_full_name;
	}

	public Long getMeasurement_device_id() {
		return measurement_device_id;
	}

	public void setMeasurement_device_id(Long measurement_device_id) {
		this.measurement_device_id = measurement_device_id;
	}

	public String getMeasurement_device_type() {
		return measurement_device_type;
	}

	public void setMeasurement_device_type(String measurement_device_type) {
		this.measurement_device_type = measurement_device_type;
	}

	public Long getProtocol_id() {
		return protocol_id;
	}

	public void setProtocol_id(Long protocol_id) {
		this.protocol_id = protocol_id;
	}

	public String getProtocol_status() {
		return protocol_status;
	}

	public void setProtocol_status(String protocol_status) {
		this.protocol_status = protocol_status;
	}
}
