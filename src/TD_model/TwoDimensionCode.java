package TD_model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

import jp.sourceforge.qrcode.QRCodeDecoder;

public class TwoDimensionCode {
	
	public void encoderQRCode(String content,String imgPath){
		this.encoderQRCode(content, imgPath,"png",7);
	}
  public void encoderQRCode(String content,OutputStream output){
	  this.encoderQRCode(content, output,"png",7);
  }
  public void encoderQRCode(String content,String imaPath,String imgType){
	  this.encoderQRCode(content, imaPath, imgType,7);
  }
  public void encoderQRCode(String content,OutputStream output,String imgType){
	  this.encoderQRCode(content, output, imgType,7);
  }
  public void encoderQRCode(String content,String imgPath,String imgType,int size){
	  try {
		BufferedImage bufImg = this.qRCodeCommon(content,imgType,size);
		File imgFile = new File(imgPath);
		ImageIO.write(bufImg, imgType, imgFile);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  public void encoderQRCode(String content,OutputStream output,String imgType,int size){
	  try {
		BufferedImage bufImg = this.qRCodeCommon(content,imgType,size);
		ImageIO.write(bufImg, imgType, output);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  
  private BufferedImage qRCodeCommon(String content,String imgType,int size){
	  BufferedImage bufImg = null;
	  try {
		Qrcode qrcodeHandler = new Qrcode();
	//  设置二维码排错率,可选L(7%) 、M(15%) 、Q(25%) 、H(30%) ,排错率越高可存储的信息越少,但对二维码清晰度的要求越小
		qrcodeHandler.setQrcodeErrorCorrect('M');
		qrcodeHandler.setQrcodeEncodeMode('B');
		qrcodeHandler.setQrcodeVersion(size);//二维码尺寸
		byte[] contentBytes = content.getBytes("utf-8");
		int imgSize = 67 + 12 * (size - 1);
		bufImg  = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D gs = bufImg.createGraphics();
		gs.setBackground(Color.WHITE);
		gs.clearRect(0, 0, imgSize, imgSize);
		gs.setColor(Color.BLACK);
		int pixoff = 2;
		if(contentBytes.length > 0 && contentBytes.length < 800){
			boolean [][] codeOut = qrcodeHandler.calQrcode(contentBytes);
			for (int i = 0; i < codeOut.length; i++){
				for (int j = 0; j < codeOut.length; j++){
					if (codeOut[i][j]){
						gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
					}
				}
			}
		} else{
			throw new Exception("QRCode content bytes length = " + contentBytes.length + "not in [0,800].") ;
		}
		gs.dispose();
		bufImg.flush();
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	  return bufImg;
  }
  
  public String decoderQRCode (String imgPath){
	  File imageFile  = new File(imgPath);
	  BufferedImage bufImg  = null;
	  String content  = null;
	  try {
		  bufImg  = ImageIO.read(imageFile);
		  QRCodeDecoder decoder = new QRCodeDecoder();
		  content  = new  String (decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");//来自二维码图像类
		
	} catch (Exception e) {
		System.out.println("Error:" + e.getMessage());
		e.printStackTrace();
	}
//	  catch (DecodingFailedException dfe) {
//		System.out.println("Error:" + dfe.getMessage());
//		dfe.printStackTrace();
//	}  //DecodingFailedException 有错
	  
	  return content;
  }
  
  public String decoderQRCode(InputStream input){
	  BufferedImage bufImg = null;
	  String content = null;
	  try {
		bufImg = ImageIO.read(input);
		QRCodeDecoder decoder = new QRCodeDecoder();
		content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)),"uft-8");
	} catch (Exception e) {
		System.out.println("Error:" + e.getMessage());
		e.printStackTrace();
	}
//	  catch (DecodingFailedException dfe) {
//		System.out.println("Error:" + dfe.getMessage());
//		dfe.printStackTrace();
//	}  //DecodingFailedException 有错  
	  return content;
  }
  
  public static void main (String[] args){
	  String imgPath = "Michael_QRCode.png";
	  String encoderContent = "Who am i?";
	  //11223344556677889911223344556677889911223344556677889
	  TwoDimensionCode handler = new TwoDimensionCode();
	  handler.encoderQRCode(encoderContent, imgPath,"png");
	  System.out.println("===========encoder success");
	  String decoderContent = handler.decoderQRCode(imgPath);
	  System.out.println("===========解析结果如下：");
	  System.out.println(decoderContent);
	  System.out.println("===========decoder success");
  }
  
}
