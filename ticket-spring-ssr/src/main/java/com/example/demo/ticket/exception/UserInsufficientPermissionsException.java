package com.example.demo.ticket.exception;

public class UserInsufficientPermissionsException extends CertException {

	public UserInsufficientPermissionsException() {
		super("權限不足!");
	}
	
	public UserInsufficientPermissionsException(String message) {
		super(message);
	}

}
