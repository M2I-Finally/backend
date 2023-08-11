package fr.fin.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.fin.auth.UnauthorizedType;

public class ResponseError {

	private Integer status;
	private String message;

	@JsonInclude(Include.NON_NULL)
	private UnauthorizedType unauthorizedType;

	public ResponseError(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public ResponseError(Integer status, String message, UnauthorizedType unauthorizedType) {
		this.status = status;
		this.message = message;
		this.unauthorizedType = unauthorizedType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UnauthorizedType getUnauthorizedType() {
		return unauthorizedType;
	}

	public void setUnauthorizedType(UnauthorizedType unauthorizedType) {
		this.unauthorizedType = unauthorizedType;
	}

}
