package application_test;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;


//******************************
//画像から目領域を検出するクラス
//
//用意している処理：
//
//- 目検出
//
//******************************

public class EyeDetection {

	//******************************
	//フィールド
	String m_eye_classifier_path = "/usr/local/Cellar/opencv/4.2.0_1/share/opencv4/haarcascades/haarcascade_eye.xml";
	//******************************
	//none
	
	//******************************
	//メソッド
	//******************************

	//------------------------------
	//コンストラクタ
	//------------------------------
	public EyeDetection() {
	}

	//------------------------------
	//目検出を実行する
	// 検出結果（画像上の座標位置）を返す
	//@param input_img 入力画像
	//------------------------------
	public MatOfRect execEyeDetection(Mat input_img) {

		CascadeClassifier eyeDetector = new CascadeClassifier(m_eye_classifier_path);
		MatOfRect eyeDetections = new MatOfRect();
		eyeDetector.detectMultiScale(input_img, eyeDetections);

		return eyeDetections;
	}
}