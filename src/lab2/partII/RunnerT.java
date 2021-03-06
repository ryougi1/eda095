package lab2.partII;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class RunnerT extends Thread {
	private String path;
	private Main m;

	public RunnerT(Main m, String path) {
		this.path = path;
		this.m = m;
	}

	public void run() {
		URL next;
		while ((next = m.getNext()) != null){
			String s = next.getFile();
			while (s.contains("/")) {
				try {
					int i = s.indexOf("/");
					s = s.substring(i + 1, s.length());
					Files.copy(next.openStream(), Paths.get(path + s), StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Saved file " + s + " to: " + "\n" + path);
				} catch (IOException e) {
				}
			}
		}
	}
}
