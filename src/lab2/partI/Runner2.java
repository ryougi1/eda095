package lab2.partI;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Runner2 implements Runnable{
	private ArrayList<URL> pdfs;
	private String path;

	public Runner2 (ArrayList<URL> pdfs, String path) {
		this.pdfs = pdfs;
		this.path = path;
	}
	
	public void run() {
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
	}
	
	
}
