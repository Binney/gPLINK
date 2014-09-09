package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.ScpClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;


class TransferFiles implements Runnable {
	/**
	 * Define a static logger variable so that it references the
	 */
	private static Logger logger = Logger.getLogger(TransferFiles.class);
	/**
	 * 
	 */
	private Record data;
	private String localFolder;
	protected String [] remoteFiles;
	
	private String remoteFolder;
	protected String [] localFiles;
	
	private boolean ignore;
	
	public void run() {
//		tell the logger what's going on
		logger.info("[run()] Starting transfer");
		//declare the scp client
		ScpClient scp = null;
		if(data == null || data.getConn() == null){
			logger.error("[run()] the connection is null, no file transferd");
			return;
		}
			
		try {
			//try to establish a scp client
			scp = data.getConn().openScpClient();
			//oh no's! We ran into an error trying to open the 
			//...connection! 
		} catch (IOException e) {
			//log the error
			logger.error( "Error trying open connection to download file.");
			//let this notify the user that the connection was broke
			data.connect();
			//go out of the download
			return;
		}
		//check to see if we should download
		if(localFolder != null){
			String allremote = "";
			for(String temp: remoteFiles){
				allremote = "[" + temp + "] ";
			}
			//logs the start of the download
			logger.info("Download ["+ allremote +"] to ["+localFolder+"].");
			//copy the files
			try {
				scp.get(localFolder, remoteFiles, false);
			//oh no's again! Something didn't work
			} catch (IOException e1) {
				//if we ignore this failure then we probably aren't
				//...sure that the remote file exists
				if(ignore == false){
					//tell the user that we didn't download
					data.frame.messenger.createError(
							"Can not download files. " +
							"Possible  connection failure.", "run()@TransferFiles");
					logger.error("[run()] Error dialog pop's up due to download failure");
				}
				//log that the download failed
				logger.error("Error trying download "+allremote+" to ["+localFolder+"].");
			}
		
			//notify the log that we are done downloading
			logger.info("End download");
		} else if(remoteFolder != null){
			String alllocal = "";
			for(String temp: localFiles){
				alllocal = "[" + temp + "] ";
			}
			//logs the start of the download
			logger.info("Upload ["+alllocal+"] to ["+remoteFolder+"].");
			//copy the files
			try {
				scp.put(localFiles, remoteFolder, false);
			//oh no's again! Something didn't work
			} catch (IOException e1) {
				//if we ignore this failure then we probably aren't
				//...sure that the remote file exists
				if(ignore == false){
					//tell the user that we didn't download
					data.frame.messenger.createError(
							"Can not upload files. " +
							"Possible  connection failure.", "run()@TransferFiles");
				}
				//log that the upload failed
				logger.error("Error trying upload "+alllocal+" to ["+remoteFolder+"].");
			}
		
			//notify the log that we are done downloading
			logger.info("End upload");
		}
	}
	
	public TransferFiles(Record d, boolean ignoreFailure,
			File givenLocalFolder, String[] givenRemoteFiles){
		data = d;
		localFolder = givenLocalFolder.getAbsolutePath();
		remoteFiles = givenRemoteFiles;
		ignore = ignoreFailure;
	}
	
	public TransferFiles(Record d, boolean ignoreFailure,
			String givenRemoteFolder, File [] givenLocalFiles){
		data = d;
		remoteFolder = givenRemoteFolder;
		localFiles = new String[givenLocalFiles.length];
		for(int i = 0; i < givenLocalFiles.length; i ++){
			localFiles[i] = givenLocalFiles[i].getAbsolutePath();
		}
		ignore = ignoreFailure;
	}

}
