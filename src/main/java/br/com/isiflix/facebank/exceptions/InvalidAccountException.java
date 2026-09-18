package br.com.isiflix.facebank.exceptions;

public class InvalidAccountException extends RuntimeException{
	public InvalidAccountException(String message) {
		super(message);
	}

}