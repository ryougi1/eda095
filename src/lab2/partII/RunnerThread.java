package lab2.partII;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class RunnerThread extends Thread {
	private URL url;
	private Path path;
	

	public RunnerThread(URL url, Path path) {
		this.url = url;
		this.path = path;
	}

	public void run() {
//		long startTime = System.nanoTime();
		try {
			savePDF();
		} catch (IOException e) {
		}
//		System.out.println((System.nanoTime() - startTime) / 10000000);

	}

	private void savePDF() throws IOException {
		Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
	}

}