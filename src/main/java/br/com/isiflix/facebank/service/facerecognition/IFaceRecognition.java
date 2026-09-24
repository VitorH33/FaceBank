package br.com.isiflix.facebank.service.facerecognition;

import br.com.isiflix.facebank.dto.FaceDTO;
import br.com.isiflix.facebank.dto.RecognitionDTO;

public interface IFaceRecognition {
	public String toImage(FaceDTO faceData);
	public boolean cropFace(String filename, String extension, int mode);
	public void performTraining();
	public RecognitionDTO performRecognition(FaceDTO data);
}