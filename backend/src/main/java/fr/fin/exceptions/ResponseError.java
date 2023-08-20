package fr.fin.exceptions;

public class ResponseError {

	private Integer status;
	private String message;


	public ResponseError(Integer status, String message) {
		this.status = status;
		this.message = message;
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

}
