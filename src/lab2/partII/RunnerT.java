package lab2.partII;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class RunnerT extends Thread {
	private ArrayList<URL> pdfs;
	private String path;
	private Counter c;

	public RunnerT(ArrayList<URL> pdfs, String path, Counter c) {
		this.pdfs = pdfs;
		this.path = path;
		this.c = c;
	}

	public void run() {
		c.countUp();
		savePDF();
	}

	public void savePDF() {
		for (URL u : pdfs) {
			String s = u.getFile();
			while (s.contains("/")) {
				try {
					int i = s.indexOf("/");
					s = s.substring(i + 1, s.length());
					Files.copy(u.openStream(), Paths.get(path + s), StandardCopyOption.REPLACE_EXISTING);
					System.out.println("Saved file " + s + " to: " + "\n" + path);
				} catch (IOException e) {
				}
			}
		}
		c.countDown();
	}
}
