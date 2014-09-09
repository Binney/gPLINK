package gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;


@SuppressWarnings("serial")
public final class OutputPane extends JPanel {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(OutputPane.class);
	/**
	 * The extension for the operation log files
	 */
	private String op_log_ext;
	/**
	 * A ArrayList of Strings that are the files in
	 * the home directory that end with the operation
	 * log extension.
	 */
	private ArrayList <String> rootList;
	/**
	 * The proposed output root.
	 */
	private JTextField root;
	/**
	 * The form this output pane is going 
	 * to appear in.
	 */
	private Form parentForm;
	/**
	 * Flag the form as valid or not valid.
	 */
	boolean validOutput = false;
	
	
	protected OutputPane(Form p, 
			String givenop_log_ext, 
			ArrayList<String> givenHomeFiles,
			String homeFolder){
		
		parentForm = p;
		root = new JTextField(10);

		op_log_ext = givenop_log_ext;
		rootList = new ArrayList<String>();
		ArrayList <String> temp = givenHomeFiles;
		for(String file: temp){
			if(file.endsWith(op_log_ext))
				rootList.add(file);
		}
		
		setBorder(new TitledBorder("Output file root: Invalid fileroot"));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(new JLabel(homeFolder));
		add(root);
		root.getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {
				logger.info("(changeUpdate(DocumentEvent)) root fires changedUpdate");
				
			}
			public void insertUpdate(DocumentEvent e) {
				logger.info("(insertUpdate(DocumentEvent)) root fires insertUpdate");
				boolean state = okOutput();
				if(state != validOutput){
					validOutput = state;
					if(validOutput)
						setBorder(new TitledBorder("Output file root: Valid fileroot"));
					else
						setBorder(new TitledBorder("Output file root: Invalid fileroot"));
					parentForm.okForm();
				}
			}
			public void removeUpdate(DocumentEvent e) {
				logger.info("(remoteUpdate(DocumentEvent)) root fires removeUpdate");
				boolean state = okOutput();
				if(state != validOutput){
					validOutput = state;
					if(validOutput)
						setBorder(new TitledBorder("Output file root: Valid fileroot"));
					else
						setBorder(new TitledBorder("Output file root: Invalid fileroot"));
					parentForm.okForm();
				}
			}
		});
	}
	
	public String process(){
		return "--out " + FileInfo.quote(parentForm.parent.getHomeFolder() + root.getText());
	}
	protected boolean okOutput(){
		String temp = root.getText();
		return (temp.matches("^[\\w\\-]+$")
				&& !rootList.contains(temp+op_log_ext));
	}

}