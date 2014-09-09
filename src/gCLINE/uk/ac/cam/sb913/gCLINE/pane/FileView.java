package gCLINE.uk.ac.cam.sb913.gCLINE.pane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class FileView extends JPanel {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(FileView.class);
	private JTextArea textdisplay;
	public JLabel currentFile;

	public void viewFile(String fileName){
		logger.info("(viewFile(String)) Shifting viewer to: ["+fileName+"]");
		File reading = new File(fileName);
		
		if(reading.exists() == false){
			
			reading = new File(fileName);
			
			if(reading.exists() == false)
				return;
		}
		
		textdisplay.setText("");
		try {
			FileReader stdInput = new FileReader(reading);
			textdisplay.read(stdInput, null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		currentFile.setText(reading.getAbsolutePath());
	}
	
	public FileView(){
		super();
		textdisplay = new JTextArea(10, 80);
		textdisplay.setFont( new Font( "Monospaced", Font.PLAIN, 12 ));
		textdisplay.setEditable(false);
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("Log viewer"));
		
		currentFile = new JLabel("No view selected");
		
		add(currentFile, BorderLayout.PAGE_START);
		add(new JScrollPane(textdisplay), BorderLayout.CENTER);
		
	}
	
}
