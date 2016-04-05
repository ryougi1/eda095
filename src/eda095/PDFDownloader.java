package eda095;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class PDFDownloader {
	private static ArrayList<URL> pdfs;

	private PDFDownloader() throws IOException {
		getPDFLinks(downloadHTML(getURL()));
	}
	
	private URL getURL() throws MalformedURLException {
		URL url = new URL(JOptionPane.showInputDialog("Enter URL address: "));
		return url;
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
		Pattern linkPattern = Pattern.compile("href=\"(.*?.pdf)\""); //Matches href=\ literally, then captures all optionally;
		Matcher linkMatcher = linkPattern.matcher(html);
		while (linkMatcher.find()) {
				pdfs.add(new URL(linkMatcher.group(1)));
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
		String path = file.getPath()+"/";
		int counter = 0;
		for (URL u : pdfs) {
			String s = u.getFile();
			while (s.contains("/")) {
				int i = s.indexOf("/");
				s = s.substring(i+1, s.length());
			}
			Files.copy(u.openStream(), Paths.get(path+s), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Saved file " + s + " to: " + "\n"+ path);
			counter++;
			}
		System.out.println("\n" + "All files (" + counter + ") successfully saved.");
	}
	
	public static void main(String[] args) throws IOException {
		PDFDownloader d = new PDFDownloader();
		d.download();

	}
}