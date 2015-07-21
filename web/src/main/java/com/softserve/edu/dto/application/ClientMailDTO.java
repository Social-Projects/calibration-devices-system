package com.softserve.edu.dto.application;

public class ClientMailDTO {
	private String verifID;

	private String msg;

	public ClientMailDTO(){}

	public ClientMailDTO(String verifID, String msg) {
		super();
		this.verifID = verifID;
		this.msg = msg;
	}

	public String getVerifID() {
		return verifID;
	}

	public void setVerifID(String verifID) {
		this.verifID = verifID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
