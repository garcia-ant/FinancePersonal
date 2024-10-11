package com.gposter.minhasfinancas.exception;

public class ErrorAuthentication extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public  ErrorAuthentication(String msg) {
		super(msg);
	}
}
