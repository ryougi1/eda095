package lab4;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	// void parse() {
	// String html = "<html><head><title>First parse</title></head>"
	// + "<body><p>Parsed HTML into a doc.</p></body></html>";
	// Document doc = Jsoup.parse(html);
	// System.out.println(doc);
	// }

	void parse() throws IOException {
		URL url = new URL("http://cs.lth.se/eda095/");
		InputStream is = url.openStream();
		Document doc = Jsoup.parse(is, "UTF-8", "http://cs.lth.se/");
		Elements base = doc.getElementsByTag("base");
		System.out.println("Base : " + base);
		Elements links = doc.getElementsByTag("a");
		for (Element link : links) {
			String linkHref = link.attr("href");
			String linkAbsHref = link.attr("abs:href");
			String linkText = link.text();
			System.out.println("href: " + linkHref + "abshref: " + linkAbsHref + " text: " + linkText);
		}
		is.close();
	}

	public void outline(Document doc) {
		Elements els = doc.select("h1, h2, h3, h4, h5, h6");
		for (Element el : els) {
			// Last char
			Integer last = new Integer((el.tagName()).substring(el.tagName().length() - 1));
			String prefix = "";
			for (int i = 0; i < last; i++) {
				prefix += "\t";
			}
			System.out.println(prefix + el.text());
		}
	}

	public static void main(String[] args) {
		Test test = new Test();
		URL url;
		try {
			url = new URL("http://cs.lth.se/eda095/");
			InputStream is = url.openStream();
			Document doc = Jsoup.parse(is, "UTF-8", "http://cs.lth.se/");
			test.outline(doc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			test.parse();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
