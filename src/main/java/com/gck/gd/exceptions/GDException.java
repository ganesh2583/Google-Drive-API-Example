package com.gck.gd.exceptions;

/**
 * 
 * Exception class to handle Feature specific Exceptions.
 * 
 * @author gchaitan
 *
 */
public class GDException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3247495897748562996L;
	
	public GDException(String errorMessage) {
		super(errorMessage);
	}
}
