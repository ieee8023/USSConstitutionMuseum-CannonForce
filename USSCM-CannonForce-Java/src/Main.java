import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice gd = ge.getScreenDevices()[0];

		final ImageFrame window = ImageFrame.make();

		gd.setFullScreenWindow(window);
		
		VideoPlayer.setBG("res/image/background-loading.jpg", window);

		VideoPlayer.wakeScreen(window);
		
		//Thread.sleep(1000);

		new CanonForce().listen(new CannonCallback() {
			
			@Override
			public void callback(HardwareValues values) {

				try {
					VideoPlayer.wakeScreen(window);
					VideoPlayer.playVideo("res/video/mp4s/" + String.format("%02d", 1) + ".mp4");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		

		VideoPlayer.setBG("res/image/background.jpg", window);
		
		
		
	}

}
