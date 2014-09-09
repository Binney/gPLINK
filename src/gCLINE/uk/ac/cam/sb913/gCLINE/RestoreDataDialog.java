package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;

@SuppressWarnings("serial")
public class RestoreDataDialog extends JDialog{
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(RestoreDataDialog.class);
	
	JTextField fileField;
	private GCFileChooser localChooser;
	Record data;
	
	public RestoreDataDialog(JFrame parent, Record d){
		super(parent, "Restore from xml");
		data = d;
		fileField = new JTextField(40);
		
		this.getRootPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		//create a chooser for the browse button
		localChooser = new GCFileChooser(this, 
				new FileFilter(){
					@Override
					public boolean accept(File f) {
						return f.isDirectory() 
							|| f.getAbsolutePath().endsWith(".xml");
					}
					@Override
					public String getDescription() {
						return "XML file";
					}
				}, true, false,	null, 
				data.getLocalFolder().getAbsolutePath());
		
		//put the string from the chooser in the localProject field
		localChooser.addFileChoosenListener(new FileChoosenListener(){
			public void fileChoosenOccures(FileChoosenEvent evt) {
				logger.info("(createLocal()) adding the localProject");
				fileField.setText(localChooser.fileName);
			}
		});
	
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		getRootPane().add(fileField, c);
		
		//create the browse button
		JButton browse = new JButton("Browse");
		//link up the local browse button with the chooser
		browse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				localChooser.showChooser();
			}
		});
		c.weightx = 0;
		c.gridx = 1;
		getRootPane().add(browse, c);
		
		JPanel buttons = new JPanel();
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				data.loadData(new File(fileField.getText()), 
						data.getHomeFolder(), 
						data.isRemote());
				dispose();
			}
		});
		buttons.add(ok);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttons.add(cancel);
		c.weightx = 1;
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		getRootPane().add(buttons, c);
		
		pack();
		setVisible(true);
	}
	
}
