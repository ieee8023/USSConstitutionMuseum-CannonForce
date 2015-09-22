import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		
		System.out.println(Arrays.toString(AudioSystem.getAudioFileTypes()));
		
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sound/boom.mp3"));
		AudioFormat baseFormat = audioInputStream.getFormat();
		
		System.out.println(baseFormat);
		
	}
	
	public static void playSound(String file) {
	    try {
	    	
//	    	AudioSystem.getAudioInputStream(new File(textField.getText()));
//			AudioFormat baseFormat = audioInputStream.getFormat();
//	    	
//	    	AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
//					baseFormat.getSampleRate(),
//					16,
//					baseFormat.getChannels(),
//					baseFormat.getChannels() * 2,
//					baseFormat.getSampleRate(),
//					false);
//			
//			AudioInputStream dis = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
//			audioInputStream = dis;
//			audioFormat = dis.getFormat();
	    	
	    	
	    	
	    	
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
}
