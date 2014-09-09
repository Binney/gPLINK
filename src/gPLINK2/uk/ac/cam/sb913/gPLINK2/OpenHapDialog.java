package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.CalculationInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;

/**
 * A JDialog that launches Haploview.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class OpenHapDialog extends JDialog {

	/**
	 * Log comments and errors
	 */
	private static Logger logger = Logger.getLogger(OpenHapDialog.class);
	
	/**
	 * The GPLINK instance this dialog is attached to. We
	 * access the project data through this.
	 */
	private GPLINK frame;
	
	/**
	 * A String array that holds all the suffix's that
	 * flag a ped file.
	 */
	private static String [] pedSuffix = new String [] {".ped"};
	/**
	 * A String array that holds all the suffix's that
	 * flag a info file.
	 */
	private static String [] infoSuffix = new String [] {".info"};
	/**
	 * A JRadioButton to flag the ped/info file option
	 * for haploview
	 */
	private JRadioButton pedInfoRB = new JRadioButton("ped/info file set");
	/**
	 * A JTextField that holds the full ped file path.
	 */
	private JTextField pedFile = new JTextField(20);
	/**
	 * A JButton that triggers the ped file browse.
	 */
	private JButton pedButton = new JButton("Browse");
	/**
	 * A JTextField that holds the full info file path.
	 */
	private JTextField infoFile = new JTextField(20);
	/**
	 * A JButton that triggers the info file browse.
	 */
	private JButton infoButton = new JButton("Browse");
	
	/**
	 * A JRadioButton to flag the nonSNP file option.
	 * This is the default option for a random file.
	 */
	private JRadioButton nonSNPRB = new JRadioButton("Non-SNP files");
	/**
	 * A JTextField that holds the full path name of nonSNP
	 * reconized files. This is the default.
	 */
	private JTextField nonSNPFile = new JTextField(20);
	/**
	 * A JButton that browses for the nonSNP file.
	 */
	private JButton nonSNPButton = new JButton("Browse");
	
	/**
	 * A String array that holds all the suffix's that
	 * flag a map/bim file.
	 */
	private static String [] mapSuffix = new String [] {".map", ".bim"};
	/**
	 * A String array that holds all the suffix's that
	 * flag a SNP file.
	 */
	private static String [] SNPSuffix = new String [] {".adjusted",
		 ".assoc", ".assoc.mperm", 
		".assoc.perm", ".cmh", ".cmh2", ".frq", ".hwe",
		".lmiss", ".missing", ".missing.hap", 
		".linear", ".logistic", ".genotypic", ".model", 
		".qassoc", ".qassoc.gxe", ".tdt", ".tdt.mperm", 
		".tdt.perm"};
	/**
	 * A JRadioButton to flag the SNP file option
	 */
	private JRadioButton SNPRB = new JRadioButton("SNP files");
	/**
	 * A JTextField that holds a SNP defined file name.
	 */
	private JTextField SNPFile = new JTextField(20);
	/**
	 * A JButton to browse for SNP defined files.
	 */
	private JButton SNPButton = new JButton("Browse");
	/**
	 * A JTextField that holds the map/bim file names.
	 */
	private JTextField mapFile = new JTextField(20);
	/**
	 * A JButton that triggers a browse for the map/bim file.
	 */
	private JButton mapButton = new JButton("Browse");
	
	/**
	 * A JButton that signals to process and close the dialog.
	 */
	private JButton ok = new JButton("OK");
	/**
	 * A JButton to close the dialog without doing anything.
	 */
	private JButton cancel= new JButton("Cancel");
	
	/**
	 * Create a generic ActionListener for a browse button
	 * that launches a local file browse.
	 * @author Kathe Todd-Brown
	 *
	 */
	private class myActionListener implements ActionListener{
		/**
		 * A GCFileChooser that selects the file
		 */
		private GCFileChooser chooser;
		/**
		 * A JTextField that holds the file name and is
		 * the target of the GCFileChooser
		 */
		private JTextField text;
		/**
		 * A String array that holds the suffix of the files
		 * we are looking for
		 */
		private String [] suffix; 
		/**
		 * A String that is a description of the suffix's
		 */
		private String desc;
		
		/**
		 * Create myActionListener.
		 * @param myText A JTextField the field where the
		 * the file name will appear.
		 * @param mySuffix A String array that is the prefered
		 * suffix for this file chooser.
		 * @param myDesc A String that describes the prefered
		 * suffix.
		 */
		public myActionListener(JTextField myText, 
				String [] mySuffix, String myDesc){
			text = myText;
			suffix = mySuffix;
			desc = myDesc;
		}
		
		/**
		 * Create and show the file chooser.
		 */
		public void actionPerformed(ActionEvent e) {
			FileFilter filter;
			if(suffix != null && desc != null)
				filter = new FileFilter(){
					@Override
					public boolean accept(File f) {
						if(f.isDirectory())
							return true;
						for(String s: suffix){
							if(f.getName().endsWith(s))
								return true;
						}
						return false;
					}
					@Override
					public String getDescription() {
						return desc;
					}
					};
			else
				filter = null;
			
			//create a chooser for the browse button
			chooser = new GCFileChooser(frame, filter,
					true, false, null, frame.data.getLocalFolder().getAbsolutePath());
			//put the string from the chooser in the localProject field
			chooser.addFileChoosenListener(new FileChoosenListener(){
				public void fileChoosenOccures(FileChoosenEvent evt) {
					text.setText(chooser.fileName);
				}
			});
			
			//show the chooser
			chooser.showChooser();
		}
	}
	/**
	 * An ItemListener to set the JTextFields and JButtons to reflect
	 * the corrisponding radio button selection.
	 */
	private ItemListener setFields = new ItemListener(){
		public void itemStateChanged(ItemEvent arg0) {
			pedFile.setEditable(pedInfoRB.isSelected());
			pedButton.setEnabled(pedInfoRB.isSelected());
			infoFile.setEditable(pedInfoRB.isSelected());
			infoButton.setEnabled(pedInfoRB.isSelected());
			nonSNPFile.setEditable(nonSNPRB.isSelected());
			nonSNPButton.setEnabled(nonSNPRB.isSelected());
			SNPFile.setEditable(SNPRB.isSelected());
			SNPButton.setEnabled(SNPRB.isSelected());
			mapFile.setEditable(SNPRB.isSelected());
			mapButton.setEnabled(SNPRB.isSelected());
		}
	};
	
	/**
	 * An ActionListener that attaches to the ok button
	 * and opens an instance of haploview using the files
	 * specified in the dialog.
	 */
	private ActionListener openHaplo = new ActionListener(){

		public void actionPerformed(ActionEvent e) {
			
			/*
			 * *************************************************************
			 * Deal with ped/info combo since it has a seperate command line
			 * *************************************************************
			 */

			if(pedInfoRB.isSelected()){	
				//construct the command line
				String [] hapOptions = RunCommand.stripAndSplit(
						frame.data.getHaploAppend());
				String [] command = new String [ 7 +
				         hapOptions.length];
				command[0] = "java";
				command[1] = "-jar";
				command[2] = frame.data.getHaploPath();
				command[3] = "-p";
				command[4] = pedFile.getText();
				command[5] = "-i";
				command[6] = infoFile.getText();
				
				//append the projects haploview
				if(!hapOptions[0].equals(null)){
					for(int i = 0; i < hapOptions.length; i ++){
						command[i+7] = hapOptions[i];
					}
				}
				
				//send it off to the os
				try{	
					@SuppressWarnings("unused")
					Process haplo = Runtime.getRuntime().exec(command);
					
				} catch (IOException e1) {
					frame.messenger.createError(
		    	        	"Error trying to open the following files " +
    	        			"in haploview " +
    	        			"["+pedFile.getText()+"], ["+infoFile.getText()+"].\n",
    	        			"openHaploview:OpenHapDialog.java");
					logger.error("[openHaploview] Error trying to " +
							"open the following files in haploview " +
    	        			"["+pedFile.getText()+"], ["+infoFile.getText()+"].");
		        }
				
				//we are done!
				OpenHapDialog.this.dispose();
		        return;
			}
					
			/*
			 * ****************************************** 
			 * Deal with non-SNP files in Haploview
			 * ******************************************
			 */
			if(nonSNPRB.isSelected())		
			{
				String filename = nonSNPFile.getText();
				//check that the files are there
				if(new File(filename).exists() == false )
				{
					frame.messenger.createError( 
							"[ " + filename + " ] does not exist, " +
									"can not open Haploview.\n",
							"openHaplo:OpenHapDialog.java");
					logger.error("[openHaplo] [" + filename + " ] does not exist, " +
									"can not open Haploview.");
					OpenHapDialog.this.dispose();
					return;
				}
				
				//create the commandline
				String [] hapOptions = RunCommand.stripAndSplit(
						frame.data.getHaploAppend());
				String [] command = new String [ 6 +
				         hapOptions.length];
				command[0] = "java";
				command[1] = "-jar";
				command[2] = frame.data.getHaploPath();
				command[3] = "-plink";
				command[4] = filename;
				command[5] = "-nonSNP";
				
				//add any extra's to the haploview command
				if(!hapOptions[0].equals(null)){
					for(int i = 0; i < hapOptions.length; i ++){
						command[i+6] = hapOptions[i];
					}
				}
				//run the command locally
				try{
					@SuppressWarnings("unused") 
					Process haplo = Runtime.getRuntime().exec(command);
				} catch (IOException e1) {
					frame.messenger.createError(
	        			"Error trying to open the following files in Haploview " +
	        			"[" + filename + "]. \n",
	        			"openHaplo:OpenHapDialog.java");
					logger.error("[openHaplo] Error trying to open the following files in Haploview " +
	        			"[" + filename + "].");
		        }
				OpenHapDialog.this.dispose();
				return;
			}
			
			/*
			 * ****************************************** 
			 * Deal with remaining files (SNP results)
			 * ******************************************
			 */
			if(SNPRB.isSelected()){
				String filename = SNPFile.getText();
				String infile = mapFile.getText();
				//check that the files exist
				if(new File(filename).exists() == false 
						|| new File(infile).exists() == false)
				{
					frame.messenger.createError(
							"[ " + filename + " ] and/or [ "+ infile + " ] does not exist, " +
									"can not open haploview.\n",
							"openHaplo:OpenHapDialog.java");
					logger.error(
							"[openHaplo][ " + filename + " ] and/or [ "+ infile + " ] does not exist, " +
									"can not open haploview.\n");
					OpenHapDialog.this.dispose();
					return;
				}
				//create the commandline
				String [] hapOptions = RunCommand.stripAndSplit(
						frame.data.getHaploAppend());
				String [] command = new String [ 7 +
				         hapOptions.length];
				command[0] = "java";
				command[1] = "-jar";
				command[2] = frame.data.getHaploPath();
				command[3] = "-plink";
				command[4] = filename;
				command[5] = "-map"; 
				command[6] = infile;
				
				//add any extra's to the haploview command
				if(!hapOptions[0].equals(null)){
					for(int i = 0; i < hapOptions.length; i ++){
						command[i+7] = hapOptions[i];
					}
				}
				
				//try to run the command locally
				try{
					@SuppressWarnings("unused") 
					Process haplo = Runtime.getRuntime().exec(command);
				} catch (IOException e1) {
					frame.messenger.createError(
		        			"Error trying to open the following files in haploview " +
		        			"[ " + filename + "], [ " + infile + "]. \n",
		        			"openHaplo:OpenHapDialog.java");
					logger.error("[openHaplo] Error trying to open the " +
							"following files in haploview " +
		        			"[ " + filename + "], [ " + infile + "].");
		        }
				OpenHapDialog.this.dispose();
				return;
			}
			//nothing was triggered. Something probably went wrong
			//...but it doens't matter so we just leave.
			OpenHapDialog.this.dispose();
		}
		
	};
	
	/**
	 * Populate the fields based on the file and operation
	 * name.
	 * @param givenOp A String that names the operation that this file is
	 * attached to if it's none, null otherwise.
	 * @param filename A String that is the file name we are looking
	 * to open with haploview.
	 */
	private void populateFields(String givenOp, String filename){
		//get the input/output files for the operation
		ArrayList <FileInfo> inf = null;
		ArrayList <FileInfo> outf = null;
		if(givenOp != null){
			CalculationInfo op = (CalculationInfo)frame.data.getOp(givenOp);
			inf = op.getInputFiles();
			outf = op.getOutputFiles();
		}
		
		//check to see if the file is a ped file
		for(int i = 0; i < pedSuffix.length; i ++){
			//do the actual check
			if(filename.endsWith(pedSuffix[i])){
				pedInfoRB.setSelected(true);
				pedFile.setText(new File(frame.data.getLocalFolder(),
						FileInfo.fileName(filename)).getAbsolutePath());
				//if the operation is not null
				if(givenOp != null){
					//go through the infile looking for hte corrisponding info file
					for(FileInfo f : inf){
						if(f.toString().endsWith(".info")){
							infoFile.setText(new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
					//go through the outfile looking for the corrisponding info file
					for(FileInfo f : outf){
						if(f.toString().endsWith(".info")){
							infoFile.setText(new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
				}
				//we're done
				return;
			}
		}
		//check to see if the file is an info file
		for(int i = 0; i < infoSuffix.length; i ++){
			if(filename.endsWith(infoSuffix[i])){
				pedInfoRB.setSelected(true);
				infoFile.setText(new File(frame.data.getLocalFolder(),
						FileInfo.fileName(filename)).getAbsolutePath());
				//if the operation is not null
				if(givenOp != null){
					//go through the infile looking for the corrisponding ped file
					for(FileInfo f: inf){
						if(f.toString().endsWith(".ped")){
							pedFile.setText( new File(frame.data.getLocalFolder(), 
									FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
					//go through the outfiles looking for the corrisponding ped file
					for(FileInfo f: outf){
						if(f.toString().endsWith(".ped")){
							pedFile.setText( new File(frame.data.getLocalFolder(), 
									FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
				}
				//we're done!
				return;
			}				
		}
		
		//check to see if the file is a snp file
		for(int i = 0; i < SNPSuffix.length; i ++){
			if(filename.endsWith(SNPSuffix[i])){
				SNPRB.setSelected(true);
				SNPFile.setText(new File(frame.data.getLocalFolder(),
						FileInfo.fileName(filename)).getAbsolutePath());
				if(givenOp!=null){
					for(FileInfo f: inf){
						if(f.toString().endsWith(".map")
								|| f.toString().endsWith(".bim")){
							mapFile.setText( new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
					for(FileInfo f: outf){
						if(f.toString().endsWith(".map")
								|| f.toString().endsWith(".bim")){
							mapFile.setText( new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
				}
				//we're done!
				return;
			}				
		}
		//check to see if the file is a map file
		for(int i = 0; i < mapSuffix.length; i ++){
			if(filename.endsWith(mapSuffix[i])){
				SNPRB.setSelected(true);
				mapFile.setText(new File(frame.data.getLocalFolder(),
						FileInfo.fileName(filename)).getAbsolutePath());
				if(givenOp != null){
					for(FileInfo f: inf){
						for(int j = 0; j < SNPSuffix.length; j ++)
						if(f.toString().endsWith(SNPSuffix[j])){
							mapFile.setText( new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
					for(FileInfo f: outf){
						for(int j = 0; j < SNPSuffix.length; j ++)
						if(f.toString().endsWith(SNPSuffix[j])){
							mapFile.setText( new File(frame.data.getLocalFolder(), 
										FileInfo.fileName(f.toString())).getAbsolutePath());
							return;
						}
					}
				}
				return;
			}
		}
		
		//assume it is a nonSNP file
		nonSNPRB.setSelected(true);
		nonSNPFile.setText(new File(frame.data.getLocalFolder(), 
				FileInfo.fileName(filename)).getAbsolutePath());
	}

	/**
	 * Intialize and layout the panel.
	 *
	 */
	void createPanel(){
		//add the action lsiteners to the browse buttons
		pedButton.addActionListener(
				new myActionListener(pedFile, pedSuffix, "PED file"));
		infoButton.addActionListener(
				new myActionListener(infoFile, infoSuffix, "INFO file"));
		nonSNPButton.addActionListener(
				new myActionListener(nonSNPFile, null, null));
		mapButton.addActionListener(
				new myActionListener(mapFile, mapSuffix, "MAP file"));
		SNPButton.addActionListener(
				new myActionListener(SNPFile, SNPSuffix, "snp files"));
		
		//add the item listeners to the radio buttons
		pedInfoRB.addItemListener(setFields);
		nonSNPRB.addItemListener(setFields);
		SNPRB.addItemListener(setFields);
		
		//make sure you can only pick one button at a time
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(pedInfoRB);
		pickOne.add(nonSNPRB);
		pickOne.add(SNPRB);
		
		//create a panel for the buttons
		JPanel buttonPane = new JPanel();
		buttonPane.add(ok);
		ok.addActionListener(openHaplo);
		buttonPane.add(cancel);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				OpenHapDialog.this.dispose();
			}
		});
		
		//lay everything out all pretty
		Container mainPane = this.getContentPane();
		this.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		mainPane.add(pedInfoRB, c);
		
		c.gridx = 1;
		c.weightx = 1;
		mainPane.add(pedFile,c);
		
		c.gridx = 2;
		c.weightx = 0;
		mainPane.add(pedButton, c);
		
		c.gridx = 3;
		mainPane.add(new JLabel("(-info)"), c);
		
		c.gridx = 4;
		c.weightx = 1;
		mainPane.add(infoFile, c);
		
		c.gridx = 5;
		c.weightx = 0;
		mainPane.add(infoButton, c);
		
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0;
		mainPane.add(nonSNPRB, c);
		
		c.gridx = 1;
		c.weightx = 1;
		mainPane.add(nonSNPFile,c);
		
		c.gridx = 2;
		c.weightx = 0;
		mainPane.add(nonSNPButton, c);
		
		c.gridy = 2;
		c.gridx = 0;
		c.weightx = 0;
		mainPane.add(SNPRB, c);
		
		c.gridx = 1;
		c.weightx = 1;
		mainPane.add(SNPFile,c);
		
		c.gridx = 2;
		c.weightx = 0;
		mainPane.add(SNPButton, c);
		
		c.gridx = 3;
		mainPane.add(new JLabel("(-map)"), c);
		
		c.gridx = 4;
		c.weightx = 1;
		mainPane.add(mapFile, c);
		
		c.gridx = 5;
		c.weightx = 0;
		mainPane.add(mapButton, c);
		
		c.gridy = 3;
		c.gridx = 0;
		c.weightx = 1;
		c.gridwidth = 6;
		mainPane.add(buttonPane, c);
		}
	
	/**
	 * Create a Dialog to open a Haploview instance.
	 * @param mf The GPLINK instance that this dialog
	 * is attached to.
	 * @param givenOp A String that 
	 * @param givenFile
	 */
	public OpenHapDialog(GPLINK mf, 
			String givenOp, String givenFile){
		super(mf, "Open File in Haploview");
		//set the GPLINK
		frame = mf;
		//create the panel
		createPanel();
		//populate the fields with the given options
		populateFields(givenOp, givenFile);
		//show everything off
		pack();
		setVisible(true);
	}

}
