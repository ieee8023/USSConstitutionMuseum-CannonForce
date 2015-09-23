import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gertboard;

import lcd.SevenSegment;

public class CannonForce {
	
	static boolean running = false;
        
    final GpioPinDigitalInput fireBtn;
    final GpioPinDigitalOutput fireBtnLed;
    final GpioPinDigitalInput wood1In;
    final GpioPinDigitalInput wood2In;
    final GpioPinDigitalInput wood3In;
    
    final SevenSegment segment;
    int distance = 0;
	final DescriptiveStatistics ds;
    
    
    public CannonForce(){
    	
    	 System.out.println("Init Level 1");
         
 		int err = Gertboard.gertboardSPISetup();
 		if (err != 0) System.out.println("Error with gertboardSPISetup: " + err);
         
         final GpioController gpio = GpioFactory.getInstance();

         fireBtn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_UP);
         fireBtnLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.HIGH);
         
         wood1In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
         wood2In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);
         wood3In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_UP);
         

         segment = new SevenSegment(0x70, true);
         
         ds = new DescriptiveStatistics();
         ds.setWindowSize(5);
    }
    
    
    
    public HardwareValues getHardwareValues(){
    	
    	
        System.out.println("    wood1In " + wood1In.getState());
        System.out.println("    wood2In " + wood2In.getState());
        System.out.println("    wood3In " + wood3In.getState());
        System.out.println("    distance " + distance);
        

    	HardwareValues values = new HardwareValues();
    	
    	if (wood1In.getState().isHigh())
    		values.woodType = HardwareValues.WoodType.LIVEOAK;
    	if (wood2In.getState().isHigh())
    		values.woodType = HardwareValues.WoodType.OAK;
    	if (wood3In.getState().isHigh())
    		values.woodType = HardwareValues.WoodType.PINE;
    	
    	values.distance = distance;
    	
    	return values;
    }
        
    
    public void listen(final CannonCallback callback){
    	
       
        fireBtn.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            	
            	if (event.getState().equals(PinState.HIGH)){
            	
	            	if (running) return;
	            	running = true;
	            	
	                System.out.println(event.getPin() + " = " + event.getState());
	                
	                fireBtnLed.setState(PinState.LOW);
	                try {
	                	
	                	if (callback != null)
	                		callback.callback(getHardwareValues());
	                	
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					} finally {
						fireBtnLed.setState(PinState.HIGH);
						running = false;
					}
            	}
                
            }
            
        });
        
        System.out.println("Init Level 2");
        System.out.println(fireBtn.getPin() + " waiting");
        

        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller        
    }
    
	public static int MAX_RESISTANCE = 1000;
	public static int MIN_RESISTANCE = 500;
    
    public void writeDistance(){

    	ds.addValue(Gertboard.gertboardAnalogRead(1));

    	distance = (int) ds.getMean();
    	
    	//System.out.println("Given distance: " + distance);
    	
		/*
		 * scale
		 *         (b-a)(x - min)
		 *	f(x) = --------------  + a
         * 		 	max - min
		 */
		
		double b = 260;
		double a = 0;
		double x = distance;
		double min = CannonForce.MIN_RESISTANCE;
		double max = CannonForce.MAX_RESISTANCE;
		
		double scaled = ((b-a)*(x - min))/(max-min);
		
		double scaled2 = Math.min(b,scaled);
		scaled2 = Math.max(a,scaled2);
    	

		distance = (int) scaled2;
    	
    	//System.out.println("scaled: " + distance);
    	
    	try {
    	      segment.writeDigit(0, (distance / 1000));     // 1000th
    	      segment.writeDigit(1, (distance / 100) % 10); // 100th
    	      segment.writeDigit(3, (distance / 10) % 10);  // 10th
    	      segment.writeDigit(4, distance % 10);         // Ones
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public static void main(String args[]) throws InterruptedException {

    	new CannonForce().listen(null);
    	
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
    }
    
}
