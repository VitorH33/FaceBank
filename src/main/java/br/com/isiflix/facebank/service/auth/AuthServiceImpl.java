package br.com.isiflix.facebank.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.isiflix.facebank.dto.RecognitionDTO;
import br.com.isiflix.facebank.model.Cliente;
import br.com.isiflix.facebank.repo.ClienteRepo;
import br.com.isiflix.facebank.security.IsiToken;
import br.com.isiflix.facebank.security.TokenUtil;

@Service
public class AuthServiceImpl implements IAuthService{

	@Autowired
	private ClienteRepo repo;
	
	@Override
	public IsiToken authenticate(String key, String password) {
		// TODO Auto-generated method stub
		Cliente c = repo.findByEmailOrCpfOrTelefone(key, key, key);
		if (c != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(password, c.getSenha())) {
				IsiToken token =TokenUtil.encode(c);
				return token;
			}
		}
		return null;
	}

	@Override
	public IsiToken authenticateByFace(RecognitionDTO faceData) {
		// LBPH returns a distance: a lower value means a closer match.
		if (faceData != null && faceData.confidence() < 50.0 && faceData.label() > 0) {
			Cliente c = repo.findById(faceData.label()).orElse(null);
			if (c != null) {
				return TokenUtil.encode(c);
			}
		}
		return null;
	}

}
