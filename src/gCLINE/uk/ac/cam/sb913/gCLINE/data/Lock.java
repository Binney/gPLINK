package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;

/**
 * Deal with the locking and unlocking of a folder.
 * @author Kathe
 *
 */
public class Lock {
	
	static public String UNLOCKED_KEY = "";
	/**
	 * A logger for this clas
	 */
	private Logger logger = Logger.getLogger(Lock.class);
	
	/**
	 * This allows us to pass errors
	 */
	private ErrorManager error;
	/**
	 * The key of this particular instance of the lock.
	 */
	private String key;
	
	/**
	 * The lock file.
	 */
	private File lockFile;
	/**
	 * The directory that the remote lock file is in
	 */
	private String remoteFile;
	
	private Record data;
	private StartFrame frame;
	
	/**
	 * Create a lock from a string representing the 
	 * local file and set the new key.
	 * @param givenFrame The main frame this lock is 
	 * attached to.
	 * @param d the Record that this lock is associated with.
	 */
	public Lock(StartFrame givenFrame,
			Record d){
		logger.info("[Lock(StartFrame, Record)] Entering.");
		frame = givenFrame;
		data = d;
		if(data.isRemote()){
			remoteFile = data.getRemoteFolder() + Record.LOCK_FILE;
			logger.info("[Lock(StartFrame, Record)] data is remote");
		}else
			remoteFile = null;
		lockFile = new File(data.getLocalFolder(), Record.LOCK_FILE);
		
		error = frame.messenger;
		//create a new file if it doesn't already exist
		try {
			lockFile.createNewFile();
		} catch (IOException e1) {
			error.createError("Error creating lock file ["
					+ lockFile.getAbsolutePath() + 
					"] \n ------------------ \n"
					+ e1.getMessage(), 
					"Lock@Lock.java");
			logger.error("(Lock())Error creating lock file ["
					+ lockFile.getAbsolutePath() + "]" );
			
		}
		//Generate a random key
		key = ((Integer)( (int)(Math.random()*100000000))).toString();
		logger.info("[Lock(StartFrame, Record)] key set: "+ key);
		logger.info("[Lock(StartFrame, Record)] reading lock now");
		readLock();
		logger.info("[Lock(StartFrame, Record)] ...exiting");
		
	}
	
	public boolean availableLock(){
		String lock = readLock();
		logger.info("[availableLock()] checking avaliablity, lock: "
				+lock+"== unlock: "+UNLOCKED_KEY);
		return (lock == null  
					|| lock.equals(UNLOCKED_KEY));
	}
	
	public boolean hasLock(){
		String lock = readLock();
		logger.info("[hasLock()] checking owner, lock: "
				+lock+"== key: "+getKey());
		return (lock != null 
					&& lock.equals(getKey()));
	}
	
	public boolean stealLock(){
		logger.info("[stealLock()] entering stealing lock");
		int responce = JOptionPane.showConfirmDialog(frame, "Another " +
				"instance of gPLINK has locked this project.\n" + 
				"Either gPLINK is open elsewhere, or the last instance did not close cleanly.\n" +
				"Unless you think somebody else is currently working on this project, select Yes\n" +  
                "Otherwise it will open in Browse-only mode\n"+
 				"Do you wish to steal it?");
		if(responce == JOptionPane.YES_OPTION){
			if(lockFile() == false){
				logger.warn("[stealLock()] unable to steal lock file (1)");
				return false;
			} else{
				logger.info("[stealLock()] stolen lock file sucessfull");
				return true;
			}
		}else{
			logger.warn("[stealLock()] unable to steal lock file (2)");
			
			return false;
		}
	}
	
	/**
	 * Get the key for this instance of the lock
	 * @return a string that is the lock's key
	 */
	private String getKey(){
		logger.info("[getKey()] Getting key: ["+ key + "]");
		return key;
	}

	
	/**
	 * Read the lock code from the file.
	 * @return the lock code for this file
	 */
	private String readLock(){
		if(data.isRemote()){
			frame.new Download(data, true, data.getLocalFolder(), new String[]{ remoteFile}).run();
		}

		String lock = "";
		if(!lockFile.isFile()){
			logger.info("[readLock()] not a file! returning lock: " + lock);
			return lock;
		}
		/*
		 * read in the lock setting
		 */
		try{
			/*
			 * read the lock
			 */
			BufferedReader in = new BufferedReader(
					new FileReader(lockFile));
			//the lock code is on the first line
			lock = in.readLine();
			in.close();
		}catch(IOException e){
			error.createError("Error trying to read in local lock.",
					"readLock@Lock.java");
		}
		logger.info("[readLock()] returning lock: " + lock);
		return lock;
	}
	
	/**
	 * Write the key to the lock file.
	 * @return a boolean that is true if the file was
	 * writen to sucessfully, false otherwise
	 */
	public boolean lockFile(){
		
		logger.info("[lockFile()] locking " + lockFile.getAbsolutePath() + " with " + key);
		/*
		 * try to lock the local project file
		 */
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(lockFile));
			out.write(key);
			out.newLine();
			out.close();
		} catch (IOException e) {
			logger.error("[lockFile()] Error writing the lock file locally.");
			return false;
			//new ErrorDialog(frame, "Error writing the lock file locally. \n" +
			//"lockMeta@Project.java");
			//eprintStackTrace();
		}
		
		if(data.isRemote()){
			frame.new Upload(data, false, data.getRemoteFolder(), new File[] {lockFile}).run();
		}
		
		logger.info("[lockFile()] Exiting locking, "+ lockFile.getAbsolutePath() +" is:" + readLock());
		return true;
	}
	
	/**
	 * Write a blank file to the lock file.
	 * @return a boolean that is true if the file was
	 * writen to sucessfully, false otherwise
	 */
	public boolean unlockFile(){
		logger.info("[unlockFile()] unlocking " + lockFile.getAbsolutePath() + " with \"" + UNLOCKED_KEY + "\"");
		/*
		 * try to lock the local project file
		 */
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(lockFile));
			out.write(UNLOCKED_KEY);
			out.newLine();
			out.close();
		} catch (IOException e) {
			logger.error("[unLock()] Error writing the unlock file locally.");
			return false;
			//new ErrorDialog(frame, "Error writing the lock file locally. \n" +
			//"lockMeta@Project.java");
			//eprintStackTrace();
		}
		if(data.isRemote()){
			frame.new Upload(data, false, data.getRemoteFolder(), new File[] {lockFile}).run();
		}
		return true;
	}
	
}
