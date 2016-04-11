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

public class RunnerDownloader2 {
		private URL url;
		private ArrayList<URL> pdfs;

		public RunnerDownloader2() throws IOException {
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
			Runner2 r = new Runner2(pdfs, path);
			r.run();
		}
		
		public static void main (String[] args) {
			try {
				RunnerDownloader2 rd = new RunnerDownloader2();
				rd.download();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}