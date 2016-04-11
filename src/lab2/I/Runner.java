package lab2.I;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Runner implements Runnable{
	private URL url;
	private Path path;
	
	public Runner (URL url, Path path){
		this.url = url;
		this.path = path;
	}
	@Override
	public void run() {
		try {
			Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
		}
	}
}
