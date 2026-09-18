package br.com.isiflix.facebank.exceptions;

public class NotEnoughBalanceException extends RuntimeException{
	public NotEnoughBalanceException(String message) {
		super(message);
	}

}