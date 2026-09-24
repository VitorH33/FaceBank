package br.com.isiflix.facebank.service.auth;

import br.com.isiflix.facebank.dto.RecognitionDTO;
import br.com.isiflix.facebank.security.IsiToken;

public interface IAuthService {
	public IsiToken authenticate(String key, String password);
	public IsiToken authenticateByFace(RecognitionDTO faceData);
}