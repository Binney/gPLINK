package gCLINE.uk.ac.cam.sb913.gCLINE.data.infos;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.KeyWords;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;

/**
 * A class that contains the information for a given
 * operation.
 * <p>This includes 1) a unique name 2) associated command
 * line 3) a description 4) input files 5) output files.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public abstract class OperationInfo extends DefaultMutableTreeNode
							implements KeyWords{

	/**
	 * A String that uniquely identifies the operation.
	 */
	protected String name;
	
	/**
	 * A String that describes the operation.
	 */
	protected String description;

	/**
	 * A String holding when the ExecutableInfo was created.
	 */
	protected String timestamp;
	
	static public DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
    
	protected DefaultTreeModel parentTree;
	
	/**
	 * Get the unique name of the operation. Note: unique! Two operations in one project cannot have the same name.
	 * @return A String that is the operation's name.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns a human-readable description of this ExecutableInfo. 
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Rounds up all the operations attached to this Executable, which will
	 * be either an arraylist containing just this class or an arraylist
	 * containing a queue and all its children.
	 * @return
	 */
	public abstract ArrayList<OperationInfo> getOpChildren();	

	/**
	 * Get the log file associated with this operation
	 * @param localFolder A file reflecting the local 
	 * reference for this project
	 * @param suffix A String that is the log suffix (example: .log)
	 * @return A File that is the local log file, null if none found
	 */
	public abstract File getLog(File localFolder, String suffix);
	
	public static File getLog(File localFolder, 
			String suffix, ArrayList <String[]> outfiles){
		for(String [] f : outfiles){
			if(f[0].endsWith(suffix))
				return new File(localFolder, f.toString());
		}
		return null;
	}
	
	/**
	 * Returns when this executableinfo was created.
	 * @return
	 */
	public String getTimeStamp(){
		return timestamp;
	}
	
	/**
	 * Get the name and description of this
	 * operation.
	 * @return A String that is the name and description
	 * of this operation seperated by ":".
	 */
	@Override
	public String toString(){
		return name + ": ("+ timestamp + ") " + description;
	}
	
	/**
	 * Print out the operation to a string.
	 * @return The entire operation as a flat string.
	 */
	public abstract String printfull();

	public abstract Element asElement(Document d);

	public boolean clineIsValid(String cmd) {
		// TODO check for other ways in which a cline command might be invalid?
		String [] parsedCmd = RunCommand.stripAndSplit(cmd);
		String opName = "";
		boolean flag_out = false;
		for (String element: parsedCmd) {
			if (flag_out) {
				opName = FileInfo.fileName(element);
				flag_out = false;
			}
			flag_out = element.equals("--out");
		}
		return (opName != "");
	}

	public abstract void addFile(String type, String given_name, 
			String localDesc, String globalDesc);
	
	/**
	 * Execute this ExecutableInfo.
	 * @param data
	 */
	public abstract void execute(Record data);

	/**
	 * Called when the operation is complete or a single part of the queue is done.
	 * @param data
	 * @param success boolean whether the operation succeeded without errors.
	 */
	public abstract void finish(Record data, boolean success);

	public OperationInfo (DefaultTreeModel parentTree){
		this("", "", null, parentTree);
	}
	
	/**
	 * Create a new OperationInfo from name, description, command, and time of creation.
	 * @param givenName the name of the operation
	 * @param givenDesc a description of the operation
	 * @param command the command line string for this operation
	 * @param time the time of creation of this operation
	 * @param givenParentTree the parent tree of this operation
	 */
	public OperationInfo (String givenName, 
			String givenDesc, String time, DefaultTreeModel givenParentTree) {
		super(givenParentTree, true);
		parentTree = givenParentTree;
		name = givenName;
		description = givenDesc;
		if(time == null){
			java.util.Date date = new java.util.Date();
	        timestamp = DATEFORMAT.format(date);
		} else
			timestamp = time;
		
	}

}
