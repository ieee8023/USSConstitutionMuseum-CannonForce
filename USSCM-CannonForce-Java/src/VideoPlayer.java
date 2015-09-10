import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.humble.video.Decoder;
import io.humble.video.Demuxer;
import io.humble.video.DemuxerStream;
import io.humble.video.MediaDescriptor;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.awt.ImageFrame;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

public class VideoPlayer {

	
	final private static int FPS = 20;
	
	private static void playVideo(String filename, ImageFrame window, int numFrames) throws InterruptedException, IOException {

		System.out.println(filename);
		
		Demuxer demuxer = Demuxer.make();
		demuxer.open(filename, null, false, true, null, null);
		int numStreams = demuxer.getNumStreams();

		int videoStreamId = -1;
		Decoder videoDecoder = null;
		for (int i = 0; i < numStreams; i++) {
			final DemuxerStream stream = demuxer.getStream(i);

			final Decoder decoder = stream.getDecoder();
			if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_VIDEO) {
				videoStreamId = i;
				videoDecoder = decoder;
				// stop at the first one.
				break;
			}
		}

		if (videoStreamId == -1)
			throw new RuntimeException("could not find video stream in container: " + filename);

		videoDecoder.open(null, null);

		final MediaPicture picture = MediaPicture.make(videoDecoder.getWidth(), videoDecoder.getHeight(),
				videoDecoder.getPixelFormat());

		final MediaPictureConverter converter = MediaPictureConverterFactory.createConverter(MediaPictureConverterFactory.HUMBLE_BGR_24, picture);
		BufferedImage image = null;

		final MediaPacket packet = MediaPacket.make();
		while (demuxer.read(packet) >= 0 && numFrames != 0) {

			if (packet.getStreamIndex() == videoStreamId) {
				int offset = 0;
				int bytesRead = 0;
				do {
					bytesRead += videoDecoder.decode(picture, packet, offset);
					if (picture.isComplete()) {

						numFrames--;
						
						Thread.sleep((long) (1000.0 / FPS));
						// finally, convert the image from Humble format into
						// Java images.
						image = converter.toImage(image, picture);
						// And ask the UI thread to repaint with the new image.
						
						//Image scaled = image.getScaledInstance(1000, 1000, 0);
						
						
						
						window.setImage(getScaledImage(image,window.getWidth(),window.getHeight()));
						
						//window.setImage(image);

					}
					offset += bytesRead;
				} while (offset < packet.getSize());
			}
		}

		demuxer.close();
		//window.dispose();
	}

  public static void main(String[] args) throws InterruptedException, IOException
  {
	  
	  
	   GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	   
	   GraphicsDevice gd = ge.getScreenDevices()[0];

	   final ImageFrame window = ImageFrame.make();
	   
	   gd.setFullScreenWindow(window);

	   //window.setSize(800, 640);
	   
	   setBG("image/background-loading.jpg", window);
	   
	   Thread.sleep(5000);
	   
	   setBG("image/background.jpg", window);
	   
	   for (int i = 1; i <= 85; i++) {
		   
		   try{
			   playVideo("video/mp4s/" + String.format("%02d", i) + ".mp4", window, 9999);
		   }catch (Exception e) {
			   e.printStackTrace();
		   }
	   }
	   
	   setBG("image/background-hwerror.jpg", window);
       
       
       //playVideo("video/mp4s/50.mp4", window, 2);
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