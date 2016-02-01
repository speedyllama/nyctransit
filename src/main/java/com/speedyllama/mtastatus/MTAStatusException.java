package com.speedyllama.mtastatus;

public class MTAStatusException extends Exception {
	private static final long serialVersionUID = 3972627403566108534L;
	
	public MTAStatusException(Exception e) {
		super(e);
	}
}
