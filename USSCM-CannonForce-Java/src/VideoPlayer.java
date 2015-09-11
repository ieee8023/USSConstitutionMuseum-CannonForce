import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class VideoPlayer {

	static void playVideo(String filename) throws InterruptedException, IOException {

		Process p = Runtime.getRuntime().exec("omxplayer " + filename);

		p.waitFor(10, TimeUnit.SECONDS);
		if (p.isAlive())
			p.destroyForcibly();
	}

	public static void main(String[] args) throws InterruptedException, IOException, AWTException {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice gd = ge.getScreenDevices()[0];

		final ImageFrame window = ImageFrame.make();

		gd.setFullScreenWindow(window);

		// window.setSize(800, 640);

		setBG("res/image/background-loading.jpg", window);

		wakeScreen(window);
		
		Thread.sleep(1000);

		setBG("res/image/background.jpg", window);

		// setBG("res/image/background-hwerror.jpg", window);

		for (int i = 1; i <= 85; i++) {

			playVideo("res/video/mp4s/" + String.format("%02d", i) + ".mp4");
		}

		setBG("res/image/background.jpg", window);

	}

	public static void wakeScreen(ImageFrame window) throws AWTException, InterruptedException{
		
        Robot robot = new Robot();
        
        // to the left
        robot.mouseMove(0, window.getHeight());
        Thread.sleep(1);
        // to the left
        robot.mouseMove(window.getWidth(), window.getHeight());
		
	}
	
	
	public static void setBG(String filename, ImageFrame window) throws IOException, InterruptedException {

		BufferedImage image = ImageIO.read(new File(filename));

		window.setImage(getScaledImage(image, window.getWidth(), window.getHeight()));
	}

	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

}