package lab2.I;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class RunnerDownloader {
	private URL url;
	private ArrayList<URL> pdfs;

	public RunnerDownloader() throws IOException {
		getURL();
		getPDFLinks(downloadHTML(url));
	}

	private void getURL() throws MalformedURLException {
		URL url = new URL(JOptionPane.showInputDialog("Enter URL address: "));
		this.url = url;
	}

	private String downloadHTML(URL url) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
		String s;
		StringBuilder sb = new StringBuilder();
		while ((s = bufferedReader.readLine()) != null) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

	private void getPDFLinks(String html) throws MalformedURLException {
		pdfs = new ArrayList<URL>();
		Pattern linkPattern = Pattern.compile("href=\"(.*?.pdf)\"");
		Matcher linkMatcher = linkPattern.matcher(html);
		while (linkMatcher.find()) {
				pdfs.add(new URL(url, linkMatcher.group(1)));
		}
	}

	public void download() throws IOException {
		File file;
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		switch (jf.showSaveDialog(jf)) {
		case JFileChooser.APPROVE_OPTION:
			file = jf.getSelectedFile();
			break;
		default:
			return;
		}
		String path = file.getPath() + "/";
		int counter = 0;
		Runner r;
		for (URL u : pdfs) {
			String s = u.getFile();
			while (s.contains("/")) {
				int i = s.indexOf("/");
				s = s.substring(i + 1, s.length());
			}
			r = new Runner(u, Paths.get(path + s));
			r.run();
			System.out.println("Saved file " + s + " to: " + "\n" + path);
			counter++;
		}
		System.out.println("\n" + "All files (" + counter + ") successfully saved.");
	}
	
	public static void main (String[] args) {
		try {
			RunnerDownloader rd = new RunnerDownloader();
			rd.download();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
