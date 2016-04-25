package lab4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) {
		File file;
		JPanel testPan = new JPanel();
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		switch (jf.showSaveDialog(testPan)) {
		case JFileChooser.APPROVE_OPTION:
			file = jf.getSelectedFile();
			break;
		default:
			return;
		}
	}

}
