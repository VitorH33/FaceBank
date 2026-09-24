package br.com.isiflix.facebank.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.isiflix.facebank.dto.FaceDTO;
import br.com.isiflix.facebank.dto.RecognitionDTO;
import br.com.isiflix.facebank.dto.ResponseDTO;
import br.com.isiflix.facebank.security.IsiToken;
import br.com.isiflix.facebank.service.auth.IAuthService;
import br.com.isiflix.facebank.service.facerecognition.IFaceRecognition;
import br.com.isiflix.facebank.util.FaceMode;

@RestController
@CrossOrigin("*")
public class FaceController {

	private final IFaceRecognition faceRecognition;
	private final IAuthService authService;
	private final String tempFolder;
	private final String cropFolder;

	public FaceController(IFaceRecognition faceRecognition, IAuthService authService,
			@Value("${io.isiflix.tempfolder}") String tempFolder,
			@Value("${io.isiflix.cropfaces}") String cropFolder) {
		this.faceRecognition = faceRecognition;
		this.authService = authService;
		this.tempFolder = tempFolder;
		this.cropFolder = cropFolder;
	}

	@PostMapping("/face/register")
	public ResponseEntity<ResponseDTO> registerFace(@RequestBody FaceDTO face) {
		if (face == null || face.getIdCliente() == null || face.getData() == null || face.getData().isBlank()) {
			return ResponseEntity.badRequest().body(new ResponseDTO("Informe o id do cliente e a imagem em Base64"));
		}

		try {
			Files.createDirectories(Path.of(tempFolder));
			Files.createDirectories(Path.of(cropFolder));
			String imagePath = faceRecognition.toImage(face);
			if (imagePath == null) {
				return ResponseEntity.badRequest().body(new ResponseDTO("Não foi possível ler a imagem enviada"));
			}
			boolean faceFound;
			try {
				faceFound = faceRecognition.cropFace(imagePath, "-crop.png", FaceMode.REGISTER);
			} finally {
				Files.deleteIfExists(Path.of(imagePath));
			}
			if (!faceFound) {
				return ResponseEntity.badRequest().body(new ResponseDTO("Não foi possível detectar uma face na imagem"));
			}
			return ResponseEntity.ok(new ResponseDTO("Face cadastrada. Treine o modelo para habilitar o reconhecimento."));
		} catch (IOException | RuntimeException exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO("Falha ao salvar a imagem facial"));
		}
	}

	@GetMapping("/face/training")
	public ResponseEntity<ResponseDTO> trainFaces() {
		try {
			faceRecognition.performTraining();
			return ResponseEntity.ok(new ResponseDTO("Modelo facial treinado com sucesso"));
		} catch (RuntimeException exception) {
			return ResponseEntity.badRequest().body(new ResponseDTO("Não foi possível treinar o modelo facial"));
		}
	}

	@PostMapping("/face/recognize")
	public ResponseEntity<?> recognizeFace(@RequestBody FaceDTO face) {
		if (face == null || face.getData() == null || face.getData().isBlank()) {
			return ResponseEntity.badRequest().body(new ResponseDTO("Informe a imagem em Base64"));
		}

		try {
			RecognitionDTO recognition = faceRecognition.performRecognition(face);
			if (recognition == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ResponseDTO("Face não reconhecida. Tente novamente."));
			}
			IsiToken token = authService.authenticateByFace(recognition);
			if (token == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new ResponseDTO("Face não reconhecida. Tente novamente."));
			}
			return ResponseEntity.ok(token);
		} catch (RuntimeException exception) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ResponseDTO("Não foi possível autenticar esta face"));
		}
	}
}
