package br.com.isiflix.facebank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isiflix.facebank.dto.LoginDTO;
import br.com.isiflix.facebank.security.IsiToken;
import br.com.isiflix.facebank.service.auth.IAuthService;

@RestController
public class AuthController {

	
	@Autowired
	private IAuthService auth;
	
	@PostMapping("/login")
	public ResponseEntity<IsiToken> performLogin(@RequestBody LoginDTO login){
		IsiToken token = auth.authenticate(login.key(), login.senha());
		if (token != null) {
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.status(403).build();
	}
	
}