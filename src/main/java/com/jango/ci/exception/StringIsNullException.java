package com.jango.ci.exception;

public class StringIsNullException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param aString
	 */
	public StringIsNullException(String aString) {
		super("The string is null:"+aString.getClass());
	}
}
