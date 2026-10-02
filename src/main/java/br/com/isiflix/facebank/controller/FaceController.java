package br.com.isiflix.facebank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isiflix.facebank.dto.FaceDTO;
import br.com.isiflix.facebank.dto.RecognitionDTO;
import br.com.isiflix.facebank.security.IsiToken;
import br.com.isiflix.facebank.service.auth.IAuthService;
import br.com.isiflix.facebank.service.facerecognition.IFaceRecognition;
import br.com.isiflix.facebank.util.FaceMode;

@RestController
@CrossOrigin("*")
public class FaceController {

	@Autowired
	private IFaceRecognition handler;
	
	@Autowired
	private IAuthService auth;

	@PostMapping("/face/register")
	public ResponseEntity<?> registerFace(@RequestBody FaceDTO faceDto) {

		String filename = handler.toImage(faceDto);
		if (handler.cropFace(filename, "-crop.png", FaceMode.REGISTER)) {
			handler.performTraining();
			return ResponseEntity.ok(filename);
		}
		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/face/training")
	public String performFaceTraining() {
		System.out.println("Performing Face training!");
		handler.performTraining();
		return "Ok";
	}

	@PostMapping("/face/recognize")
	public ResponseEntity<IsiToken> recognizeFace(@RequestBody FaceDTO faceDTO) {
		RecognitionDTO confidence = handler.performRecognition(faceDTO);
		IsiToken token = auth.authenticateByFace(confidence);
		if (token != null) {
			return ResponseEntity.ok(token);
		}
		return ResponseEntity.status(403).build();
	}

}