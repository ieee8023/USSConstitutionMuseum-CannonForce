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

public class CanonForce {
	
	static boolean running = false;
    
    public static void main(String args[]) throws InterruptedException {

    	new CanonForce().listen(null);
    	
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }
    	
    	
    }
    
    
    
    public void listen(final CannonCallback callback){
    	
        System.out.println("Init Level 1");
        
		int err = Gertboard.gertboardSPISetup();
		if (err != 0) System.out.println("Error with gertboardSPISetup: " + err);
        
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalInput fireBtn = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalOutput fireBtnLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.HIGH);
        
        final GpioPinDigitalInput wood1In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput wood2In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput wood3In = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        
        
        fireBtn.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            	
            	if (event.getState().equals(PinState.HIGH)){
            	
	            	if (running) return;
	            	running = true;
	            	
	                System.out.println(event.getPin() + " = " + event.getState());
	                System.out.println("    wood1In " + wood1In.getState());
	                System.out.println("    wood2In " + wood2In.getState());
	                System.out.println("    wood3In " + wood3In.getState());
	                System.out.println("    distance " + Gertboard.gertboardAnalogRead(1));
	                
	                
	                
	                
	                fireBtnLed.setState(PinState.LOW);
	                try {
	                	
	                	HardwareValues values = new HardwareValues();
	                	
	                	if (callback != null)
	                		callback.callback(values);
	                	
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
}
