package EyeTracker.java;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class EyeTracker extends JFrame{
	private JLabel cameraScreen;
	private JButton btnCapture;
	
	private VideoCapture cap;
	private Mat frame;
	
	public EyeTracker() {
		setLayout(null);
		
		cameraScreen = new JLabel();
		cameraScreen.setBounds(0,0,640,480);
		add(cameraScreen);
		
		
		
		setSize(new Dimension(640,560));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	public void startCamera() {
		cap = new VideoCapture(0);
		frame = new Mat();
		byte[] imageData;
		ImageIcon icon;
		CascadeClassifier cascadeFaceClassifier = new CascadeClassifier(
				"C:\\Users\\orkun\\Downloads\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_default.xml");
		CascadeClassifier cascadeEyeClassifier = new CascadeClassifier(
				"C:\\Users\\orkun\\Downloads\\opencv\\build\\etc\\haarcascades\\haarcascade_eye.xml");
		
		CascadeClassifier cascadeNoseClassifier = new CascadeClassifier(
				"C:\\Users\\orkun\\Downloads\\opencv\\build\\etc\\haarcascades\\haarcascade_mcs_nose.xml");
		while(true) {
			cap.read(frame);
			MatOfRect faces = new MatOfRect();
			cascadeFaceClassifier.detectMultiScale(frame, faces);
			Rect biggestFace = new Rect();
			boolean faceFound = false;
			for (Rect rect : faces.toArray()) {
				if(faceFound==false) {
					faceFound = true;
				}
				if((biggestFace.width + biggestFace.height)<(rect.width + rect.height)) {
					biggestFace = rect;
				}
				
				
				
				
			}
			
			Imgproc.putText(frame, "Face", new Point(biggestFace.x,biggestFace.y-5), 1, 2, new Scalar(0,0,255));								
			Imgproc.rectangle(frame, new Point(biggestFace.x, biggestFace.y), new Point(biggestFace.x + biggestFace.width, biggestFace.y + biggestFace.height),
					new Scalar(0, 100, 0),3);
			
			
			Mat faceMat = new Mat (frame,biggestFace);
			MatOfRect eyes = new MatOfRect();
			cascadeEyeClassifier.detectMultiScale(faceMat, eyes);
			for (Rect recteye : eyes.toArray()) {
				Imgproc.putText(faceMat, "Eye", new Point(recteye.x,recteye.y-5), 1, 2, new Scalar(0,0,255));				
				Imgproc.rectangle(faceMat, new Point(recteye.x, recteye.y), new Point(recteye.x + recteye.width, recteye.y + recteye.height),
						new Scalar(200, 200, 100),2);
				if (recteye.x < 20 && recteye.y < 20) {
					System.out.println("come closer");
					}
					if (recteye.x > 100 && recteye.x > 100) {
					System.out.println("go farther");
					}
			}			
				
			
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", frame, buf);
			imageData = buf.toArray();
			icon = new ImageIcon(imageData);
			cameraScreen.setIcon(icon);
		}
	}
	
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable(){
        	public void run() {
        		EyeTracker eyeTracker = new EyeTracker();
        		
        		new Thread(new Runnable() {
        			@Override
        			public void run() {
        				eyeTracker.startCamera();
        			}
        		}).start();
        	}
        });
	}

}