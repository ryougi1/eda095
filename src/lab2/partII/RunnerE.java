package lab2.partII;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class RunnerE implements Runnable {
	private URL url;
	private Path path;
	

	public RunnerE(URL url, Path path) {
		this.url = url;
		this.path = path;
	}

	public void run() {
				savePDF();
	}

	private void savePDF() {
		try {
			Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);			
		} catch (IOException e1) {
			
		}
	}

}