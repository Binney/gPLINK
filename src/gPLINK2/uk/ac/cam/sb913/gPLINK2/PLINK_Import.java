package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;

/**
 * An extention of JDialog that imports a new command into OpView
 * from a log file.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class PLINK_Import extends JDialog {
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(PLINK_Import.class);
	
	/**
	 * The StartFrame that this dialog is attached
	 * to.
	 */
	private StartFrame frame;
	/**
	 * The Project that the operation is added to.
	 */
	private Project data;
	/**
	 * A combo box that contains all the
	 * log file roots that are not already
	 * in the operation tree.
	 */
	private JComboBox nonincludedRoots;
	/**
	 * A JCheckBox that flags selection of
	 * all the non-included roots.
	 */
	private JCheckBox allBox;
	/**
	 * A ArrayList that holds all non-included roots
	 */
	private ArrayList <String> finalLogRoots;
	
	/**
	 * A boolean flag that is true if the operation is done
	 * and false otherwise.
	 */
	private enum ExitStatus {SUCCESS, FAILURE, RUNNING}
	
	private ExitStatus done;
	/**
	 * Pulls an array of strings identifying the input files
	 * @param cline a command line string. Any file names
	 * with spaces must be quoted.
	 * @return an array of strings identifying the input files
	 */
	private ArrayList <String[]> getInput(String cline){
		//a hash set that allows us to dynamically dump the input files
		//...we find, it also has the advantage of not double listing
		//...the input files
		HashSet<String> dynamicAns = new HashSet<String>();
		
		//split the commandline up based on empty spaces outside of quotes
		//...and strip the quotes
		String [] commandArray = RunCommand.stripAndSplit(cline);
		//go through the command
		for(int i = 0; i < commandArray.length; i ++){
			//find files that have . in it signalling a file
			if(commandArray[i].contains(".") 
					//unless it's a number
					&& (!commandArray[i].matches("\\d*.?\\d*"))
					//or an executable
					&& (!commandArray[i].endsWith(".exe"))
					&& (!commandArray[i].endsWith(".exe\""))
					//or ends in plink
					&& (!commandArray[i].endsWith("plink"))
					&& (!commandArray[i].endsWith("plink\""))){
				//add strings that have extensions that are not
				//...executable
				dynamicAns.add(commandArray[i]);
			}
			//if we run across the short binary form
			if(commandArray[i].equals("--bfile")){
				//default to plink
				String tempRoot = "plink";
				//if we don't walk off the end of the array
				if( (i+1) < commandArray.length)
					//set the file root
					tempRoot = commandArray[i+1];
				//but switch it back to the default if it
				//...starts with --
				if(tempRoot.startsWith("--"))
					tempRoot = "plink";
				//add the three binary input files based
				//...on the file root
				dynamicAns.add(tempRoot + ".bed");
				dynamicAns.add(tempRoot + ".fam");
				dynamicAns.add(tempRoot + ".bim");
			}
			//if we run across the short standard form
			if(commandArray[i].equals("--file")){
				//default to plink
				String tempRoot = "plink";
				//if we don't walk off the end of the array
				if( (i+1) < commandArray.length)
					//set the file root
					tempRoot = commandArray[i+1];
				//but switch it back to the default if it
				//...starts with --
				if(tempRoot.startsWith("--"))
					tempRoot = "plink";
				//add the two standard input files based
				//...on the file root
				dynamicAns.add(tempRoot + ".map");
				dynamicAns.add(tempRoot + ".ped");
			}
		}

		//make the answer
		ArrayList <String[]> ans = new ArrayList <String[]>();
		for(String s: dynamicAns){
			ans.add(new String[] {s, ""});
		}
		
		//return the answer
		return ans;
	}
	/**
	 * Get the command line based on the log file.
	 * @param outputRoot A String that is the file root for our
	 * log file.
	 * @return A String that is a command line in this log file.
	 */
	private String getCommandline(String outputRoot){
		
		done = ExitStatus.RUNNING;
		//get the log file
		File logFile = new File(data.getLocalFolder().getAbsolutePath(),
				outputRoot+".log");
		//start the command line with the local plink command
		String cline = FileInfo.quote(data.getPlinkPath());
		//try to read the log file
		try {
			//set up a buffered file reader
			BufferedReader input = new BufferedReader(new FileReader(logFile));
			//start reading the line
			String line = input.readLine();
			//keep reading until we run out of lines
			while(line !=null){
				//look for the line that flags the commandline
				//...options in effect
				if(line.contains("Options in effect")){
					//read the next line
					line = input.readLine();
					//loop through until we are either at the
					//...end of the file or until we are
					//...at a blank line
					while(line != null
							&& !line.trim().equals("")){
						//trim the line
						line = line.trim();
						logger.info("Reading line[" + line + "]");
						
						//split the line on the spaces
						String [] temp = line.split("\\s");
						//if the line has 1 or 2 elements
						if(temp.length <= 2){
						//	add the original line to the command line
							cline += " " + line.trim();
							logger.info("Simple add");
						}
						//otherwise
						else{
						//	find the first index of the space in the line
							int index = line.indexOf(" ");
						//	get the substring from index + 1
							String filename = line.substring(index + 1);
						//	check to see if this contains a .
							if(filename.contains(".") 
									//unless it's a number
									&& (!filename.matches("\\d*.?\\d*"))){
						//	if it does then process it as a filename
								cline +=  " " + temp[0] + " " + FileInfo.quote(filename);
								logger.info("adding file: ["+FileInfo.quote(filename)+"]");
						//	otherwise add the orginal line to the command line
							}else{
								logger.info("Simple add");
								cline += " " + line.trim();
							}
								
						}
						//*************old
						////add this line to the command line
						//cline += " " + line.trim();
						//go to the next line
						line = input.readLine();
					}
					
				}
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO NB this is the main assumption of gPLINK: that the output
				// from PLINK will contain these lines depending on success or failure
				// may need extra stuff
				// also can put in progress bar stuff here, or near here
				//check to see if the log file is done
				if(line.contains("Analysis finished")){
					done = ExitStatus.SUCCESS;
				}
				if(line.contains("ERROR")){
					done = ExitStatus.FAILURE;
				}
				//go on to the next line
				line = input.readLine();
			}
			//close everything up
			input.close();
			//check for exceptions
		} catch (FileNotFoundException e) {
			frame.messenger.createError("File not found exception: " +
					"[" + logFile.getAbsolutePath() + "]",
					"process(String)@PLINK_Import.java");
		} catch(IOException e2){
			frame.messenger.createError("IO exception reading: " +
					"[" + logFile.getAbsolutePath() + "]",
			"process(String)@PLINK_Import.java");
		}
		//if we make it here then the command line probably
		//...doesn't contain anything.
		return cline;
	}
	/**
	 * Take the options selected on the form and add an appropriate
	 * operation.
	 *
	 */
	
	private void process(){
		/*
		 * Get the roots that we are importing
		 */
		ArrayList <String> selectedRoots = new ArrayList<String>();
		//are we importing everything or just one?
		if(allBox.isSelected())
			selectedRoots = finalLogRoots;
		else
			selectedRoots.add((String)nonincludedRoots.getSelectedItem());
		
		//if we are remote then pull down each logfile
		if(data.isRemote()){
			logger.info("[process()] pulling down the selected log files");
			String[] allLog = new String [selectedRoots.size()];
			for(int i = 0; i < selectedRoots.size(); i++){
		    	allLog[i] = data.getRemoteFolder() + selectedRoots.get(i) + ".log";
			}

			//note that we want to be notified if this fails
			(frame.new Download(data, false, data.getLocalFolder(), allLog)).run();
		}
		
		//Get the time stamps for the remote files
		HashMap<String, String> fileTimes = new HashMap<String, String>();
		if(data.isRemote()){
			RunCommand getFileList = new RunCommand("ls --full-time " + data.getRemoteFolder(), data);
			getFileList.run();
			ArrayList <String> fullRemoteFiles = getFileList.outputLines;
			for(String line: fullRemoteFiles){
				String [] t = line.split("\\s+");
				if(t.length > 8){
					String file = t[8];
					String date = t[5] + " " + t[6].substring(0,8);
					fileTimes.put(file, date);
				}
			}
		}
		/*
		 * Process each root
		 */
		for(String root: selectedRoots){
			//if it's remote we assume that we had pulled the latest log
			//...file in our previous update cycle
		    
			//pull the info from the associated log file
			String cline = getCommandline(root);
			//get the input files from the commandline
			ArrayList <String[]> infiles = getInput(cline);
			//generate the timestamp for the operation
			String time = "No time found";
			if(data.isRemote()){
				time = fileTimes.get(root + data.getLogExt());
				if(time == null)
					time = "No time found";
			}else{
				time = OperationInfo.DATEFORMAT.format(new Date(
					new File(data.getLocalFolder(), root + data.getLogExt()).lastModified()));
			}
			//add a new operation based on this
			data.addOperation(root, "", cline, time,
					infiles, new ArrayList<String[]> (), null);
			
			//look to see what the status of the operation is
			if(!done.equals(ExitStatus.RUNNING)){
				try {
					//get the file that flags the exit status of the operation
					File gplinkfile = new  File(data.getLocalFolder(),
							root + Project.GPLINK_EXT);
					//try to create the gplink file if it doesn't already exist
					if(gplinkfile.createNewFile()){
						//write the successful completion to the gplink file
						BufferedWriter out = new BufferedWriter(new FileWriter(gplinkfile));
						if(done.equals(ExitStatus.SUCCESS))
							// write a success code
							out.write("0");
						else
							//otherwise write a failure code
							out.write("1");
					    //close it all up
					    out.close();
					    //if it's a remote file then copy it to the remote folder
					    if(data.isRemote()){
					    	//note that we want to be notified if this fails
					    	new Thread(frame.new Upload(data, false, 
					    			data.getRemoteFolder(),
					    			new File[]{ gplinkfile})).start();
						}
					    
					}

				} catch (IOException e) {
					frame.messenger.createError("Error trying to create the .gplink file " +
							"for imported operation.", "process()@PLINK_Import.java");
				}
			}
		}
		
	}
	/**
	 * Create a JPanel that contains the JComboBox with the
	 * appropriate file root options.
	 * @return A JPanel that gives the user the option of selecting
	 * a specific file root to import.
	 */
	private JPanel createRoots(){
		
		//create a ArrayList that holds all the know operations
		ArrayList <OperationInfo> knownOps = data.getAllOp();
		ArrayList <String> knownOpNames = new ArrayList <String> ();
		for(OperationInfo temo: knownOps){
			knownOpNames.add(temo.getName());
		}
		//create a ArrayList that holds all the know files in the
		//...home directory
		ArrayList <String> allFiles = data.getHomeFiles(); 
		
		finalLogRoots = new ArrayList <String>();
		
		//filter out the log files from the files in our home
		//...folder
		for(String file: allFiles){
			//check for the log file
			if(file.endsWith(data.getLogExt())){
				//grab the log root
				String logRoot = 
					file.substring(0, 
							file.length() - data.getLogExt().length());
				//flag the operation to be added if it's not already known
				if(!knownOpNames.contains(logRoot))
					finalLogRoots.add(logRoot);	
			}
		}
		
		//sort things alphabetically
		Collections.sort(finalLogRoots);
		//populate the combo box
		nonincludedRoots = new JComboBox<String>(finalLogRoots.toArray(new String[finalLogRoots.size()]));
		//this is a hack to get around a java 1.5 bug
		//http://forum.java.sun.com/thread.jspa?threadID=678410&messageID=3960925
		
		//ok this is a strange bug. if it's set to true I don't
		//...see the drop down list. -10.17.07
		nonincludedRoots.setEditable(false);
		//create the pane we will return
		JPanel comboPane = new JPanel();
		//lay everything out on the page axis
		comboPane.setLayout(new BoxLayout(comboPane, BoxLayout.PAGE_AXIS));
		//add a label to explain everything.
		comboPane.add(new JLabel("Select analysis to import:"));
		//add the combo box to the panel
		comboPane.add(nonincludedRoots);
		//add the check box that will flag all roots for inclusion
		allBox = new JCheckBox("Import all operations.");
		comboPane.add(allBox);
		return comboPane;
	}
	/**
	 * Create the JButtons for this form.
	 * @return A JPanel that contains the ok and cancel
	 * JButtons.
	 */
	private JPanel createButtons(){
		//initialise the buttons
		JButton okbutton = new JButton("OK");
		JButton cancelbutton = new JButton("Cancel");
		
		//add the action listeners
		cancelbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				dispose();}}
		);
		okbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				process();
				dispose();}}
		);
		
		//add the buttons to that pane
		JPanel buttonPane = new JPanel();
		buttonPane.add(okbutton);
		buttonPane.add(cancelbutton);
		return buttonPane;
	}

	/**
	 * Create a JDialog that will allow the user to input
	 * plink operations from log files in their home directory.
	 * @param mf a StartFrame this dialog is attached to and
	 * unloads/downloads with.
	 * @param d a Project that the imported operations are added to.
	 */
	public PLINK_Import(StartFrame mf, Project d){
		super(mf, "Import Analysis");
		//initialise the class variables
		frame = mf;
		data = d;
		//setup and lay everything out
		getRootPane().setLayout(new BoxLayout(getRootPane(), BoxLayout.PAGE_AXIS));
		getRootPane().add(createRoots());
		getRootPane().add(createButtons());
		//show it off
		pack();
		setVisible(true);
	}
}
