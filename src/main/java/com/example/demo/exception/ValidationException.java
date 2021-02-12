package com.example.demo.exception;

public class ValidationException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8030513685513404551L;
	private String message;

    public ValidationException(String message) {
    }

    public String getMessage() {
        return message;
    }
}
