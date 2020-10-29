package application_test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class FaceDetectionDemo {

	public static void main (String[] args) {
		
		//boolean isBigEyes =false;

		//*****************************
		// モード選択をキーボードからの入力で選択する
		//*****************************
		System.out.println("モード選択をしてください。(big or small)");

		String buf = "n";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try {
			buf = br.readLine();
			System.out.println("input key is " + buf);
		}
		catch(IOException e) {
			System.out.println("Quit program");
			System.exit(0);
		}

		//*****************************
		//処理開始
		//*****************************

		//OpenCVを使うために必ず入れる一行
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//映像取得モジュールのインスタンス化
		VideoCaptureModule vcm = new VideoCaptureModule(buf);

		//顔検出モジュールのインスタンス化
		FaceDetection fd = new FaceDetection();
		
		//目検出モジュールのインスタンス化
		EyeDetection ed = new EyeDetection();

		//画像処理モジュールのインスタンス化
		MyImageProcessing mip = new MyImageProcessing();

/*
//--------------------------------------------------------------------
// 一枚の画像を読み込んで処理する例
//--------------------------------------------------------------------
		Mat input = Imgcodecs.imread("/Users/megu/eclipse-workspace/application-test2/src/application_test/riko1.jpg"); //画像を読み込む処理。引数はファイルパス
		MatOfRect res = ed.execEyeDetection(input); //読み込んだ画像に対し顔検出を実行
		//mip.drawDetectionResults(input, res); //結果を描画
		mip.resizeImage(input, res);
		
		//mip.mosaicingImage(input, res);
		vcm.saveImage(input); //結果を描画した画像を保存
*/
		
//--------------------------------------------------------------------
// ビデオ処理の例
//--------------------------------------------------------------------

		//例1: 別の画像を顔領域に貼り付ける場合
		//mip.readImage("image.jpg"); //顔領域に貼り付ける画像の読み込み. 引数は貼り付けたい画像のファイルパス。

		//映像取得開始
		//カメラが開いている間処理を続ける
		while(vcm.isCameraOpened())
		{
			Mat image = vcm.getFrameFromCamera(); //カメラ映像から画像を一枚取り出す

			//---------------------------
			//カメラ画像の縮小（処理高速化のため）
			//---------------------------
			double ratio = 0.5; //入力画像を縮小する割合
			Imgproc.resize(image, image, new Size(0, 0), ratio, ratio);

			//---------------------------
			//顔検出の実行
			//---------------------------
			MatOfRect mor = ed.execEyeDetection(image);

			//---------------------------
			//顔領域に対する画像処理
			//---------------------------
			//mip.drawDetectionResults(image, mor); //顔位置に矩形を描画
			//mip.imposeImage(image, mor); //例1: 顔領域への別の画像の重畳
			//mip.mosaicingImage(image, mor); //例2: 顔領域のモザイク処理
			if(buf.equals("big")) {
				mip.resizeImagebig(image, mor);
			}else if(buf.equals("small")){ 
				mip.resizeImagesmall(image, mor);
			}
			vcm.showImage(image); //取り込んだ画像を表示

			vcm.saveVideo(image); //ビデオ保存（一枚ずつビデオに追加していく）

			//キーボード入力の取得
			//表示している画面をアクティブにするとキー入力を受け付ける
			int key = vcm.getInputKey();

			//key==81 というのは、'q'を押したことを意味（参考：アスキーコード）
			//qが押されたらwhileループを抜ける
			if(key == 81)
				break;

			else if (key == 73) //"i"が押されたら画像保存
				vcm.saveImage(image);
		}
		//映像取得を終了する（プログラムを終了する）メソッド
		vcm.stopVideoCapture();	
	}
	
}
