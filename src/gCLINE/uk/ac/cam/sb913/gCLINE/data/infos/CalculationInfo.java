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
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;

/**
 * A class that contains the information for a given
 * calculation, ie a single command typed at the command line.
 * <p>This includes 1) a unique name 2) associated command
 * line 3) a description 4) input files 5) output files.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class CalculationInfo extends OperationInfo
							implements KeyWords{
	public static String INPUT_LABEL = "Input files";
	public static String OUTPUT_LABEL = "Output files";

	protected String description;
	/**
	 * A String that holds the command line for this 
	 * calculation.
	 */
	protected String cline;
	/**
	 * A DefaultMutableTreeName that holds the input files.
	 */
	protected DefaultMutableTreeNode input;
	/**
	 * A DefaultMutableTreeNode that holds the output files.
	 */
	protected DefaultMutableTreeNode output;
	
	/**
	 * Return the command line text for this calculation.
	 * @return
	 */
	public String getCline(){
		return cline;
	}

	public ArrayList<OperationInfo> getOpChildren() {
		ArrayList<OperationInfo> arlist = new ArrayList<OperationInfo>();
		arlist.add(this);
		return arlist;
	}
	
	public ArrayList<FileInfo> getInputFiles(){
		ArrayList <FileInfo> ans = new ArrayList<FileInfo>();
		
		if(input.isLeaf())
			return ans;
		
		FileInfo f = (FileInfo) input.getFirstChild();
		
		while(f != null){
			ans.add(f);
			f = (FileInfo)f.getNextSibling();
		}
		
		return ans;
	}
	
	public ArrayList<FileInfo> getOutputFiles(){
		ArrayList <FileInfo> ans = new ArrayList<FileInfo>();
		
		if(output.isLeaf())
			return ans;
		
		FileInfo f = (FileInfo) output.getFirstChild();
		
		while(f != null){
			ans.add(f);
			f = (FileInfo) f.getNextSibling();
		}
		
		return ans;
	}
	

	/**
	 * Get the log file associated with this calculation
	 * @param localFolder A file reflecting the local 
	 * reference for this project
	 * @param suffix A String that is the log suffix (example: .log)
	 * @return A File that is the local log file, null if none found
	 */
	public File getLog(File localFolder, String suffix){
		ArrayList <FileInfo> allOutput = getOutputFiles();
		for(FileInfo f : allOutput){
			if(f.toString().endsWith(suffix))
				return new File(localFolder, f.toString());
		}
		return null;
	}
	
	public static File getLog(File localFolder, 
			String suffix, ArrayList <String[]> outfiles){
		for(String [] f : outfiles){
			if(f[0].endsWith(suffix))
				return new File(localFolder, f.toString());
		}
		return null;
	}

	/**
	 * Removes a file from the operation. Note that this
	 * removes all instances of this file.
	 * @param filename A String that is the file name we wish
	 * to remove from this operation.
	 */
	public void removeFile(String filename){
		//take it out of the input node
		FileInfo f = (FileInfo) input.getFirstChild();
		//go through all the files of the input node
		while(f != null){
			//check the file
			if(filename.equals(f.toString())){
					//remove the node
					f.removeFromParent();
					//notify the tree that the node
					//...has changed
					if(parentTree != null)
						parentTree.nodeStructureChanged(input);
			}
			//check the rest of the files
			f = (FileInfo)f.getNextSibling();
		}
		
		//take it out of the output note
		f = (FileInfo) output.getFirstChild();
		
		while(f != null){
			if(filename.equals(f.toString())){
				f.removeFromParent();
				if(parentTree != null)
					parentTree.nodeStructureChanged(output);
		
			}
			f = (FileInfo)f.getNextSibling();
		}
		
	}
	
	/**
	 * Add a file to one of the file nodes.
	 * @param type A String flag from KeyWords that flags
	 * this file as either an input or an output.
	 * @param given_name A String that holds the file name.
	 * @param localDesc A String that is the description
	 * for this file local to this operation.
	 * @param globalDesc A String that is the description
	 * for this file globally.
	 */
	@Override
	public void addFile(String type, String given_name, 
			String localDesc, String globalDesc){
		
		//check the conditions
		if(globalDesc == null)
			globalDesc = "";
		if(localDesc == null)
			localDesc = "";
		if(given_name == null)
			return;
		
		//add the file to the appropriate node
		if(type.equals(INFILE_KEY)){
			addFileToNode(input, new FileInfo(given_name, localDesc, globalDesc));
		}
		if(type.equals(OUTFILE_KEY)){
			addFileToNode(output, new FileInfo(given_name, localDesc, globalDesc));
		}
	}
	
	/**
	 * Add a file to a given node.
	 * @param node A DefaultMutableTreeNode that represents either then input
	 * or output node for this operation.
	 * @param givenFile A FileInfo that is the file we wish to add to the given
	 * node.
	 */
	private void addFileToNode(DefaultMutableTreeNode node, FileInfo givenFile){
		//check that there are files in this folder
		// replace existing file if one is present with the same name
		if(!node.isLeaf()){
			FileInfo index = (FileInfo) node.getFirstChild();
			//look through all the files
			while(index != null){
				//if it's already there
				if((index.toString()).equals(givenFile.toString())){
					//FileInfo temp = (FileInfo)index.getPreviousSibling();
					//remove the old file	
					index.removeFromParent();
					//index = temp;
				}
				//move on to the next index
				index = (FileInfo) index.getNextSibling();
			}
		}
		//add it to the node
		node.add(givenFile);
		if(parentTree != null)
			parentTree.nodeStructureChanged(node);
	}
	
	/**
	 * Get the details of the operation 
	 * like the command line.
	 * @return a ArrayList containing the deals of the 
	 * operation
	 */
	public ArrayList <String> getOpDetails(){
		ArrayList <String> ans = new ArrayList <String> ();
		ans.add(cline);
		return ans;
	}
	
	/**
	 * Print out the operation to a string.
	 * @return The entire operation as a flat string.
	 */
	public String printfull(){
		String ans = this.toString() + " \n";
		
		ArrayList <String> details = getOpDetails();
		for(String d: details){
			ans = ans + d + " \n";
		}
		
		ans = ans + "Input Files:\n" ;
		FileInfo fileIndex = (FileInfo)input.getFirstChild();
		while(fileIndex != null){
			ans = ans + fileIndex.toString() + "\n";
			fileIndex = (FileInfo)fileIndex.getNextSibling();
		}//end input while
		
		
		ans = ans + "Output Files:\n";
		fileIndex = (FileInfo)output.getFirstChild();
		while(fileIndex != null){
			ans = ans + fileIndex.toString() + "\n";
			fileIndex = (FileInfo) fileIndex.getNextSibling();
		}//end output while
		return ans;
	}

	public Element asElement(Document d){
		Element op = d.createElement(CALC_KEY);
		op.setAttribute(NAME_KEY, name);
		op.setAttribute(CLINE_KEY, cline);
		op.setAttribute(TIMESTAMP_KEY, timestamp);
		
		op.appendChild(d.createTextNode(description));
		
		Element ins = d.createElement(INFILE_KEY);
		Element outs = d.createElement(OUTFILE_KEY);
		
		FileInfo fileIndex;
		if(!input.isLeaf()){
			fileIndex = (FileInfo)input.getFirstChild();
			while(fileIndex != null){
				ins.appendChild(fileIndex.asElement(d));
				fileIndex = (FileInfo)fileIndex.getNextSibling();
			}//end input while
		}
		if(!output.isLeaf()){
			fileIndex = (FileInfo)output.getFirstChild();
			while(fileIndex != null){
				outs.appendChild(fileIndex.asElement(d));
				fileIndex = (FileInfo) fileIndex.getNextSibling();
			}//end output while
		}
		op.appendChild(ins);
		op.appendChild(outs);
		return op;
	}

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

	public void execute(Record data) {
		data.updaterOperation(name);
		System.out.println("Now I'm executing myself :( " + name);
		new Thread(new RunCommand(cline, data)).start();
	}

	public void finish(Record data, boolean success) {
		// TODO this um really. What if the only item in a project is a queue?
		if (((DefaultMutableTreeNode)this.getParent()).getSiblingCount()>1) { // NB nodes are their own siblings, so even the root has 1 sibling - itself.
			// This is one in a queue. Execute the next
			((OperationInfo)(this.getParent())).finish(data, success);
		}
	}
	
	/**
	 * Create a new CalculationInfo from name, description, command, and time of creation.
	 * @param givenName the name of the calculation
	 * @param givenDesc a description of the calculation
	 * @param command the command line string for this calculation
	 * @param time the time of creation of this calculation
	 * @param givenParentTree the parent tree of this calculation
	 */
	public CalculationInfo (String givenName, 
			String givenDesc, String command,
			String time, DefaultTreeModel givenParentTree) {
		super(givenName, givenDesc, time, givenParentTree);
		addClineNode(command);
		addInputNode();
		addOutputNode();
		if(time == null){
			stampTime();
		} else
			timestamp = time;

		//check assumptions
		if(name == null || description == null || cline == null) {}
			//throw new Exception("[OperationInfo] ERROR: Null arguments!");
			// Throw an exception (TODO CreateQueue and Execute need to know how to catch this!
		
	}

	public CalculationInfo (String givenName, String givenDescript, String command, 
		String time, ArrayList<String[]> ifiles, ArrayList<String[]> ofiles, DefaultTreeModel givenParentTree) {
		super(givenName, givenDescript, time, givenParentTree);
		cline = command;

		System.out.println("New CalculationInfo empty: " + givenName + ", " + givenDescript + ", " + command);

		ArrayList <String[]> outfiles = new ArrayList <String[]> ();
		ArrayList <String[]> infiles = new ArrayList <String[]> ();

		addClineNode(command);
		addInputNode();
		addOutputNode();
		stampTime();

		fillFiles(infiles, INFILE_KEY);
		fillFiles(outfiles, OUTFILE_KEY);
        System.out.println("Done on the creation!");
	}

	// Parse command
	public CalculationInfo (String command, String givenDescription, DefaultTreeModel givenParentTree) {
		super(givenParentTree);
		
		// TODO make this accept non-PLINK Operations by recognising the --gplink flag and accordingly ignoring the parse loop
		System.out.println("New CalculationInfo parsing: " + givenDescription + ", " + command);

		//split the command based on spaces outside of quotes
		String [] parsedCmd = RunCommand.stripAndSplit(command);

		ArrayList <String[]> outfiles = new ArrayList <String[]> ();
		ArrayList <String[]> infiles = new ArrayList <String[]> ();

		String opName = null;
		
		// Run through each element of the command; if we see a flag, then
		// interpret the next element accordingly.

		boolean flag_bfile = false;
		boolean flag_file = false;
		boolean flag_out = false;
		
		for(String element: parsedCmd){
			if(flag_out){
				opName = FileInfo.fileName(element);
				flag_out = false;
			}else if(flag_bfile){ 
				// NB if both --bfile and --file have been flagged, only
				// binary files will be used as they are faster

				infiles.add(new String[] {element + ".bim", ""});
				infiles.add(new String[] {element + ".bed", ""});
				infiles.add(new String[] {element + ".fam", ""});
				flag_bfile = false;
			}else if(flag_file){
				infiles.add(new String[] {element + ".map", ""});
				infiles.add(new String[] {element + ".ped", ""});
				flag_file = false;
			} else if(element.contains(".")
					&& !element.endsWith(".exe")
					&& !element.endsWith(".exe\"")
					&& !element.endsWith("plink")
					&& !element.endsWith("plink\"")
					&& !element.matches("^\\d*\\.?\\d+$")){
				infiles.add(new String[] {element, ""});
			}
			flag_file = element.equals("--file");
			flag_bfile = element.equals("--bfile");
			flag_out = element.equals("--out");
		}
		
		name = opName;
		description = givenDescription;
		addClineNode(command);
		addInputNode();
		addOutputNode();
		stampTime();

		fillFiles(infiles, INFILE_KEY);
		fillFiles(outfiles, OUTFILE_KEY);
    }
	
	private void addClineNode(String command) {
		cline = command;
		add(new DefaultMutableTreeNode(cline));
	}
	
	private void addInputNode() {
		input = new DefaultMutableTreeNode(INPUT_LABEL);
		add(input);
	}
	
	private void addOutputNode() {
		output = new DefaultMutableTreeNode(OUTPUT_LABEL);
		add(output);
	}
	
	private void stampTime() {
		java.util.Date date = new java.util.Date();
        timestamp = DATEFORMAT.format(date);
	}
	
	private void fillFiles(ArrayList<String[]> files, String key) {
		for(String[] file: files){
			System.out.println("Now filling files " + file[0] + " and " + file[1]);
			//addFile(key, file[0], file[1], ((Record)parentTree).globalFileNotes.get(file[0]));
			addFile(key, file[0], file[1], FileInfo.fileName(file[0]));
			System.out.println("Now adding file " + file[0]);
		}
	}



}
