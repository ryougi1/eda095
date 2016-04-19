package lab2.partII;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Main {
	private URL url;
	private static String path;
	private static ArrayList<URL> pdfs;
	private final static int THREAD_MAX = 10;
	
	public Main() throws IOException {
		getURL();
		getPDFLinks(downloadHTML(url));
		setPath();
//		for (int i = 0; i < THREAD_MAX; i++) {
//			RunnerT rt = new RunnerT(this, path);
//			rt.start();
//			
//			new Thread(new RunnerR(this, path)).start();
//		}
	}
	
	public synchronized URL getNext() {
		if (!pdfs.isEmpty()) {
			return pdfs.remove(0);				
		} else {
			return null;
		}
	}
	
	private  void getURL() throws MalformedURLException {
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
	
	private void setPath() {
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
		path = file.getPath() + "/";
	}
	
	/** To start the executor version uncomment following start method and main method **/
	public void start() {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_MAX);
		for (URL u : pdfs) {
			String s = u.getFile();
			while (s.contains("/")) {
				int i = s.indexOf("/");
				s = s.substring(i + 1, s.length());
			}
			executor.submit(new RunnerE(u, Paths.get(path + s)));
		}
		executor.shutdown();
	}
	
	public static void main (String[] args) throws IOException {
		Main m = new Main(); //Initialize necessary variables
		m.start();
	}
	
	/** To start the non-executor versions uncomment following methods **/

	
//	public static void main (String[] args) throws IOException {
//		Main m = new Main();
//	}
}
