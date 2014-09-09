package gPLINK2.uk.ac.cam.sb913.gPLINK2.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.AutoUpdater;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;

@SuppressWarnings("serial")
public class Project extends Record {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(Project.class);
	/**
	 * 
	 */
	static String PLINK_PATH = "plink_path";
	static String PLINK_PREFIX = "plink_prefix";
	static String HAPLOVIEW_PATH = "haploview";
	static String HAPLOVIEW_APPEND = "haplo_append";
	public static String GPLINK_EXT = ".gplink";
	
	
	public void setHaploAppend(String append){
		globalConfig.setProperty(HAPLOVIEW_APPEND, append);
	}
	
	public void setHaploPath(String path){
		globalConfig.setProperty(HAPLOVIEW_PATH, path);
	}
	
	public void setPlinkPath(String path){
		if(isRemote())
			remoteConfig.setProperty(PLINK_PATH, path);
		else
			globalConfig.setProperty(PLINK_PATH, path);
	}
	
	public void setPlinkPrefix(String path){
		if(isRemote())
			remoteConfig.setProperty(PLINK_PREFIX, path);
		else
			globalConfig.setProperty(PLINK_PREFIX, path);
	}
	
	public String getHaploAppend(){
		return globalConfig.getProperty(HAPLOVIEW_APPEND, "");
	}
	
	public String getHaploPath(){
		return globalConfig.getProperty(HAPLOVIEW_PATH, "");
	}
	
	public String getPlinkPath(){
		if(isRemote())
			return remoteConfig.getProperty(PLINK_PATH, "plink");
		else
			return globalConfig.getProperty(PLINK_PATH, "plink");
	}
	
	public String getPlinkPrefix(){
		if(isRemote())
			return remoteConfig.getProperty(PLINK_PREFIX, "");
		else
			return globalConfig.getProperty(PLINK_PREFIX, "");
	}
	
	
	public Project() {
		super();
		logger.info("[Project()] create a Project");
	}

	public Project(GPLINK f) {
		super(f);
		logger.info("[Project(GPLINK)] create a Project");
	}

	public Project(GPLINK f, String localName) {
		super(f, localName);
		logger.info("[Project(GPLINK, String)] Entering...");
		setAutoUpdater(new AutoUpdater(f, this, 
				GPLINK_EXT, frame.messenger, getLocalUpdateSec()));
		logger.info("[Project(GPLINK, String)] ...exiting");
	}

	public Project(GPLINK f, String localName, String remoteFileName,
			String hostname, String username, String portnum, SshClient c) {
		super(f, localName, remoteFileName, hostname, username, portnum, c);
		logger.info("[Project(GPLINK, String,... SshClient)] Entering...");
		setAutoUpdater(new AutoUpdater(f, this, 
				GPLINK_EXT, frame.messenger, getRemoteUpdateSec()));
		logger.info("[Project(GPLINK, String,... SshClient)] ...exiting");
		
	}

	
	@Override
	public void setAutoUpdater(AutoUpdater given) {
		if(update != null){
			update.cancel();
			update = null;
		}
		
		update = given;
	}

