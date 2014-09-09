package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.FileView;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.FolderView;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.OpView;

/**
 * General main window for the generalized command line
 * gui.
 * @author Kathe Todd-Brown
 *
 */
public abstract class StartFrame extends JFrame {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(StartFrame.class);
	/**
	 * A panel that views the associated log files.
	 */
	protected FileView fileViewer;
	/**
	 * A panel to view the files in your project folder.
	 */
	public FolderView folderViewer;
	/**
	 * A panel to look at the operation tree.
	 */
	protected  OpView opViewer;
	/**
	 * The version number associated with this gui.
	 */
	public String version;
	/**
	 * Set the version number.
	 */
	protected abstract void setVersion();
	/**
	 * The error manager for this gui.
	 */
	public ErrorManager messenger;
	/**
	 * A label that informs weather files are in
	 * transit from or to a remote directory,
	 * this is shown at the lower right hand side.
	 */
	protected JLabel statusBar;
	/**
	 * A ArrayList that stores the files currently
	 * being transmited.
	 */
	protected ArrayList<String> filesInTransit;
	/**
	 * An ActionListener that closers the gui.
	 */
	public ActionListener closing;
	/**
	 * A WindowListener that closes the gui properly.
	 */
	public WindowListener properClosing;
	/**
	 * Layout the panels; fileView, FolderView, OpView
	 * and add the file transfer status note.
	 *
	 */
	public abstract void layoutPanels();
	
	protected boolean isBrowseOnly;
	
	public abstract void setBrowseOnly(boolean state);
	
	public boolean getBrowseOnly(){
		return isBrowseOnly;
	}
	
	/**
	 * Create a GUI.
	 * @param name A String that is the GUI name.
	 */
	
	public StartFrame(String name){
		super(name);
		BasicConfigurator.configure();
		Logger.getLogger("com.sshtools").setLevel(Level.OFF);
		Logger.getLogger("gCLINE.uk.ac.cam.sb913.gCLINE").setLevel(Level.OFF);
		
		messenger = new ErrorManager(this);
		statusBar = new JLabel("Ready!");
		setBrowseOnly(false);
		setVersion();
		layoutPanels();
	}
	/**
	 * Set the log file viewer.
	 * @param givenFile A File that we want to
	 * look at through the log file viewer.
	 */
	public void setFileViewer(File givenFile){
		//only switch if we have a non-null file.
		if(givenFile != null)
			fileViewer.viewFile(givenFile.getAbsolutePath());
		else
			logger.info("(setFileViewer(File)) the given file is null.");
	}
	/**
	 * Set the file transet status message.
	 * @param message The String that we wish to set the
	 * transit status message to.
	 */
	synchronized private void setStatus(String message){
		statusBar.setText(message);
	}
	/**
	 * Tell the file transfer status that there is a change
	 * in the files being transfered.
	 * @param files A String array that is the files we
	 * wish to add or remove from our transit ArrayList.
	 * @param addFile A boolean flag that is true if
	 * we wish to add a file and false if we want to
	 * remove it.
	 */
	synchronized private void changeTransitFile(String [] files, 
			boolean addFile){
		//change the ArrayList
		if(addFile == true){
			for(String s: files){
				filesInTransit.add(s);
			}
		} else {
			for(String s: files){
				filesInTransit.remove(s);
			}
		}
		
		//notify the status message
		if(filesInTransit.isEmpty())
			setStatus("Ready!");
		else{
			String message = "Down/Uploading: ";
			for(String file: filesInTransit){
				message = message + " [" + file + "]";
			}
			logger.info("[changeTrasitFile(String[], boolean)] " + message);
			setStatus(message);
			}
		
	}
	/**
	 * Check to see if the file is in transit.
	 * @param f The file name in transit.
	 * @return true if it is in transit, false otherwise.
	 */
	public boolean inTransit(String f){
		return filesInTransit.contains(f);
	}
	
	/**
	 * Download a set of files from a remote directory.
	 * Note that this extends TransferFiles that is a 
	 * Runnable class.
	 * @author Kathe Todd-Brown
	 *
	 */
	public class Download extends TransferFiles{
		/**
		 * Create A Download instance.
		 * @param d The Record that holds the connection etc.
		 * @param ignoreFailure flag weather we should ignore a failure to
		 * download the files or notify the user.
		 * @param givenLocalFolder A File that is local folder we are going to download
		 * these files to.
		 * @param givenRemoteFiles An array of Strings that lists the remote files
		 * we are downloading.
		 */
		public Download(Record d, boolean ignoreFailure, File givenLocalFolder, String[] givenRemoteFiles) {
			super(d, ignoreFailure, givenLocalFolder, givenRemoteFiles);
		}
		/**
		 * Notify the GUI that we are transferring files and then
		 * run transfer the files.
		 */
		public void run(){
			changeTransitFile(super.remoteFiles, true);
			super.run();
			changeTransitFile(super.remoteFiles, false);
		}
	}
	/**
	 * Upload a set of files to a remote directory.
	 * Note that this extends TransferFiles that is a 
	 * Runnable class.
	 * @author Kathe Todd-Brown
	 *
	 */
	public class Upload extends TransferFiles{
		/**
		 * Create A Upload instance.
		 * @param d The Record that holds the connection etc.
		 * @param ignoreFailure flag weather we should ignore a failure to
		 * upload the files or notify the user.
		 * @param givenRemoteFolder A String that is remote folder we are going to upload
		 * these files to.
		 * @param givenLocalFiles An array of Files that lists the local files
		 * we are uploading.
		 */
		public Upload(Record d, boolean ignoreFailure, String givenRemoteFolder, File[] givenLocalFiles) {
			super(d, ignoreFailure, givenRemoteFolder, givenLocalFiles);
			logger.info("[Upload(Record, boolean, String, File[])] ending");
			
		}

		public void run(){
			logger.info("[run()] starting");
			if(getBrowseOnly() == false){
				//add the file to the gui notifiier
				changeTransitFile(super.localFiles, true);
				super.run();
				//remote the files from teh gui notifier
				changeTransitFile(super.localFiles, false);
			}else 
				new ErrorManager(StartFrame.this).createError("Can not upload files [" 
					+ localFiles[0]+ "] when set in browse mode.", 
					"run():Upload@StartFrame.java");
			logger.info("[run()] ending");
		}
		
	}
}
