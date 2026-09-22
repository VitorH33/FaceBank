package br.com.isiflix.facebank.security;

public class IsiToken {
	private String token;

	public IsiToken(String token) {
		super();
		this.token = token;
	}

	public IsiToken() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}