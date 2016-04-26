package lab4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MonothreadCrawler {
	ArrayList<URL> toCrawl;
	ArrayList<URL> hasCrawled;
	ArrayList<String> mails;

	public MonothreadCrawler(String urlString) {
		try {
			toCrawl = new ArrayList<URL>();
			toCrawl.add(new URL(urlString));
		} catch (MalformedURLException e) {
			System.out.println("Entered URL is malformed: " + e.getMessage());
		}
		hasCrawled = new ArrayList<URL>();
		mails = new ArrayList<String>();
	}

	public void crawl() {
		while (hasCrawled.size() < 20 && toCrawl.size() > 0) {
			URL currentURL = toCrawl.remove(0);
			URLConnection URLcon;
			try {
				URLcon = currentURL.openConnection();
				if (URLcon.getContentType() == null) {
					System.out.println(currentURL.toString() + " could not be accessed.");
				} else if (URLcon.getContentType().contains("text") && !hasCrawled.contains(currentURL)) {
					hasCrawled.add(currentURL);
					parse(currentURL);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void parse(URL url) {
		try {
			InputStream is = url.openStream();
			Document doc = Jsoup.parse(is, "UTF-8", url.toString());
			Elements links = doc.select("a[href]");
			Elements frames = doc.select("frame[src]");
			for (Element link : links) {
				String urlString = link.absUrl("href");
				if (urlString.startsWith("mailto:")) {
					handleMail(urlString);
				} else {
					URL u = new URL(urlString);
					toCrawl.add(u);
				}
			}
			for (Element frame : frames) {
				System.out.println(frame.toString());
				URL u = new URL(url, frame.toString());
				toCrawl.add(u);
			}
			is.close();
		} catch (IOException ioe) {
			System.out.println(url.toString() + " could not be accessed.");
		}
	}

	private void handleMail(String urlString) {
		String mailAddress = urlString.substring(urlString.indexOf(":") + 1, urlString.length());
		mailAddress = mailAddress.replaceAll("%40", "@");
		mailAddress = mailAddress.toLowerCase();
		if (!mails.contains(mailAddress)) {
			mails.add(mailAddress);
		}
	}

	public void save() {
		File file;
		JPanel thisPanelPreventsSuspiciousFileChooserBehaviour = new JPanel();
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		switch (jf.showSaveDialog(thisPanelPreventsSuspiciousFileChooserBehaviour)) {
		case JFileChooser.APPROVE_OPTION:
			file = jf.getSelectedFile();
			break;
		default:
			return;
		}
		String path = file.getPath() + "/";
		saveURL(path);
		saveMail(path);
	}

	private void saveURL(String path) {
		try {
			File file = new File(path, "URLs.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			for (URL u : hasCrawled) {
				writer.write(u.toString() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveMail(String path) {
		try {
			File file = new File(path, "Mails.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			for (String m : mails) {
				writer.write(m + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MonothreadCrawler mc = new MonothreadCrawler("http://cs.lth.se/");
		long startTime = System.nanoTime();
		mc.crawl();
		System.out.println((System.nanoTime() - startTime) / 10000000);
		mc.save();
	}

}
