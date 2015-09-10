import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		String video = "video/mp4s/01.mp4";
		
		Process p = Runtime.getRuntime().exec("/Applications/VLC.app/Contents/MacOS/VLC --noaudio --play-and-stop --play-and-exit -f " + video);

		//Thread.sleep(5000);
		//p.destroyForcibly();
	}

}
