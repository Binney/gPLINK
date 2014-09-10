package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;

/**
 * AutoUpdater extends Timer, automatically updating
 * the output files attached in OpView.
 * @author Kathe Todd-Brown
 *
 */
public class AutoUpdater extends Timer {
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(AutoUpdater.class);
	/**
	 * Window that this updater is attached to.
	 */
	private StartFrame frame;
	/**
	 * The file extension that this updater looks at to
	 * determine the exit status of the log.
	 */
	private String op_status_ext;
	/**
	 * Flags a completed operation
	 */
	public static String COMPLETE = "Complete";
	/**
	 * Flags a running operation
	 */
	public static String RUNNING = "Running";
	/**
	 * Flags a failed operation
	 */
	public static String ERROR = "Failed";
	/**
	 * Flags an operation not yet executed but queued
	 */
	public static String QUEUED = "Queued";

	/**
	 * A hash map that keeps all the operations and their current status
	 * completed, running, or failed.
	 */
	private HashMap<String, String> opStatus;
	
	/**
	 * This classes access to the general data
	 */
	private Record data;
	
	/**
	 * Error messages
	 */
	private ErrorManager errors;
	
	private void getRemoteFiles(ArrayList<String> allfiles) {
		//logger.info("(readLogs.run()) Starting to copy files.");
		int numFiles = allfiles.size();
		ArrayList <String> temp = new ArrayList <String>();
		for(int i = 0; i < numFiles; i ++){
			String file = allfiles.get(i);
			//we want files that have the correct extension but
			//...also have non-trivial roots
			if((file.endsWith(data.getLogExt()) && !file.equals(data.getLogExt()))
					|| file.endsWith(op_status_ext) && !file.equals(op_status_ext)){
				if(!new File(data.getLocalFolder(), file).exists()){
					temp.add(data.getRemoteFolder() + file);
					logger.info("[run()@readLogs] [" +file + "] added to download queue.");
				}
			}
		}
		String [] filesToGet = new String[temp.size()];
		for(int i = 0; i < filesToGet.length; i ++){
			filesToGet[i] = temp.get(i);
		}
		
		//copy the file to the local project
		//Note that we are waiting to get all the files before
		//...moving on
		(frame.new Download(data, false, data.getLocalFolder(), 
				filesToGet)).run();
	}
	
	/**
	 * This runs every 5 seconds ideally but can be delayed if there isn't
	 * enough processor space.
	 * If the operation is still running then check for a .gplink file
	 * for the completion status. If a running operation is now completed
	 * get the current list of files in our project folder and compares
	 * them to the files listed as operations outputs. If there are files
	 * that are not in the output list but matches the operation root then
	 * it is added to the correct operation output.
	 */
	private TimerTask readLogs = new TimerTask(){
		@Override
		
		public void run() {
			logger.info("[readLogs] Entering...");
			
			//Error out if the operation status is null. This
			//...should never happen.
			if(opStatus == null){
				errors.createError("opStatus is null. Canceling the auto-updating", 
						"run@TimerTask@AutoUpdater.java");
				AutoUpdater.this.cancel();
				return;
			}
			ArrayList <String> allfiles = data.getHomeFiles();
			logger.info("[run()@readLogs] all the files are: " + allfiles.toString());
			//download all log and gplink files
			if(data.isRemote()){
				getRemoteFiles(allfiles);
			}
			
			// TODO the timer should also update the progress bars for task progress for (all/selected?) tasks

			//The following for loop takes a while the first time around
			//...but most of the time you will be copying only 1 or 2 new
			//...operations at a time so it doesn't really take much more
			//...time.
			
			//Go through each operation and check the status
			for(OperationInfo op: data.getAllOp()){
				checkOperationStatus(op, allfiles);
			}
			if(frame != null && frame.folderViewer != null)
				EventQueue.invokeLater(frame.folderViewer.new UpdateJList(false));
			
			data.saveInfo();
			
			logger.info("[readLogs] ...Ending");

		}
		
		
	};
	
	private void checkOperationStatus(OperationInfo op, ArrayList<String> allfiles) {
		String s = op.getName();
		if(s != null && opStatus.get(s) == null){
			// this is the first time the timer was run since the task began; add it
			// TODO need some way to tell if it's in a future queue - maybe get updater
			// to mark all tasks as queued until it actually sees a Thread,
			// or mark all new items seen after a running one as queued? the former
			// would be preferable as it doesn't require writing new stuff when a queue
			// is partway through execution
			logger.info("[run()@readLogs] ["+ s + "] now being flagged as Running");
			opStatus.put(s, RUNNING);
		}
		
		//Check that the operation is valid and is marked as running
		if(s != null && opStatus.get(s).equals(RUNNING)){
			logger.info("[run()@readLogs] [" + s + "] was running.");
			
			//the file that contains the status of the operation
			File gplinkStatusFile = new File(data.getLocalFolder(), s + op_status_ext);
			
			//if this file exists then the operation is done
			if(gplinkStatusFile.exists()){
				//initialise the setting to fail
				String setting = "1";
				BufferedReader in;
				try {
					in = new BufferedReader(new FileReader(gplinkStatusFile));
					setting = in.readLine();
					in.close();
				} catch (IOException e) {}
				
				for(String file: allfiles){
					//add all files in the current directory that have the correct root and 
					// don't end with op_status_ext
					if(file.startsWith(s + ".") && ! file.endsWith(op_status_ext))
						op.addFile(Record.OUTFILE_KEY, file, "", "");
				}
				
				//0 is a successful completion
				if(setting != null && setting.equals("0")){
					opStatus.put(s, COMPLETE);
					// move on to next operation in queue if applicable
					op.finish(data, true);
				} else {
					opStatus.put(s, ERROR);
					// move on to next operation in queue if applicable and error-tolerant
					op.finish(data, false);
				}
				data.nodeStructureChanged(data.getOp(s));
				
			} else{
				//the operation is still running
				opStatus.put(s, RUNNING);
				logger.info("[run()@readLogs] [" + s + "] is still running.");
				// TODO update progress bars by reading from log file; may need to process the exact format
				// of the progress updating bit depending on task
				File logFile = new File(data.getLocalFolder(), s + data.getLogExt());
			}
		}else {
			logger.info("[run()@readLogs] [" + s + "] is not flagged as running no files added.");
			
		}
	}
	
