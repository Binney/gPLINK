package gCLINE.uk.ac.cam.sb913.gCLINE.data.infos;

import java.io.File;
import java.util.ArrayList;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;

import javax.swing.tree.DefaultTreeModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
  * A class to hold information about multiple operations
  * in a queue. Extends OperationInfo and has many CalculationInfo
  * child nodes.
  */
@SuppressWarnings("serial")
public class QueueInfo extends OperationInfo {

	/**
	 * The index of the child operation which is currently being executed.
	 * Note that this isn't stored in the project XML file; it's calculated
	 * again each time the queue is read via looking at which operations 
	 * have completed.
	 */
	private int currentTask;
	
	public ArrayList<OperationInfo> getOpChildren() {
		ArrayList <OperationInfo> ans = new ArrayList<OperationInfo>();
		ans.add((OperationInfo)this);
		// This relies on the fact that the only children of a QueueInfo will be its operations.
		int numOp = this.getChildCount();
		for(int i = 0; i < numOp; i ++){
			OperationInfo child = (OperationInfo)this.getChildAt(i);
			ans.add(child);
		}
		return ans;
	}	
	
	/**
	 * Print out the operation to a string.
	 * @return The entire operation as a flat string.
	 */
	public String printfull(){
		String ans = "";
		// TODO also add the error tolerance options and so on
		ArrayList <OperationInfo> ops = getOpChildren(); // NB this contains the queue itself as well as all children
		for(OperationInfo op: ops){
			ans = ans + op.toString() + " \n";
		}

		return ans;
	}	
	
	@Override
	public Element asElement(Document d){
		Element q = d.createElement(QUEUE_KEY);
		q.setAttribute(NAME_KEY, name);
		q.setAttribute(TIMESTAMP_KEY, timestamp);
		
		q.appendChild(d.createTextNode(description));
		
		// TODO presumably will also want to append extra conditions here, eg error tolerance options

		Element ops = d.createElement(CALC_KEY);
		
		CalculationInfo calcInfo;
		if(!this.isLeaf()){
			calcInfo = (CalculationInfo)this.getFirstChild();
			while (calcInfo != null) {
				ops.appendChild(calcInfo.asElement(d));
				calcInfo = (CalculationInfo)calcInfo.getNextSibling();
			}
		}

		q.appendChild(ops);
		return q;
	}
	
	/**
	 * Get the log file associated with whichever calculation
	 * this queue is executing at the moment.
	 * @param localFolder A file reflecting the local 
	 * reference for this project
	 * @param suffix A String that is the log suffix (example: .log)
	 * @return A File that is the local log file, null if none found
	 */
	public File getLog(File localFolder, String suffix){
		CalculationInfo mostRecentOp = (CalculationInfo)this.getChildAt(currentTask);
		ArrayList <FileInfo> allOutput = mostRecentOp.getOutputFiles();
		for(FileInfo f : allOutput){
			if(f.toString().endsWith(suffix))
				return new File(localFolder, f.toString());
		}
		return null;
	}
	
	public void addFile(String type, String given_name, 
			String localDesc, String globalDesc) {
		// Do nothing. Queues don't have files. TODO
	}
	
	public boolean addCalculation (CalculationInfo calcInfo) {
		try {
			// place new operation at end of tree if not present already
			//int index = this.getChildCount();
			System.out.println("[QueueInfo.addCalculation(CalculationInfo)] adding calculation " + calcInfo.getName() + " to end of branch");
			//this.parentTree.insertNodeInto(calcInfo, this, index);
			this.add(calcInfo);
			System.out.println("Just added calculation which is a " + this.getChildAt(this.getChildCount()).getClass().getSimpleName());
		} catch (Exception e) {
			System.out.println("Oh noes!");
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean addCalculation (String cline, String description) {
		try {
			System.out.println("Creating an opinfo from " + cline + " and " + description);
			CalculationInfo calcInfo = new CalculationInfo(cline, description, null);
			System.out.println("Adding " + calcInfo.getCline() + " which is a " + calcInfo.getClass().getSimpleName());
			this.addCalculation(calcInfo);
		} catch (Exception e) {
			System.out.println("Seriously?!");
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean addCalculation(String givenName, String givenDesc, String cline, String time) {
		try {
			CalculationInfo calcInfo = new CalculationInfo(givenName, givenDesc, cline, time, parentTree);
			System.out.println("About to add " + calcInfo.getName() + " which is a " + calcInfo.getClass().getSimpleName());
			this.addCalculation(calcInfo);
		} catch (Exception e) {
			System.out.println("Dangit!");
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public void execute(Record data) {
		System.out.println("Now adding " + name);
		data.updaterOperation(name);
		// execute next child
		System.out.println("and executing number " + Integer.toString(currentTask));
		((CalculationInfo)this.getChildAt(currentTask)).execute(data);
	}

	@Override
	public void finish(Record data, boolean success) {
		// TODO um?
		currentTask += 1;
		if (currentTask < this.getChildCount()) {
			this.execute(data);
		} else {
			writeSuccessLog(success);
		}
	}
	
	private void writeSuccessLog(boolean success) {
		// TODO write log file with relevant success code
	}
	
	public QueueInfo (DefaultTreeModel givenparentTree) {
		super(givenparentTree);
	}

	public QueueInfo (String givenName, 
			String givenDesc, String time, DefaultTreeModel givenParentTree) {
		super(givenName, givenDesc, time, givenParentTree);

	}
	public QueueInfo (String givenName, 
			String givenDesc, DefaultTreeModel givenParentTree) {
		super(givenName, givenDesc, null, givenParentTree);

	}


}