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
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class Spider {
	ArrayList<URL> toCrawl;
	ArrayList<URL> hasCrawled;
	ArrayList<String> mails;

	public Spider(String URLString) {
		try {
			toCrawl = new ArrayList<URL>();
			toCrawl.add(new URL(URLString));
		} catch (MalformedURLException e) {
			System.out.println("Entered URL is malformed: " + e.getMessage());
		}
		hasCrawled = new ArrayList<URL>();
		mails = new ArrayList<String>();
	}


	public synchronized URL getNextURL() {
		while (toCrawl.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		return toCrawl.remove(0);
	}

	public synchronized void addToCrawl(URL u) {
		if (!hasCrawled.contains(u) && !toCrawl.contains(u)) {
			toCrawl.add(u);
			notifyAll();
		}
	}

	public synchronized boolean isDone() {
		if (hasCrawled.size() + toCrawl.size() > 2000) {
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
			File file = new File(path, "URLs1.txt");
			Writer writer = new BufferedWriter(new FileWriter(file));
			for (URL u : hasCrawled) {
				writer.write(u.toString() + "\n");
			}
			for (URL u : toCrawl)  {
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
		Spider spider = new Spider("http://cs.lth.se/");
		ArrayList<Processor> processors = new ArrayList<Processor>();
		long startTime = System.nanoTime();
		for (int i = 0; i < 10; i++) {
			processors.add(new Processor(spider));
			processors.get(i).start();
		}
		for (Processor p : processors) {
		    try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println((System.nanoTime() - startTime) / 1000000000 + " s");
		spider.save();
	}

}