	/**
	 * runs the Timer's cancel and set the opStatus to null for
	 * garbage collection.
	 */
	@Override
	public void cancel(){
		super.cancel();
		opStatus = null;
		logger.info("[cancel()] AutoUpdater");
	}
	
	public void addOp(String opName){
		opStatus.put(opName, RUNNING);
	}
	
	/**
	 * Create and start our unique timer. This will update the
	 * status of our operations on the operations viewer.
	 * @param d the current project
	 */
	
	public AutoUpdater(StartFrame f, Record d, 
			String givenExt, ErrorManager e,
			int sec) {
		super();
		
		logger.info("(AutoUpdater(Record, String, ErrorManager))Intailizing the update");
		
		frame = f;
		data = d;
		errors = e;
		op_status_ext = givenExt;
		opStatus = new HashMap<String, String>();
		fillOpStatus();
		
		setTimer(sec);
		
	}
	
	private void fillOpStatus() {
		for (OperationInfo ops : data.getAllOp()) {
			opStatus.put(ops.getName(), RUNNING);
		}		
	}
	
	private void setTimer(int sec) {
		readLogs.run();
		
		long interval = sec*1000;	//time in milliseconds, 5 seconds
		
		// run readLogs every 5 or 30 seconds but you can bump it since
		// it's not critical if the processor is busy
		schedule(readLogs, 0, interval); 
	}

	/**
	 * Pull the operation name from the text in the operation
	 * node.
	 * @param s a string from the operation node in the form "name: description"
	 * @return a string containing the name of the operation
	 */
	private String processOpName(String s){
		//the operation name and the description are separated by a :
		//...so split the string at the :
		s = s.split(":")[0];
		//if we have an html flag
		if(s.contains("<html>"))
			//cut it off
			s = s.substring(new String ("<html>").length(), s.length());
		//return the operation name
		return s;
	}
    
	/**
	 * Create a renderer to reflect that status of each operation 
	 * (completed, failed or running).
	 * @author Kathe Todd-Brown
	 *
	 */
	@SuppressWarnings("serial")
	public class MyRenderer extends DefaultTreeCellRenderer {
        Icon sucessIcon;
        Icon failIcon;
        Icon runningIcon;
        Icon queuedIcon;
        Icon fileInfoIcon;
        /**
         * Create a new renderer
         * @param sicon the icon that flags the success status
         * @param ficon the icon that flags the failed status
         * @param ricon the icon that flags the running status
         * @param qicon the icon that flags the queued status
         * @param fileIcon the icon that represents a file
         */
        public MyRenderer(Icon sicon, Icon ficon, Icon ricon,
        		Icon qicon, Icon fileIcon) {
        	sucessIcon = sicon;
            failIcon = ficon;
            runningIcon = ricon;
            queuedIcon = qicon;
            fileInfoIcon = fileIcon;
        }

       /**
        * Set the icon of each node.
        */
        public Component getTreeCellRendererComponent(  JTree tree, 
        		Object value, boolean sel, boolean expanded, boolean leaf, 
        		int row, boolean hasFocus) {

            super.getTreeCellRendererComponent( tree, value, sel, expanded,
            		leaf, row, hasFocus);
            logger.info("[getTreeCellRendererComponent():MyRenderer] looking at"+ value.toString() 
            		+ ":" + opStatus.get(processOpName(value.toString())));
            if (isRunningOp(value)) {
            	setIcon(runningIcon);
            } else if (isCompleteOp(value)) {
            	 setIcon(sucessIcon);
            } else if (isFailedOp(value)) {
            	setIcon(failIcon);
            } else if(leaf == false 
            		&& isFile(value)){
            	setIcon(fileInfoIcon);
            } 
            return this;
        }

        private boolean isFile(Object value){
        	return (value != null 
        			&& !value.toString().equals("Input files")
        			&& !value.toString().equals("Output files"));
        }
        
        private boolean isRunningOp(Object value) {
        	if(opStatus != null
        			&& opStatus.get(processOpName(value.toString())) != null
        			&& opStatus.get(processOpName(value.toString())).equals(AutoUpdater.RUNNING))
        		return true;
            return false;
        }
        
        protected boolean isCompleteOp(Object value) {
        	if(opStatus != null
        			&& opStatus.get(processOpName(value.toString())) != null
        			&& opStatus.get(processOpName(value.toString())).equals(AutoUpdater.COMPLETE))            
            	return true;
            return false;
        }
        
        protected boolean isFailedOp(Object value) {
        	if(opStatus != null
        			&& opStatus.get(processOpName(value.toString())) != null
        			&& opStatus.get(processOpName(value.toString())).equals(AutoUpdater.ERROR))           
            	return true;
            return false;
        }

        protected boolean isQueuedOp(Object value) {
        	if(opStatus != null
        			&& opStatus.get(processOpName(value.toString())) != null
        			&& opStatus.get(processOpName(value.toString())).equals(AutoUpdater.QUEUED))
        		return true;
        	return false;
        }
    }
	
}
