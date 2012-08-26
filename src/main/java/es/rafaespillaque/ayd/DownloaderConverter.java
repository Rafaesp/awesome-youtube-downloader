package es.rafaespillaque.ayd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class DownloaderConverter implements Runnable{

	public void run() {
		
		//TODO Opcion para youtube-dl -U (update)
		
		PySystemState state = new PySystemState();

		// state.argv.append(new PyString("--extract-audio"));
		// state.argv.append(new PyString("--audio-format"));
		// state.argv.append(new PyString("mp3"));
		state.argv.append(new PyString(
				"http://www.youtube.com/watch?v=6mtM8R7KWyY"));

		PythonInterpreter interpreter = new PythonInterpreter(null, state);
		File file = new File("youtube-dl");
		System.out.println("inicio");
		InputStream in = null;
		try {
			in = new FileInputStream(file);

			interpreter.execfile(in);
		} catch (Exception ignore) {

		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {

			System.out.println("deco");

//			String[] commands = new String[] { "chmod", "777", "./ffmpeg" };
//			Process proc1 = Runtime.getRuntime().exec(commands);
//			proc1.waitFor();

			ProcessBuilder pb = new ProcessBuilder(new String[] { "./ffmpeg",
					"-i", "6mtM8R7KWyY.flv", "-f", "mp3", "-vn",
					"/home/rafa/Descargas/vidddd.mp3" });
			pb.redirectErrorStream(true);
			Process proc = pb.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String aux = br.readLine();

			while (aux != null) {
				System.out.println(aux);
				aux = br.readLine();
			}

			System.out.println("fin");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

}
