import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		System.out.println(Arrays.toString(AudioSystem.getAudioFileTypes()));
		
		SoundPlayer sp = new SoundPlayer("res/sound/boom.mp3");
		
		sp.playSound(true);

        System.out.println("Done");
		
	}
	
	Clip clip;
	
	public SoundPlayer(String file) {
		
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
			AudioFormat baseFormat = audioInputStream.getFormat();
			
			//System.out.println(baseFormat);
	    	
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false);
			
			AudioInputStream dis = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
			audioInputStream = dis;
			//AudioFormat audioFormat = dis.getFormat();
			//System.out.println("Converted to: " + audioFormat);
			
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        
		} catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void playSound(boolean block) {
	    try {
	    	
	    	if (clip != null){
		    	clip.setFramePosition(0);
		        clip.start();
		    	
		        if (block)
		            while(clip.getFrameLength() >= clip.getFramePosition()+1){
		            	Thread.sleep(10);
		            }
	    	}else{
	    		System.out.println("clip is null");
	    	}
	        
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
}
