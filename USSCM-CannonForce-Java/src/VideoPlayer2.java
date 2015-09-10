import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.humble.video.awt.ImageFrame;

public class VideoPlayer2 {

	
	final private static int FPS = 25;
	
	private static void playVideo(String filename, ImageFrame window, int numFrames) throws InterruptedException, IOException {

		filename = filename + "-i";
		
		System.out.println(filename);

	        int i = 0;
	        while(true){
	        	
	        	System.out.println(i);
	        	i+=1;
		        BufferedImage b = ImageIO.read(new File(filename + "/" + String.format("%05d", i) + ".png"));
		        
		        window.setImage(getScaledImage(b, window.getWidth(),window.getHeight()));
		        //window.setImage(b);
		        
		        Thread.sleep((long) (1000.0 / FPS));
	        }

		
		//window.setImage(getScaledImage(frame, window.getWidth(),window.getHeight()));
		
	}

//	public static BufferedImage toBitmap(Picture pic) {
//		BufferedImage dst = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//		  toBufferedImage(pic, dst);
//		  return dst;
//		}
//
//		public static void toBufferedImage(Picture src, BufferedImage dst) {
//		  int[][] srcData = src.getData();
////		  int[] packed = new int[src.getWidth() * src.getHeight()];
//
//		  
//		  for (int i = 0; i < srcData.length; i++) {
//		      for (int j = 0; j < srcData[0].length; j+=3) {
//		    	  dst.setRGB(i, j, (srcData[i][j] << 16) | (srcData[i][j + 1] << 8) | srcData[i][j + 2]);
//		      }
//		  }
//		  
////		  for (int i = 0, dstOff = 0, srcOff = 0; i < src.getHeight(); i++) {
////		      for (int j = 0; j < src.getWidth(); j++, dstOff++, srcOff += 3) {
////		    	  dst.setRGB(i, j, (srcData[srcOff] << 16) | (srcData[srcOff + 1] << 8) | srcData[srcOff + 2]);
////		      }
////		  }
//		  
//		  
////		  for (int i = 0, dstOff = 0, srcOff = 0; i < src.getHeight(); i++) {
////		      for (int j = 0; j < src.getWidth(); j++, dstOff++, srcOff += 3) {
////		          packed[dstOff] = (srcData[srcOff] << 16) | (srcData[srcOff + 1] << 8) | srcData[srcOff + 2];
////		      }
////		  }
//		  //dst.setPixels(packed, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());
//		  //dst.setRGB(0, 0, src.getWidth(), src.getHeight(), packed, 0, 0);
//		}
//	
	
	
  public static void main(String[] args) throws InterruptedException, IOException
  {
	  
	  
	   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	   
	   GraphicsDevice gd = ge.getScreenDevices()[0];

	   final ImageFrame window = ImageFrame.make();
	   
	   //gd.setFullScreenWindow(window);

	   window.setSize(800, 640);
	   
	   setBG("res/image/background-loading.jpg", window);
	   
	   Thread.sleep(1000);
	   
	   setBG("res/image/background-hwerror.jpg", window);
	   
	   for (int i = 1; i <= 85; i++) {
		   
		   //try{
			   playVideo("res/video/mp4s/" + String.format("%02d", i) + ".mp4", window, 9999);
//		   }catch (Exception e) {
//			   e.printStackTrace();
//		   }
	   }
	   
	   setBG("res/image/background.jpg", window);
       
  }

  public static void setBG(String filename, ImageFrame window) throws IOException, InterruptedException{
	  
	  BufferedImage image = ImageIO.read(new File(filename));

	  window.setImage(getScaledImage(image, window.getWidth(),window.getHeight()));
  }
  

  
  public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}

}