	@Override
	protected void saveRemoteConfig() {
		logger.info("[saveRemoteConfig()] Entering...");
		Properties oldConfig = globalConfig;
		
		if(!isRemote())
			return;
		
		//download the global config file on the remote host
		logger.info("[saveRemoteConfig()] saving remote");
		Thread downloader = new Thread(frame.new 
				Download(this, true, getLocalFolder(), new String[] {REMOTE_GLOBAL_CONFIG}));
		downloader.start();
		try {
			downloader.join();
		} catch (InterruptedException e) {
			logger.error("[saveRemoteConfig()] InterruptedException occured downloading the global " +
					"configuration file on the remote machine.");
			frame.messenger.createError("InterruptedException occured downloading the global " +
					"configuration file on the remote machine", "saveRemoteConfig()@Project.java");
		}
		
		//load the remote global file
		Properties remoteConfig = new Properties();
		File config = new File(getLocalFolder(), REMOTE_GLOBAL_CONFIG);
		try {
			remoteConfig.load(new FileInputStream(config));
		} catch (FileNotFoundException e) {
			logger.warn("[saveRemoteConfig()] FileNotFoundException occured trying to " +
					"load downloaded configuration file. {" + config.getAbsolutePath() + "}");
		} catch (IOException e) {
			logger.warn("[saveRemoteConfig()] IOException occured trying to " +
					"load downloaded configuration file.");
		}
		
		//load the local global file
		Properties localConfig = new Properties();
		File localConfigFile = new File(GLOBAL_CONFIG);
		try {
			localConfig.load(new FileInputStream(localConfigFile));
		} catch (FileNotFoundException e) {
			logger.error("[saveRemoteConfig()] FileNotFoundException occured trying to " +
					"load local configuration file. {" + localConfigFile.getAbsolutePath() + "}");
			frame.messenger.createError("FileNotFoundException occured trying to " +
					"load local configuration file. {" + localConfigFile.getAbsolutePath() + "}",
					"saveRemoteConfig()@Project.java");
		} catch (IOException e) {
			logger.error("[saveRemoteConfig()] IOException occured trying to " +
					"load local configuration file.");
			frame.messenger.createError("IOException occured trying to " +
					"load local configuration file.", "saveRemoteConfig()@Project.java");
		}
		
		//replace the remote config plink information
		logger.info("[saveRemoteConfig()] setting the remote plink: {" 
				+ getPlinkPrefix() + "} {" + getPlinkPath() + "}");
		remoteConfig.setProperty(PLINK_PATH, getPlinkPath());
		remoteConfig.setProperty(PLINK_PREFIX, getPlinkPrefix());
		
		//replace the local config plink information
		globalConfig.setProperty(PLINK_PATH, localConfig.getProperty(PLINK_PATH, "plink"));
		globalConfig.setProperty(PLINK_PREFIX, localConfig.getProperty(PLINK_PREFIX, ""));
		
		//save everything
		try {
			remoteConfig.store(new FileOutputStream(config), "");
		} catch (FileNotFoundException e) {
			logger.error("[saveRemoteConfig()] FileNotFoundException occured trying to " +
				"store remote configuration file. {" + config.getAbsolutePath() + "}");
			frame.messenger.createError("FileNotFoundException occured trying to " +
				"store remote configuration file. {" + config.getAbsolutePath() + "}",
				"saveRemoteConfig()@Project.java");
		} catch (IOException e) {
			logger.error("[saveRemoteConfig()] IOException occured trying to " +
				"store remote configuration file.");
			frame.messenger.createError("IOException occured trying to " +
				"store remote configuration file.", "saveRemoteConfig()@Project.java");
		}
		
		//upload the remote config to the folder
		Thread uploader = new Thread(frame.new Upload(this, 
				false, "", new File[]{config}));
		uploader.start();
		try {
			uploader.join();
		} catch (InterruptedException e1) {
			logger.error("[saveRemoteConfig()] InterruptedException occured uploading the global " +
					"configuration file on the remote machine.");
			frame.messenger.createError("InterruptedException occured uploading the global " +
					"configuration file on the remote machine", "saveRemoteConfig()@Project.java");

		}
		try {
			globalConfig.store(new FileOutputStream(localConfigFile), "");
		} catch (FileNotFoundException e) {
			logger.error("[saveRemoteConfig()] FileNotFoundException occured trying to " +
				"store local configuration file. {" + localConfigFile.getAbsolutePath() + "}");
			frame.messenger.createError("FileNotFoundException occured trying to " +
				"store local configuration file. {" + localConfigFile.getAbsolutePath() + "}", 
				"saveRemoteConfig()@Project.java");
		} catch (IOException e) {
			logger.error("[saveRemoteConfig()] IOException occured trying to " +
				"store local configuration file.");
			frame.messenger.createError("IOException occured trying to " +
				"store local configuration file.", "saveRemoteConfig()@Project.java");
		}
		
		globalConfig = oldConfig;
		logger.info("[saveRemoteConfig()] ...Exiting");
	}
	
	protected void loadRemoteConfig(){
		logger.info("[loadRemoteConfig()] Entering...");
		if(isRemote()){
			Thread downloader = new Thread(frame.new Download(this, 
					true, getLocalFolder(), new String[] {REMOTE_GLOBAL_CONFIG}));
			downloader.start();
			try {
				downloader.join();
			} catch (InterruptedException e) {
				logger.error("[loadRemoteConfig()] InteruptionException occured trying" +
						" to download remote global configuration file.");
				frame.messenger.createError("InteruptionException occured trying" +
						" to download remote global configuration file.", 
						"loadRemoteConfig()@Project.java");
			}
			Properties tempConfig = new Properties();
			File config = new File(getLocalFolder(), REMOTE_GLOBAL_CONFIG);
			try {
				tempConfig.load(new FileInputStream(config));
			} catch (FileNotFoundException e) {
				logger.warn("[loadRemoteConfig()] FileNotFoundException occured trying" +
						" to load remote global configuration file.");
			} catch (IOException e) {
				logger.warn("[loadRemoteConfig()] IOException occured trying" +
						" to load remote global configuration file.");
			}
			String path = tempConfig.getProperty(PLINK_PATH, "plink");
			String prefix = tempConfig.getProperty(PLINK_PREFIX, "");
			logger.info("[loadRemoteConfig()] replacing plink: {" + prefix + "} {"+path + "}");
			setPlinkPath(path);
			setPlinkPrefix(prefix);
			config.delete();
		}
		
		logger.info("[loadRemoteConfig] ...exiting");
	}
	

}
