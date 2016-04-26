package lab4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class Spider {
	ArrayList<URL> toCrawl;
	ArrayList<URL> hasCrawled;
	ArrayList<String> mails;
	long startTime;

	public Spider(String URLString, long startTime) {
		try {
			toCrawl = new ArrayList<URL>();
			toCrawl.add(new URL(URLString));
		} catch (MalformedURLException e) {
			System.out.println("Entered URL is malformed: " + e.getMessage());
		}
		hasCrawled = new ArrayList<URL>();
		mails = new ArrayList<String>();
		this.startTime = startTime;
	}

//	public void start() {
//		startTime = System.nanoTime();
//		ExecutorService es = Executors.newFixedThreadPool(10);
//		for (int i = 0; i < 10; i++) {
//			es.submit(new Processor(this));
//		}
//		es.shutdown();
//	}

	public synchronized URL getNextURL() {
		if (toCrawl.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		return toCrawl.remove(0);
	}

	public synchronized void addToCrawl(URL u) {
		if (!hasCrawled.contains(u)) {
			toCrawl.add(u);
			notifyAll();
		}
	}

	public boolean isDone() {
		if (hasCrawled.size() > 2000) {
			System.out.println((System.nanoTime() - startTime) / 10000000);
			return true;
		}
		return false;
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
		Spider spider = new Spider("http://cs.lth.se/", System.nanoTime());
		for (int i = 0; i < 10; i++) {
			new Processor(spider).start();;
		}
		
	}

}
