package com.app.exec;

public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException() {
		super();
	}

	public ProductNotFoundException(String msg) {
		super(msg);
	}

	public ProductNotFoundException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
