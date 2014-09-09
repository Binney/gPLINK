package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.session.SessionChannelClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;

public class RunCommand implements Runnable{

	/**
	 * Define a static logger variable
	 */
	static private Logger logger = Logger.getLogger(RunCommand.class);
	
	private SshClient conn; 
	
	public ArrayList <String> outputLines;
	public ArrayList <String> errorLines;
	
	public String command;
	
	private Record data;
	
	/**
	 * Split a given strip up into an array based
	 * on the spaces outside of "".
	 * @param given a string that we wish to split
	 * @return an array of strings that contains the 
	 * same contains the split input string.
	 */
	static public String [] stripAndSplit(String given){
		ArrayList <String> temp = new ArrayList<String>();
		temp.add("");
		given = given.trim();
		int currentTemp = 0;
		boolean insideQuotes = false;
		//go through the entire string
		for(int i = 0; i < given.length(); i++){
			//flag when we pass "
			if(given.charAt(i) == ('\"'))
				insideQuotes = ! insideQuotes;
			else{
				//if we aren't inside quotes
				if(insideQuotes == false 
						&& given.charAt(i) == ' '){
						//check to make sure it's not empty before
						//...we move on
						if(!temp.get(currentTemp).equals("")){
							currentTemp ++;
							temp.add(currentTemp, "");
						}
					}
				else
					//add the character to the current
					temp.set(currentTemp, 
						temp.get(currentTemp) + given.charAt(i));
			}
		}
		//create an array of the correct size
		String [] ans = new String [temp.size()];
		//populate it with the processed given string
		for(int i = 0; i < temp.size(); i++)
			ans[i] = temp.get(i).trim();
		//return that array
		return ans;
	}
	
	public RunCommand(String cmd, Record d){
		data = d;
		command = cmd;
		conn = data.getConn();
	}
	
	public RunCommand(String cmd, Record d, boolean forceLocal){
		this(cmd, d);
		if(forceLocal)
			conn = null;
			
	}
	
	public RunCommand(String cmd, SshClient c){
		data = null;
		command = cmd;
		conn = c;
	}
	
	public void run() {
		
		if(conn == null){
			logger.info("Executing command locally: [" + command + "]");
			String [] temp = stripAndSplit(command);
			
			try {
				@SuppressWarnings("unused") 
				Process testing = Runtime.getRuntime().exec(temp, null, data.getLocalFolder());
				
			} catch (IOException e1) {
				logger.error("[run()] IOException trying to run command locally.");
				String cmd = "";
				for(String t : temp){
					cmd += "[[" + t + "]]";
				}
				
				if(data != null)
					data.frame.messenger.createError(
							"Can not execute this command locally. \n" +
			    			"[ " + command + " ]-> " + cmd, "run()@RunCommand.java");
				else
					new ErrorManager(null).createError(
			    			"Can not execute this command locally. \n" +
			    			"[ " + command + " ]-> " + cmd, "run()@RunCommand.java");
			}
			return;
		}
		
		logger.info("Executing command remotely ["+command+"].");
		
		SessionChannelClient sess = null;
		try {
			sess = conn.openSessionChannel();
		} catch (IOException e) {
			logger.error("[run()] IOException Can not open session channel.");
			
			e.printStackTrace();
			if(data != null)
				data.frame.messenger.createError("Error accessing connection." +
					"Reconnecting... \n ",
					"run()@RunCommand.java");
			else
				new ErrorManager(null).createError("Error accessing connection." +
					"Reconnecting... \n ",
					"run()@RunCommand.java");
			if(data != null)
				data.connect();
			return;
		}
		
		try {
			sess.executeCommand(command);
		} catch (IOException e) {
			logger.error("[run()] IOException Cannot execute ["+ command +"].");
			e.printStackTrace();
			if(data != null)
				data.frame.messenger.createError("Error executing ["+ command +"]. \n ",
					"execute():SendCommand.java");
			else
				new ErrorManager(null).createError("Error executing ["+ command +"]. \n ",
					"execute():SendCommand.java");
			return;
		}
		
		try{
		/**
		  * Reading from the session InputStream
		  */
		  InputStream in = sess.getInputStream();
		  InputStreamReader isr = new InputStreamReader(in);
		  BufferedReader br = new BufferedReader(isr);
		  
		  String line = br.readLine();
		  outputLines = new ArrayList<String>();
		  while(line != null) {
			  outputLines.add(line);
			  line = br.readLine();
		  }
		  
		  /**
		  * Reading from the session ErrStream
		  */
		  InputStream err = sess.getStderrInputStream();
		  isr = new InputStreamReader(err);
		  br = new BufferedReader(isr);

		  line = br.readLine();
		  errorLines = new ArrayList<String>();
		  while(line != null) {
			  errorLines.add(line);
			  line = br.readLine();
		  }
	
		//close up the session
		sess.close();
		} catch(IOException e){
			logger.error("[run()] IOException Error reading the command results " +
					"["+command+"].");
			e.printStackTrace();
			if(data != null)
				data.frame.messenger.createError("Error reading the command results " +
					"["+command+"].","execute():SendCommand.java");
			else
				new ErrorManager(null).createError("Error reading the command results " +
					"["+command+"].","execute():SendCommand.java");
			
		}
	}
}
