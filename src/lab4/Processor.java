package lab4;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Processor extends Thread {
	private Spider spider;

	public Processor(Spider s) {
		spider = s;
	}

	public void run() {
		while (!spider.isDone()) {
			URL url = spider.getNextURL();
			try {
				URLConnection uc = url.openConnection();
				if (uc.getContentType() == null) {
					System.out.println(url.toString() + " could not be accessed.");
				} else if (uc.getContentType().contains("text") && !spider.hasCrawled.contains(url)) {
					spider.hasCrawled.add(url);
				}
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
						spider.addToCrawl(u);
					}
				}
				for (Element frame : frames) {
					URL u = new URL(url, frame.toString());
					spider.addToCrawl(u);
				}
				is.close();
			} catch (IOException ioe) {
				System.out.println(url.toString() + " could not be accessed.");
			}
		}

	}

	private void handleMail(String urlString) {
		String mailAddress = urlString.substring(urlString.indexOf(":") + 1, urlString.length());
		mailAddress = mailAddress.replaceAll("%40", "@");
		mailAddress = mailAddress.toLowerCase();
		if (!spider.mails.contains(mailAddress)) {
			spider.mails.add(mailAddress);
		}
	}
}
