import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
public class Main {

	public static void main(String[] args) {
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice gd = ge.getScreenDevices()[0];

		final ImageFrame window = ImageFrame.make();

		gd.setFullScreenWindow(window);
		
		VideoPlayer.setBG("res/image/background-loading.jpg", window);

		VideoPlayer.wakeScreen(window);

		final SoundPlayer soundPlayer = new SoundPlayer("res/sound/boom.mp3");
		
		
		
		final CannonForce cf = new CannonForce();
		
		cf.listen(new CannonCallback() {
			
			@Override
			public void callback(HardwareValues values) {

					VideoPlayer.wakeScreen(window);
					soundPlayer.playSound(false);
					VideoPlayer.playVideo("res/video/mp4s/" + String.format("%02d", getVideo(values)) + ".mp4");
				
			}
		});
		
    	ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    	exec.scheduleAtFixedRate(new Runnable() {
    	  @Override
    	  public void run() {
    		  
    		  cf.writeDistance();
    		  
    		  
    	  }
    	}, 0, 50, TimeUnit.MILLISECONDS);
    	
    	exec.scheduleAtFixedRate(new Runnable() {
    	  @Override
    	  public void run() {
    		  
				VideoPlayer.wakeScreen(window);
    	  }
    	}, 0, 1, TimeUnit.MINUTES);
		
		

		VideoPlayer.setBG("res/image/background.jpg", window);

	}
	
	public static HashMap<HardwareValues.WoodType, Integer[]> makeVideos(){
	
		HashMap<HardwareValues.WoodType, Integer[]> m =  new HashMap<HardwareValues.WoodType, Integer[]>();
		
		Integer[] live_oak_fame_both_sides_planked = { 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30 };
		 
		Integer[] narrow_spaced_fame_both_sides_planked = { 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 11, 11, 11, 11 };
		 
		Integer[] narrow_spaced_fame_w_outer_planking = { 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 6, 6, 6, 6 };
		 
		Integer[] no_hull = { 41, 41, 41, 41, 41, 41, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 43, 43, 44, 44, 44, 44, 44, 44, 77, 77, 77, 77 };
		
		ArrayUtils.reverse(live_oak_fame_both_sides_planked);
		ArrayUtils.reverse(narrow_spaced_fame_both_sides_planked);
		ArrayUtils.reverse(narrow_spaced_fame_w_outer_planking);
		ArrayUtils.reverse(no_hull);
		
		m.put(HardwareValues.WoodType.LIVEOAK, live_oak_fame_both_sides_planked);
		m.put(HardwareValues.WoodType.PINE, narrow_spaced_fame_both_sides_planked);
		m.put(HardwareValues.WoodType.OAK, narrow_spaced_fame_w_outer_planking);
		m.put(HardwareValues.WoodType.NONE, no_hull);
		
		return m;
	}
	
	static HashMap<HardwareValues.WoodType, Integer[]> m =  makeVideos();
	
	public static int getVideo(HardwareValues values){
		
		/*
		 * scale
		 *         (b-a)(x - min)
		 *	f(x) = --------------  + a
         * 		 	max - min
		 */
		
		double b = m.get(values.woodType).length-1;
		double a = 0;
		double x = values.distance;
		double min = CannonForce.MIN_RESISTANCE;
		double max = CannonForce.MAX_RESISTANCE;
		
		double scaled = ((b-a)*(x - min))/(max-min);
		
		double scaled2 = Math.min(b,scaled);
		scaled2 = Math.max(a,scaled2);
		
		System.out.println(values.woodType + " in:" + values.distance + ", out:" + scaled + ", out2:" + scaled2);
		

		
		return m.get(values.woodType)[(int) scaled2];

		
	}

}
