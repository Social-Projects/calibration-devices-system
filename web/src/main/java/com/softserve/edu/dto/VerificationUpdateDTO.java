package com.softserve.edu.dto;

import java.util.List;

public class VerificationUpdateDTO {

	private List<String> idsOfVerifications;
	private Long organizationId;
	private String message;

	public List<String> getIdsOfVerifications() {
		return idsOfVerifications;
	}

	public void setIdsOfVerifications(List<String> idsOfVerifications) {
		this.idsOfVerifications = idsOfVerifications;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage()
	{
		return this.message;
	}
}
