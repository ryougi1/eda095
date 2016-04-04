package lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFDownloader {
	private URL url;
	private static String html;

	public PDFDownloader(URL url) throws IOException {
		this.url = url;
		html = downloadHTML();
	}

	public void downloadPDFs() {
		ArrayList<String> pdfs = getPDFLinks();
	}
	
	private String downloadHTML() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
		String s;
		StringBuilder sb = new StringBuilder();
		while ((s = bufferedReader.readLine()) != null) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

	private ArrayList<String> getPDFLinks() {
		ArrayList<String> PDFlinks = new ArrayList<String>();
		Pattern linkPattern = Pattern.compile("href=\"(.*?)\"");
		Matcher linkMatcher = linkPattern.matcher(html);
		while (linkMatcher.find()) {
			if (linkMatcher.group(1).endsWith(".pdf")) {
				PDFlinks.add(linkMatcher.group(1));				
			}
		}
		
		return PDFlinks;
	}

	public static void main(String[] args) throws IOException {
		PDFDownloader d = new PDFDownloader(new URL("http://cs.lth.se/eda095/foerelaesningar/?no_cache=1"));
		d.downloadPDFs();

		// System.out.println(d.downloadHTML());
	}
}