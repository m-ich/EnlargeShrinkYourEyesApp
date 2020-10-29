package application_test;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;



//******************************
//画像処理クラス
//
//処理：
//
//- 検出結果を描画する
//
//******************************


public class MyImageProcessing {


	//******************************
	//フィールド
	//******************************
	private Mat m_imposeImg; //重畳する画像


	//******************************
	//メソッド
	//******************************


	//------------------------------
	//コンストラクタ
	//------------------------------
	public MyImageProcessing()
	{}

	//------------------------------
	//重畳すべき画像を読み込むメソッド
	//@param filePath 画像のファイルパス
	//------------------------------
	public void readImage(String filePath)
	{
		m_imposeImg = Imgcodecs.imread(filePath);		
	}

	
	//------------------------------
	//検出結果の座標位置に四角形を描画するメソッド
	//@param img 描画する画像
	//@param mor 顔領域を示すデータ（左上の(x,y)座標と幅と高さが格納されている）
	//------------------------------
	public void drawDetectionResults(Mat img, MatOfRect mor)
	{
		for(Rect rect : mor.toArray()) {

			Imgproc.rectangle(img,  new Point(rect.x, rect.y),
					new Point (rect.x+rect.width, rect.y+rect.height),
					new Scalar (0,0,255), 5);
		}
	}
	
	
	//------------------------------
	//特定の画像を貼り付けるメソッド
	//@param img 描画対象の画像
	//@param mor 顔領域を示すデータ（左上の(x,y)座標と幅と高さが格納されている）
	//------------------------------
	public void imposeImage(Mat img, MatOfRect mor)
	{
		//検出されたすべての顔画像に対するループ
		for(Rect rect : mor.toArray()) {
			
			//---------------------------
			//1. 重畳する画像のリサイズ
			//---------------------------
			double ratio = 0.1; //拡大/縮小倍率
			Mat resizedImg = new Mat(); 
			Imgproc.resize(m_imposeImg, resizedImg, new Size(0, 0), ratio, ratio);
			
			//---------------------------
			//2. 重畳領域の決定
			//---------------------------
			Rect mrect = new Rect();			
			//検出された顔領域の左上の座標を指定
			mrect.x = rect.x; 
			mrect.y = rect.y;
			//画像サイズは重畳する画像（リサイズ済み）のサイズに指定
			mrect.width = resizedImg.cols();
			mrect.height = resizedImg.rows();
						
			//---------------------------
			//3. 画像の重畳
			//---------------------------

			//重畳する画像がカメラ画像の範囲外にならないようにする
			if(mrect.x + mrect.width < img.cols() && mrect.y+mrect.height <img.rows() )
			{			
				Mat roiImg = img.submat(mrect); //カメラ画像中の重畳領域の指定
				resizedImg.copyTo(roiImg); //その領域に画像を重畳（コピー）
			}
		}
	}
	
	//------------------------------
	//顔画像のモザイク化
	//@param img カメラ画像
	//@param mor 顔領域を示すデータ（左上の(x,y)座標と幅と高さが格納されている）
	//------------------------------
	public void mosaicingImage(Mat img, MatOfRect mor)
	{
		//検出されたすべての顔画像に対するループ
		for(Rect rect : mor.toArray()) {
			
			//---------------------------
			// 1. 顔画像のモザイク化
			//---------------------------
			
			//顔画像領域を抜き出す
			Mat trim = new Mat(img, rect); 

			//解像度を落とす処理（モザイク処理）
			Imgproc.resize(trim, trim, new Size(), 0.1, 0.1, Imgproc.INTER_NEAREST);
			Imgproc.resize(trim, trim, new Size(), 10., 10., Imgproc.INTER_NEAREST);
			
			
			//---------------------------
			//2. モザイク画像の貼り付け
			//---------------------------
			//解像度を落とした顔画像を入力画像にコピー
			for(int j = 0;j<trim.height();j++)
			{
				for(int i = 0;i<trim.width();i++)
				{
					double [] value = trim.get(j,  i);
					img.put(rect.y+j, rect.x+i, value);
				}
			}
			//Mat roiImg = img.submat(rect); //カメラ画像中の重畳領域の指定
			//trim.copyTo(roiImg); //モザイク画像を貼り付け
		}
	}
	
	public void resizeImagebig(Mat img, MatOfRect mor)
	{	
		//検出されたすべての顔画像に対するループ
		for(Rect rect : mor.toArray()) {
			
			/*float scale=0.5f;
			float width=img.width();
			float height=img.height();*/
			
			Mat trim = new Mat(img, rect); 
			Mat dst = new Mat();
			float scale=1.5f;
			float width=trim.width();
			float height=trim.height();
			Imgproc.resize(trim, dst, new Size(width*scale,height*scale));
			
			//Imgcodecs.imwrite("test.jpg",dst);
			
			Rect mrect = new Rect();			
			//検出された顔領域の左上の座標を指定
			mrect.x = (int)(rect.x-width/4); 
			mrect.y = (int)(rect.y-height/4);
			//画像サイズは重畳する画像（リサイズ済み）のサイズに指定
			mrect.width = dst.cols();
			mrect.height = dst.rows();
						
			//---------------------------
			//3. 画像の重畳
			//---------------------------

			//重畳する画像がカメラ画像の範囲外にならないようにする
			if(mrect.x + mrect.width < img.cols() && mrect.y+mrect.height <img.rows() )
			{			
				Mat roiImg = img.submat(mrect); //カメラ画像中の重畳領域の指定
				dst.copyTo(roiImg); //その領域に画像を重畳（コピー）
			}
			
		}
			
	}
		
	public void resizeImagesmall(Mat img, MatOfRect mor)
	{	
		//検出されたすべての顔画像に対するループ
		for(Rect rect : mor.toArray()) {
				
			Mat trim = new Mat(img, rect); 
			Mat dst = new Mat();
			float scale=0.8f;
			float width=trim.width();
			float height=trim.height();
			Imgproc.resize(trim, dst, new Size(width*scale,height*scale));
				
			Rect mrect = new Rect();
			//検出された顔領域の左上の座標を指定
			mrect.x = (int)(rect.x+width/10); 
			mrect.y = (int)(rect.y+height/10);
			//画像サイズは重畳する画像（リサイズ済み）のサイズに指定
			mrect.width = dst.cols();
			mrect.height = dst.rows();

			//重畳する画像がカメラ画像の範囲外にならないようにする
			if(mrect.x + mrect.width < img.cols() && mrect.y+mrect.height <img.rows() )
			{			
				Mat roiImg = img.submat(mrect); //カメラ画像中の重畳領域の指定					
				dst.copyTo(roiImg); //その領域に画像を重畳（コピー）
			}	
		}
	}
}
