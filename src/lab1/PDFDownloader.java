package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class PDFDownloader {
	private URL url;
	private static String html;
	private static ArrayList<URL> pdfs;

	public PDFDownloader(URL url) throws IOException {
		this.url = url;
		downloadHTML();
		getPDFLinks();
	}

	private void downloadHTML() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
		String s;
		StringBuilder sb = new StringBuilder();
		while ((s = bufferedReader.readLine()) != null) {
			sb.append(s);
			sb.append("\n");
		}
		html = sb.toString();
	}

	private void getPDFLinks() throws MalformedURLException {
		pdfs = new ArrayList<URL>();
		Pattern linkPattern = Pattern.compile("href=\"(.*?)\"");
		Matcher linkMatcher = linkPattern.matcher(html);
		while (linkMatcher.find()) {
			if (linkMatcher.group(1).endsWith(".pdf")) {
				pdfs.add(new URL(linkMatcher.group(1)));
			}
		}
	}

	public void download() throws IOException {
		int i = 1;
		for (URL u : pdfs) {
			Files.copy(u.openStream(), Paths.get("file" + i + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
			i++;
		}
	}

	public void download2() {
		Path path = null;
		for (URL u : pdfs) {
			File file;
			JFileChooser jf = new JFileChooser();
			switch (jf.showSaveDialog(jf)) {
			case JFileChooser.APPROVE_OPTION:
				file = jf.getSelectedFile();
				break;
			default:
				return;
			}
			path = Paths.get(file.getPath());
			try {
	    		Files.copy(u.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException {
		PDFDownloader d = new PDFDownloader(new URL("http://cs.lth.se/eda095/foerelaesningar/?no_cache=1"));
		d.download2();

	}
}