package br.com.isiflix.facebank.service.facerecognition;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imdecode;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.isiflix.facebank.dto.FaceDTO;
import br.com.isiflix.facebank.dto.RecognitionDTO;
import br.com.isiflix.facebank.util.FaceMode;
import jakarta.xml.bind.DatatypeConverter;

@Service
public class FaceRecognitionImpl implements IFaceRecognition {

	@Value("${io.isiflix.tempfolder}")
	private String tmpFolder;

	@Value("${io.isiflix.cropfaces}")
	private String cropFolder;

	@Value("${io.isiflix.recognitionfaces}")
	private String recognitionFolder;

	@Override
	public String toImage(FaceDTO faceData) {
		// TODO Auto-generated method stub
		try {
			String filename = tmpFolder + File.separator + faceData.getIdCliente() + ".png";
			byte[] imageData = DatatypeConverter.parseBase64Binary(faceData.getData());
			File newImage = new File(filename);
			OutputStream output = new BufferedOutputStream(new FileOutputStream(newImage));
			output.write(imageData);
			output.close();
			return filename;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean cropFace(String filename, String extension, int mode) {
		// TODO Auto-generated method stub
		try {
			CascadeClassifier classifier = new CascadeClassifier();
			classifier.load("haarcascade_frontalface_default.xml");

			Mat image = imread(filename, IMREAD_GRAYSCALE);
			if (image != null) {
				RectVector faces = new RectVector();
				classifier.detectMultiScale(image, faces);
				System.out.println("Faces detected = " + faces.size());
				if (faces.size() > 0) {
					Mat croppedFace = new Mat(image, faces.get(0));
					String newFile = filename.replace(tmpFolder,
							(mode == FaceMode.REGISTER) ? cropFolder : recognitionFolder);
					imwrite(newFile + extension, croppedFace);
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public void performTraining() {
		FaceRecognizer recognizer = LBPHFaceRecognizer.create();

		File dir = new File(cropFolder);
		File[] images = dir.listFiles();

		for (File f : images) {
			System.out.println("Face Found = " + f.getName());
		}

		MatVector photos = new MatVector(images.length); 
		Mat labels = new Mat(images.length, 1, opencv_core.CV_32SC1); 
		int contador = 0;
		IntBuffer bufferLabels = labels.createBuffer();
		int personId = 0;

		for (File currentImage : images) {
			Mat photo = opencv_imgcodecs.imread(cropFolder + File.separator + currentImage.getName(),
					opencv_imgcodecs.IMREAD_GRAYSCALE);
			personId = Integer.parseInt(currentImage.getName().replace(".png-crop.png", ""));
			System.out.println("Found PersonID = " + personId);

			bufferLabels.put(contador, personId);
			opencv_imgproc.resize(photo, photo, new Size(160, 160));
			photos.put(contador, photo);
			contador++; 
		}

		File f = new File("LBPHTraining.yml");
//		if (f != null) {
//			System.out.println("Training file Exists!");
//			recognizer.read("LBPHTraining.yml");
//		}
		recognizer.train(photos, labels);
		recognizer.save("LBPHTraining.yml");
		System.out.println("Complete Training");
	}

	@Override
	public RecognitionDTO performRecognition(FaceDTO data) {
		double finalConfidence = 0;
		try {
			byte[] imgbytes = DatatypeConverter.parseBase64Binary(data.getData());
			Mat currentFace = imdecode(new Mat(imgbytes), IMREAD_GRAYSCALE);
			Mat detectedFace = null;
			
			System.out.println("DEBUG - Face received!");
	

			CascadeClassifier classifier = new CascadeClassifier();
			classifier.load("haarcascade_frontalface_default.xml");
			RectVector faces = new RectVector();
			classifier.detectMultiScale(currentFace, faces);
			System.out.println("HERE - Faces detected = " + faces.size());
			if (faces.size() > 0) {
				detectedFace = new Mat(currentFace, faces.get(0));
			}
			if (detectedFace == null) {
				return null;
			}

			opencv_imgproc.resize(detectedFace, detectedFace, new Size(160, 160));

			FaceRecognizer recognizer = LBPHFaceRecognizer.create();
			recognizer.read("LBPHTraining.yml");
			recognizer.setThreshold(50);
			DoublePointer confidence = new DoublePointer(1);
			IntPointer labels = new IntPointer(1);
			
			recognizer.predict(detectedFace, labels, confidence);
			finalConfidence = confidence.get(0) * 100 / 100;
			//System.out.println("Final confidence = " + finalConfidence);
			//System.out.println("Confidence to face = " + confidence.get(0) + " and label = " + labels.get(0));
			if (labels.get(0) > 0) {
				return new RecognitionDTO(finalConfidence, labels.get(0));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		// TODO Auto-generated method stub
		return null;
	}
}
