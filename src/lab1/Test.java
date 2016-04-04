package lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Test implements Serializable{
	private Path p;
	
	public Test() {
		savePath();
	}
	
	public void savePath() {
		File f;
		JFileChooser jf = new JFileChooser();
		switch (jf.showSaveDialog(jf)) {
		case JFileChooser.APPROVE_OPTION:
			f = jf.getSelectedFile();
			break;
		default:
			return;
		}
		p = Paths.get(f.getAbsolutePath());
	}
	
	public void savePDF(URL url) {
    	try {	
    		InputStream in = url.openStream();
    		Files.copy(in, p, StandardCopyOption.values());
        	JOptionPane.showMessageDialog(null,
                	"U DID IT");
        	in.close();
    	} catch (Exception ex) {
        	JOptionPane.showMessageDialog(null,
                	"OH NO");
    	}
	}
	
	

}
