package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.sshtools.common.authentication.KBIRequestHandlerDialog;
import com.sshtools.common.authentication.PasswordAuthenticationDialog;
import com.sshtools.common.hosts.AbstractHostKeyVerification;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolException;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.KBIAuthenticationClient;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.InvalidHostFileException;
import com.sshtools.j2ssh.transport.TransportProtocolException;

import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.CalculationInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.QueueInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;

/**
 * This class manages the operation annotations.
 * As an extension of DefaultTreeModel that allows Record
 * to be passed to a JTree constructor for the user to interact 
 * with in the Operations pane. Notes: All files are identified
 * by their full file names. All operation names are unique.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public abstract class Record extends DefaultTreeModel implements KeyWords{
	/**
	 * This is the default text editor. This is set
	 * based on what kind of operating system you have.
	 * Window's operating system uses "write",
	 * Mac's uses "open -a /Applications/TextEdit.app",
	 * any other kind of operating system is assumed to be
	 * Linux/Unix and uses "emacs". These key words are
	 * passed as a command line to the operating system with
	 * a file name as the argument.
	 */
	static public String DEFAULT_EDITOR;
	static{
		String temp = System.getProperty("os.name");
		if(temp.matches(".*[wW]indow.*")){
			//you use window's you poor poor baby
			DEFAULT_EDITOR = "write";
		} else if(temp.matches(".*[mM]ac.*")){
			DEFAULT_EDITOR = "open -a /Applications/TextEdit.app";
		} 	
		else
			DEFAULT_EDITOR = "emacs";
	}
	
	/**
	 * OP_LOG_EXT is a String that identifies the file extension
	 * for the operation log file.
	 */
	private String OP_LOG_EXT = ".log";
	
	/**
	 * frame is a StartFrame (an extension of JFrame)
	 * that holds the main GUI. This is what the user
	 * interacts with.
	 */
	public StartFrame frame;
	
	/**
	 * remoteConfig is a Properties that 
	 * holds the information for the remote connection.
	 * populated from info including: user name,
	 * host name, port number, remote project folder name,
	 * remote update period
	 */
	protected Properties remoteConfig;

	/**
	 * globalConfig is a Properties that 
	 * holds the global configuration data.
	 * Includes: 
	 * Alternative editor, previous projects
	 */
	protected Properties globalConfig;
	
	/**
	 * localConfig is a Properties that 
	 * holds the local project configuration data.
	 * Includes: local project folder name, 
	 * home folder name, local update period
	 */
	protected Properties localConfig;
	
	/**
	 * isNew is a boolean that flags if this
	 * Record is new or recreated from stored
	 * data.
	 */
	public boolean isNew;
	
	/**
	 * globalFileNotes is
	 * a HashMap<String, String> that carries 
	 * the global file notes. These notes appear in the
	 * left file panel and before an local file notes in
	 * the operation tree.
	 */
	public HashMap<String, String> globalFileNotes;
	
	/**
	 * myLock is a Lock that keeps track of the permissions
	 * to work on this project.
	 */
	public Lock myLock;
	
	/**
	 * conn is a SshClient that we use to connect with a
	 * remote server.
	 */
	private SshClient conn;
	
	/**
	 * update is an AutoUpdater that 
	 * automatically checks the home directory for
	 * new files and updates the displays.
	 */
	public AutoUpdater update;
	
	/**
	 * logger is a Logger that optionally log 
	 * messages for debugging/troubleshooting.
	 */
	private static Logger logger = Logger.getLogger(Record.class);
	
	/**
	 * Setter function for the local update period.
	 * @param sec an int that is the number of seconds between
	 * runs of the auto-updater in local projects.
	 */
	public void setLocalUpdateSec(int sec){
		globalConfig.setProperty(LOCAL_UPDATE_INTERVAL_KEY, new Integer(sec).toString());	
	}
	
	/**
	 * Setter function for the remote update period.
	 * @param sec an int that is the number of seconds between
	 * runs of the auto-updater in remote projects.
	 */
	public void setRemoteUpdateSec(int sec){
		globalConfig.setProperty(REMOTE_UPDATE_INTERVAL_KEY, new Integer(sec).toString());	
	}
	
	/**
	 * Setter function for the autoUpdater. Each implementation of
	 * Record must implement a setter function for the auto-updater
	 * because the auto-updater is unique for each implementation.
	 */
	public abstract void setAutoUpdater(AutoUpdater given);
	
	/**
	 * Setter function for the log extension
	 * @param ext a string describing the log extension
	 */
	public void setLog(String ext){
		OP_LOG_EXT = ext;
	}
	
	/**
	 * Setter function for the connection data
	 * @param c an SshClient that is the actual connection
	 * @param host a String holding the host name
	 * @param port a String holding the port number
	 * @param user a String holding the user name
	 */
	public void setConnData(SshClient c, 
			String host, String port, String user){
		conn = c;
		remoteConfig.setProperty(HOST_KEY, host);
		remoteConfig.setProperty(PORT_KEY, port);
		remoteConfig.setProperty(USER_KEY, user);
		
	}
	
	/**
	 * Setter function for the alternative editor.
	 * @param editor a String that has the command for 
	 * the alternate editor.
	 */
	public void setAltEditor(String editor){
		globalConfig.setProperty(USER_EDITOR_KEY, editor);
	}
	
	/**
	 * This function adds the current project to the 
	 * head of the list of previous projects. This
	 * allows us to launch the recent projects quickly from the
	 * associated menu bar.
	 *
	 */
	public void setLastProject(){
		//get the current name of the local directory
		String localFolderName = getLocalFolder().getAbsolutePath();
		//if the project is already at the top don't do
		//...anything
		if(getP1() != null 
				&& getP1().equals(localFolderName)){
			return;
		//else if it's in the second slot then move it
		//...to the top and more the first down
		} else if(getP2() != null 
				&& getP2().equals(localFolderName)){
			setP2(getP1());
			setP1(localFolderName);
		//if it's in the third slot then move it 
		//...to the top and more the first two down
		} else if(getP3() != null
				&& getP3().equals(localFolderName)){
			setP3(getP2());
			setP2(getP1());
			setP1(localFolderName);
		//otherwise sift everything down and
		//...put it at the top
		}else{
			setP4(getP3());
			setP3(getP2());
			setP2(getP1());
			setP1(localFolderName);
		}
	}
	
	/**
	 * Setter for the first project name.
	 * @param name a String that is the name of the project
	 */
	public void setP1(String name){
		if(name != null)
			globalConfig.setProperty(P1_KEY, name);
	}
	/**
	 * Setter for the second project name.
	 * @param name a String that is the name of the project
	 */
	public void setP2(String name){
		if(name != null)
			globalConfig.setProperty(P2_KEY, name);
	}
	/**
	 * Setter for the third project name.
	 * @param name a String that is the name of the project
	 */
	public void setP3(String name){
		if(name != null)
			globalConfig.setProperty(P3_KEY, name);
	}
	/**
	 * Setter for the forth project name.
	 * @param name a String that is the name of the project
	 */
	public void setP4(String name){
		if(name != null)
			globalConfig.setProperty(P4_KEY, name);
	}
	/**
	 * Get the local update period for the auto-updater.
	 * @return an int that is the number of seconds between
	 * each update run.
	 */
	public int getLocalUpdateSec(){
		return new Integer(globalConfig.getProperty(LOCAL_UPDATE_INTERVAL_KEY, "5"));
	}
	/**
	 * Get the remote update period for the auto-updater.
	 * @return an int that is the number of seconds between
	 * each update run.
	 */
	public int getRemoteUpdateSec(){
		return new Integer(globalConfig.getProperty(REMOTE_UPDATE_INTERVAL_KEY, "30"));
	}
	
	/**
	 * Get the first project name
	 * @return The first project name, null if it's not
	 * set.
	 */
	public String getP1(){
		return globalConfig.getProperty(P1_KEY, null);
	}
	/**
	 * Get the second project name
	 * @return The second project name, null if it's
	 * not set
	 */
	public String getP2(){
		return globalConfig.getProperty(P2_KEY, null);
	}
	/**
	 * Get the third project name
	 * @return The third project name, null if it's
	 * not set
	 */
	public String getP3(){
		return globalConfig.getProperty(P3_KEY, null);
	}
	/**
	 * Get the forth project name
	 * @return The forth project name, null if it's
	 * not set.
	 */
	public String getP4(){
		return globalConfig.getProperty(P4_KEY, null);
	}
	/**
	 * Get the command for the alternative editor
	 * @return The user defined alternative editor.
	 */
	public String getAltEditor(){
		return globalConfig.getProperty(USER_EDITOR_KEY, null);
	}
	
	/**
	 * get the connection to the remote directory
	 * @return the connection to the remote directory
	 */
	public SshClient getConn(){
		return conn;
	}
	
	/**
	 * Get the name of the remote file
	 * @return a string that describes the remote file
	 */
	public String getRemoteFolder(){
		if(remoteConfig == null)
			return null;
		//get the remote folder and default to null
		return remoteConfig.getProperty(REMOTE_FOLDER_KEY, null);
	}
	
	/**
	 * Get the name of the local file
	 * @return a string that describes the local file
	 */
	public File getLocalFolder(){
		if(localConfig == null)
			return null;
		String localFolder = localConfig.getProperty(LOCAL_FOLDER_KEY, null);
		if(localFolder == null)
			return null;
		else
			return new File(localFolder);
	}
	
	/**
	 * Get the home folder that the data is stored in.
	 * If the project is remote then this is the folder
	 * name on the remote server, otherwise it's
	 * the full name of the local folder.
	 * @return A string that holds the full name of the folder
	 * that holds the data.
	 */
	public String getHomeFolder(){
		if(isRemote())
			return getRemoteFolder();
		else if(getLocalFolder() != null)
			return getLocalFolder().getAbsolutePath() + File.separator;
		else
			return "";
	}
	
	/**
	 * Get a list of files that are in your working directory
	 * @return a ArrayList of the files in your working directory
	 */
	public ArrayList <String> getHomeFiles(){
		ArrayList <String> ans = new ArrayList <String>();
		logger.info("[getHomeFiles()] getting files for");
		if(isRemote()){
			RunCommand getFileList = new RunCommand("ls " + getRemoteFolder(), this);
			getFileList.run();
			ans = getFileList.outputLines;
		}else{
			String [] temp = null;
			if(getLocalFolder() != null)
				temp = getLocalFolder().list();
			
			if(temp == null)
				return ans;
			
			for(String f: temp){
				ans.add(f);
			}
		}
		Collections.sort(ans);
		logger.info("[getHomeFiles()] list of files: "+ ans.toString());
		return ans;
	}
	
	/**
	 * Get a specific file note
	 * @param filename A string that is the file name
	 * @return A global description for this file.
	 */
	public String getGlobalNote(String filename){
		System.out.println("Setting globalfilenote " + filename);
		return globalFileNotes.get(filename);
	}
	
	/**
	 * Get a list of all the operations
	 * @return a ArrayList of all the operations information
	 */
	public ArrayList <OperationInfo> getAllOp(){
		ArrayList <OperationInfo> ans = new ArrayList <OperationInfo>();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
		int numOp = root.getChildCount();
		for(int i = 0; i < numOp; i ++){
			ArrayList <OperationInfo> li = ((OperationInfo)root.getChildAt(i)).getOpChildren();
			ans.addAll(li);
		}
		return ans;
		
	}
	
	/*
	 * Get the full information on an operation
	 * @param opName A string that is the name of the operation
	 * @return The full operation information
	 */
	public OperationInfo getOp(String opName){
		ArrayList <OperationInfo> allOperations = getAllOp();
		for(OperationInfo op: allOperations){
			if((op.getName()).equals(opName))
				return op;
		}
		return null;
	}
	
	public String getLogExt(){
		return OP_LOG_EXT;
	}
	
	/**
	 * Load the info document from xml file, otherwise
	 * set as default.
	 * @param metaFile the file that holds the xml document
	 * @param homefile the string that is our project root
	 * @return true if data was loaded from a file, false if 
	 * we had to create a empty Record
	 */
	public boolean loadData(File metaFile, String homefile, boolean remote){
		boolean ans = false;
		
		//let the logger know you are running
		logger.info("[loadData(File, String, boolean)] "+ 
			metaFile.getAbsolutePath() + " is being loaded with a " +
			" home file as: " + homefile + " and remote flag: " + remote);
		//set the local project folder
		localConfig.setProperty(LOCAL_FOLDER_KEY, metaFile.getParent());
		//log this as the most recent project
		setLastProject();
		//start create the tree model
		this.setRoot(new DefaultMutableTreeNode(homefile));
		
		//use xml document builders to create a new document from the xml
		//...log file
		Document info;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			frame.messenger.createError("Error trying to create a new " +
					"document builder.", "loadData@Record.java");
		}
		
		//check that the log file exists
		if(metaFile.exists() ){
			ans = true;
			try {
				info = parser.parse(metaFile);
			} catch (SAXException e) {
				frame.messenger.createError("BANANASError trying to create project, can not parse log file.\n Creating new project, please import old operations.\n ",
					"loadData@Record.java");
				metaFile.delete();
				return loadData(metaFile, homefile, remote);
				
			} catch (IOException e) {
				frame.messenger.createError("Error trying to create project, " +
					"IOException.\n Creating new" +
					"project, please import old operations.\n ",
					"loadData@Report.java");
				metaFile.delete();
				return loadData(metaFile, homefile, remote);
				
			}
			//info.getDocumentElement().setAttribute(VS_KEY, version);
		}else{ //file doesn't exist
			ans = false;
			//create a new document
			info = parser.newDocument();
			
			Element root = 
				info.createElement("project");
			root.setAttribute(HOME_KEY, homefile);
			
			//append the root to the project
			info.appendChild(root);
			info.getDocumentElement().setAttribute(LOCAL_FOLDER_KEY, metaFile.getParent());
			Element globalDesc = info.createElement(FOLDER_KEY);
			root.appendChild(globalDesc);
		}
		
		if(remote){
			info.getDocumentElement().setAttribute(REMOTE_FOLDER_KEY, homefile);
		} else {
			info.getDocumentElement().removeAttribute(REMOTE_FOLDER_KEY);
		}
		
		//populate the nodes in Record
		populateNodes(info);
		
		logger.info("[loadData(File, String, boolean)] ... exiting");
		return ans;
	}
	
	/**
	 * Populate the operations and global file notes
	 * from a Document.
	 * @param info A document that holds all the global
	 * descriptions and operation nodes
	 */
	private void populateNodes(Document info){
		logger.info("[populateNodes(Document)] Entering...");
		localConfig.setProperty(HOME_KEY, info.getDocumentElement().getAttribute(HOME_KEY));
		
		//start at the first child
		Node opIndex = info.getDocumentElement().getFirstChild();
		//go until you are at the last child
		while(opIndex != null){
			if(opIndex.getNodeName().equals(FOLDER_KEY)){
				Node fileIndex = opIndex.getFirstChild();
				while(fileIndex != null){
					if(fileIndex.getNodeName().equals(FILE_KEY)){
						setGlobalFileDesc(
								((Element)fileIndex).getAttribute(NAME_KEY),
								fileIndex.getTextContent());
					}//file if
					fileIndex = fileIndex.getNextSibling();
				}//file while
			}//folder if
			if(opIndex.getNodeName().equals(CALC_KEY)){
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
				processFileChildren(root, opIndex);
			} else if(opIndex.getNodeName().equals(QUEUE_KEY)){
				addQueue(opIndex);
			}//op and queue if

			opIndex = opIndex.getNextSibling();
		}//opIndex while loop
		
		logger.info("[populateNodes(Document)] ...exiting");
	}
	
	/**
	 * Process input and output files for opIndex, then add it to the specified node.
	 */
	private void processFileChildren(DefaultMutableTreeNode node, Node opIndex) {
		String opName = ((Element)opIndex).getAttribute(NAME_KEY);
		String cline = ((Element)opIndex).getAttribute(CLINE_KEY);
		//String opDescription = opIndex.getTextContent();
		String opDescription = "";
		ArrayList<String[]> infiles = new ArrayList<String[]>();
		ArrayList<String[]> outfiles = new ArrayList<String[]>();
		
		Node fileGroupIndex = opIndex.getFirstChild();
		while(fileGroupIndex!=null){
			if(fileGroupIndex.getNodeType() == Node.TEXT_NODE){
				opDescription = fileGroupIndex.getNodeValue();
			} else {
				Node fileIndex = fileGroupIndex.getFirstChild();
				while(fileIndex != null){
					if(fileGroupIndex.getNodeName().equals(INFILE_KEY)){
						infiles.add(new String [] {((Element)fileIndex).getAttribute(NAME_KEY), 
								fileIndex.getTextContent()});
					}//input files if
					if(fileGroupIndex.getNodeName().equals(OUTFILE_KEY)){
						outfiles.add(new String []{((Element)fileIndex).getAttribute(NAME_KEY),
								fileIndex.getTextContent()});
					}//output files if
					fileIndex = fileIndex.getNextSibling();
				}//file while
			}//end if/else
			fileGroupIndex = fileGroupIndex.getNextSibling();
		}//fileGroup while
		//pull the time stamp for this operation
		String time = ((Element)opIndex).getAttribute(TIMESTAMP_KEY);
		//otherwise pull the time stamp for the log file
		if( time == null || time.matches("^\\s*$")){
			File log = CalculationInfo.getLog(getLocalFolder(), OP_LOG_EXT, outfiles);
			System.out.println("Working on it");
			System.out.println(log);
			time = OperationInfo.DATEFORMAT.format(new Date(log.lastModified()));
		}
		addCalculation(opName, opDescription, cline, time, infiles, outfiles, (DefaultMutableTreeNode)node);
	}

	/**
	 * Create a Document from the operations and
	 * global file notes that we can then write as
	 * an XML document easily. Returns null if 
	 * unable to create a document.
	 * @return A Document containing all the global file
	 * notes and operation.
	 */
	private Document processNodes(){
		logger.info("[processNodes()] Entering...");
		//get the root of the record so we can go through
		//...the entire thing
		DefaultMutableTreeNode recordRoot = (DefaultMutableTreeNode)this.getRoot();
		//create a document builder so we can call a parser
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//ignore any comments
		factory.setIgnoringComments(true);
		//Create a parser
		DocumentBuilder parser = null;
		//intialize the parser
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("(processNodes()) Error trying to create a new " +
					"document builder.");
			frame.messenger.createError("Error trying to create a new " +
					"document builder.", "processNodes@Record.java");
			//we failed... return null
			return null;
		}
		//create a new document from the parser
		Document info = parser.newDocument();
		//xml root
		Element root = info.createElement("project");
		//set the attributes of the overall project
		root.setAttribute(HOME_KEY, getHomeFolder());
		if(isRemote())
			root.setAttribute(REMOTE_FOLDER_KEY, getRemoteFolder());
		
		//note all the global descriptions
		Element globalNotes = info.createElement(FOLDER_KEY);
		for(String key: globalFileNotes.keySet()){
			Element fileNote = info.createElement(FILE_KEY);
			fileNote.setAttribute(NAME_KEY, key);
			fileNote.setTextContent(globalFileNotes.get(key));
			globalNotes.appendChild(fileNote);
		}
		root.appendChild(globalNotes);
		
		//note all the operations and queues
		if(!recordRoot.isLeaf()){
			OperationInfo opIndex = (OperationInfo)recordRoot.getFirstChild();
			while(opIndex != null){
				root.appendChild(opIndex.asElement(info));
				opIndex = (OperationInfo)opIndex.getNextSibling();
			}
		}
		//add the root to the document
		info.appendChild(root);
		
		logger.info("[processNodes()] ...exiting");
		//return the document
		return info;
	}
	
	/**
	 * Load the config values from a Properties formated file.
	 *
	 */
	private void loadGlobalConfig(){
		logger.info("[loadGlobalConfig()] Entering...  [" + GLOBAL_CONFIG + "]");
		
		File globalFile = new File(GLOBAL_CONFIG);
		//load up any global configurations
		try {
			globalConfig.load(new FileInputStream(globalFile));
		} catch (FileNotFoundException e) {
			logger.error("(loadGlobalConfig) File not found exception.");
		} catch (IOException e) {
			logger.error("(loadGlobalConfig) IOException");
		}
			
		//local config file set in loadData() and popuateNodes(Document)
		logger.info("[loadGlobalConfig()] ...exiting");
		
	}
	protected abstract void loadRemoteConfig();
	
	protected abstract void saveRemoteConfig();
	/**
	 * Save the config values
	 *
	 */
	public void saveConfig(){
		
		if(globalConfig == null )
			return;
		
		logger.info("[saveConfig()] Entering...");
		File globalFile = new File(GLOBAL_CONFIG);
		
		//setSavedBrowse();
		
		if(isRemote()){
			logger.info("[saveConfig()] data flaged as remote");
			try {
				remoteConfig.store(new FileOutputStream(
						new File(getLocalFolder(), REMOTE_CONFIG_FILE)), "Remote Configuretion");
			} catch (FileNotFoundException e) {
				logger.error("[saveConfig()] FileNotFoundException trying to " +
						"store remote config file.");
				frame.messenger.createError("Can not store remote information. File not found.",
						"saveConfig()@Record.java");
			} catch (IOException e) {
				logger.error("[saveConfig()] IOException trying to " +
						"store remote config file.");
				frame.messenger.createError("Can not store remote information.",
					"saveConfig()@Record.java");
			}
			saveRemoteConfig();
		}else{
			logger.info("(saveConfig) saving global configuration [" + GLOBAL_CONFIG + "]");
			try {
				globalConfig.store(new FileOutputStream(globalFile), "Global configuration for gCLINE");
			} catch (FileNotFoundException e) {
				logger.error("[saveConfig()] FileNotFoundException trying to " +
						"store global config file.");
				frame.messenger.createError("Can not store remote information. File not found.",
						"saveConfig()@Record.java");
			} catch (IOException e) {
				logger.error("[saveConfig()] IOException trying to " +
						"store global config file.");
				frame.messenger.createError("Can not store remote information.",
						"saveConfig()@Record.java");
			}
		}
		
		logger.info("[saveConfig()] ...exiting");
	}
	
	/**
	 * Add a queue to the info document; if there is a queue with that
	 * name already present then replace it.
	 */
	public boolean addQueue(Node qIndex) {
		String qName = ((Element)qIndex).getAttribute(NAME_KEY);
		String qDescription = "PLINK queue.";		

		//check assumptions
		if(qName == null || qDescription == null)
			return false;
		
		//take out the operation if it is already there
		int index = removeOperation(qName);
		QueueInfo newQueue = (QueueInfo)getOp(qName);
		
		//create a new operation node
		newQueue = new QueueInfo(qName, qDescription, this);

		if(index == -1)
			index = this.getChildCount(root);
		logger.info("[addQueue(Node)] adding queue" + qName + "to index: " + index);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
		this.insertNodeInto(newQueue, root, index);
		DefaultMutableTreeNode queueTreeNode = (DefaultMutableTreeNode)this.getChild(root, index);
		this.nodeStructureChanged(root);
		if(update != null)
			//add it to the updater
			update.addOp(qName);
		
		// add the component operations
		// starting at the first child
		Node opIndex = qIndex.getFirstChild();
		//go until you are at the last child
		while(opIndex != null){
			if(opIndex.getNodeName().equals(CALC_KEY)){
				processFileChildren(queueTreeNode, opIndex);
			}
			opIndex = opIndex.getNextSibling();
		}

		return true;		
	}

	public boolean addQueue(String qName, String qDescription, 
		String createTime) {

		//check assumptions
		if(qName == null || qDescription == null)
			return false;

		int index = removeOperation(qName);
		QueueInfo newQueue = new QueueInfo(qName, qDescription, createTime, this);

		if(index == -1)
			index = this.getChildCount(root);
		logger.info("[addQueue(Node)] adding queue" + qName + "to index: " + index);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
		this.insertNodeInto(newQueue, root, index);
		DefaultMutableTreeNode queueTreeNode = (DefaultMutableTreeNode)this.getChild(root, index);
		this.nodeStructureChanged(root);
		if(update != null)
			//add it to the updater
			update.addOp(qName);
		
		return true;

	}

	public boolean addQueue(QueueInfo queue) {
		// TODO refactor into "appendoperationtotree"
		String qName = queue.getName();
		int index = removeOperation(qName);

		if(index == -1)
			index = this.getChildCount(root);
		logger.info("[addQueue(Node)] adding queue" + qName + "to index: " + index);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
		this.insertNodeInto(queue, root, index);
		DefaultMutableTreeNode queueTreeNode = (DefaultMutableTreeNode)this.getChild(root, index);
		this.nodeStructureChanged(root);
		if(update != null)
			//add it to the updater
			update.addOp(qName);
		
		return true;
	}

	/**
	 * Create a new operation and add to this project at the root.
	 * @param cline The command line that is tied to the operation.
	 * @param description A description of the operation.
	 */
	public boolean addCalculation(String cline, String description) {
		return addCalculation(cline, description, null);
	}

	/**
	 * Create a new operation and add to this project.
	 * @param cline The command line that is tied to the operation.
	 * @param description A description of the operation.
	 * @param node the node to which to add the operation (null for root).
	 */
	public boolean addCalculation(String cline, String description, DefaultMutableTreeNode node) {
		CalculationInfo calc = new CalculationInfo(cline, description, this);
		return this.appendOperationToTree(calc, node);
	}

	public boolean addCalculation(String opName, String opDescription, String cline,
		String time, ArrayList<String []> infiles, ArrayList<String []> outfiles, DefaultMutableTreeNode node) {
		// TODO also append files
		try {
			CalculationInfo calc = new CalculationInfo(opName, opDescription, cline, time, infiles, outfiles, (DefaultTreeModel)this);
			return appendOperationToTree(calc, node);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Add a new operation to the specified node in the data tree.
	 * @param op The operation to add.
	 * @param node The node to which to add the operation (null for root).
	 */
	public boolean appendOperationToTree(OperationInfo op, DefaultMutableTreeNode node) {

		// Default node is root
		if (node == null)
			node = (DefaultMutableTreeNode)this.getRoot();
		
		//take out the operation if it is already there
		int index = removeOperation(op.getName());

		// place new operation at end of tree if not present already
		if(index == -1)
			index = this.getChildCount(node);

		logger.info("[appendOperationToTree(String, ...)] adding operation" + op.getName() + "to index: " + index);

		this.insertNodeInto(op, node, index);
		this.nodeStructureChanged(node);
		
		return true;
	}
	
	public void updaterOperation(String name) {
		if(update != null)
			update.addOp(name);
	}

	/**
	 * Remove an operation or queue from the document.
	 * @param opName the name of the operation we are removing
	 * @return the index that this operation was at, -1 if the 
	 * operation was not found.
	 */
	public int removeOperation(String opName){
		
		OperationInfo oldOp = this.getOp(opName);
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getRoot();
		int ans = -1;
		if(oldOp != null){
			ans = root.getIndex(oldOp);
			this.removeNodeFromParent(oldOp);
			//root.remove(oldOp);
		}
		/*if(ans != -1){
			logger.info("Successfully removed " + opName);
		}else{
			logger.info("Unsuccessfully removed " + opName);
		}*/
		//reload();
		return ans;
	}
	
	/**
	 * Set a global file descriptor
	 * @param fileName a string identifying the file (absolute path name)
	 * @param fileDesc a string for local file description
	 */
	public void setGlobalFileDesc(String fileName, String fileDesc){
		globalFileNotes.put(fileName, fileDesc);
	}
	
	/**
	 * Is this record tied to a remote directory?
	 * @return true if it is false otherwise
	 */
	public boolean isRemote(){
		String ans = getRemoteFolder();
		logger.info("[isRemote()] the project is remote: "+ ans);
		return(ans != null);
	}
	
	public boolean saveInfo(){
		return saveInfo(new File(getLocalFolder(), META_FILE));
	}
	
	public boolean backupInfo(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd-HH-mm-ss");
        java.util.Date date = new java.util.Date();
        File outputFile = new File(getLocalFolder(), "backup"+dateFormat.format(date)+".xml");
        return saveInfo(outputFile);
	}
	
	/**
	 * Write the information to the project file of the xml format.
	 * @return true if writing is successful, false otherwise
	 */
	public boolean saveInfo(File metaFile){
		logger.info("(saveInfo(File)) Entering");
		System.out.println("(saveInfo(File)) Entering");
		if(frame.getBrowseOnly()){
			logger.warn("[saveInfo(File)] not saving project because we are browse only");
			System.out.println("[saveInfo(File)] not saving project because we are browse only");
			return false;
		}
		
		if(myLock.availableLock())
			if(myLock.lockFile() == false){
				logger.warn("[saveInfo(File)] unable to lock file(1)");
				System.out.println("[saveInfo(File)] unable to lock file(1)");
				return false;
			}
		if(!myLock.hasLock()){
			if(!myLock.stealLock()){
				frame.setBrowseOnly(true);
				logger.warn("[saveInfo(File)] unable to steal the lock");
				System.out.println("[saveInfo(File)] unable to steal the lock");
				return false;
			}
		}
			
		Document info = processNodes();
		if(isRemote()){
			try {
				remoteConfig.store(new FileOutputStream(
						new File(getLocalFolder(), REMOTE_CONFIG_FILE)), "Remote Configuration");
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				frame.messenger.createError("Can not store remote information. File not found.",
						"saveInfo@Record.java");
			} catch (IOException e) {
				frame.messenger.createError("Can not store remote information.",
						"saveInfo@Record.java");
				System.out.println("Can not store remote information." +
						"saveInfo@Record.java");
			}
		}
		
		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		
		//configure the transformer
		try {
			transformer = tFactory.newTransformer();
			System.out.println("Created transformer");
		} catch (TransformerConfigurationException e1) {
			frame.messenger.createError("Error trying to create transformer."
					,"saveInfo@Record.java");
			System.out.println("[saveInfo(File)] error creating transformer, returning false");
			logger.warn("[saveInfo(File)] error creating transformer, returning false");
			return false;
		}
		//set the source of the data
		DOMSource source = new DOMSource(info);
		//check the flags to see where we should output
		
		//set the receiving stream to the file specified by 
		//...rootfolder/logfile
		StreamResult ofile = null;
		try {
			logger.info("(saveInfo())Write the project info to: " 
					+ metaFile.getAbsolutePath()
					+ " in the file: " + getLocalFolder());
			System.out.println("(saveInfo())Write the project info to: " 
							+ metaFile.getAbsolutePath()
							+ " in the file: " + getLocalFolder());
			System.out.println(metaFile.toString());
			ofile = new StreamResult(new FileWriter(metaFile));
			System.out.println("Streamed!");
		} catch (IOException e) {
			frame.messenger.createError("Error trying to create file stream.",
				"saveInfo@Record.java");
			logger.warn("[saveInfo(File)] error creating filestream, returning false");
			System.out.println("[saveInfo(File)] error creating filestream, returning false");
			System.out.println(e.getMessage());
			return false;
		}
		try {
			System.out.println("Now transforming...");
			System.out.println(transformer.toString());
			transformer.transform(source, ofile);
			System.out.println("Transformed!");
		} catch (TransformerException e) {
			frame.messenger.createError("Error trying to transform source to" +
			"file stream.", "saveInfo@Record.java");
			logger.warn("[saveInfo(File)] error transforming source to filestream, returning false");
			System.out.println("[saveInfo(File)] error transforming source to filestream, returning false");
			System.out.println(e.getMessage());
			return false;
		}
		
		System.out.println("[saveInfo(File)] uploading the metafile: ["+getRemoteFolder()+"] [" + metaFile + "]");
		logger.info("[saveInfo(File)] uploading the metafile: ["+getRemoteFolder()+"] [" + metaFile + "]");
		if(isRemote()){
			new Thread(frame.new Upload(this, true, getRemoteFolder(), new File[]{metaFile})).start();
		}
		
		logger.info("[saveInfo(File)] Exiting true");
		System.out.println("[saveInfo(File)] Exiting true");
		return true;
	}
	
	/**
	 * Print the home folder of the record. If the record
	 * is remote then the home folder is the pwd of the
	 * remote folder; otherwise it is the full path name
	 * of the local folder.
	 * @return a string that is the path of the home folder
	 */
	@Override
	public String toString(){
		if(isRemote()){
			return getRemoteFolder();
		}
		else return getLocalFolder().getAbsolutePath();
	}
	
	/**
	 * This is a host key validator that automatically accepts any
	 * host key sent but doesn't actually save any of them.
	 * @author Kathe Todd-Brown
	 *
	 */
	private static class myHostKeyValidator extends AbstractHostKeyVerification{
		public myHostKeyValidator() throws InvalidHostFileException {super(null);}
		@Override
		public void onDeniedHost(String host) throws TransportProtocolException {
			new ErrorManager(null).createError("Access to '" + host + "' is denied.\n" +
                    "Verify the access granted/denied in the allowed hosts file.",
                    "onDenialHost(String)@myHostKeyValidator.Record.java");
		}
		@Override
		public void onHostKeyMismatch(String host, String allowedHostKey, 
				String actualHostKey) throws TransportProtocolException {
			allowHost(host, actualHostKey, false);}
		@Override
		public void onUnknownHost(String host, String hostKeyFingerprint) 
		throws TransportProtocolException {
			allowHost(host, hostKeyFingerprint, false);}
	}
	
	public boolean connect(){
		logger.info("[connect()] Entering...");
//		if the connection isn't null
		if(conn != null)
			//be sure that we are disconnected
			conn.disconnect();
		String host = remoteConfig.getProperty(HOST_KEY, "");
		int port = new Integer(remoteConfig.getProperty(PORT_KEY, "22"));
		String user = remoteConfig.getProperty(USER_KEY, "");
		conn = connect(frame.messenger, frame, host, user, port);
		logger.info("[connect()] ...exiting");
		return conn != null;
	}
	
	/**
	 * Connect the ssh client.
	 * @return false if an error that we can cetch happened (not always the case!)
	 * true otherwise.
	 */
	@SuppressWarnings("unchecked")
	static public SshClient connect( ErrorManager em, JFrame frame,
			String host, String user, int port){
		logger.info("[connect(ErrorManager, JFrame, String, String, int)] Entering...");
		//set the connection to something new
		SshClient conn = new SshClient();
		
		////////////
		///Establish connection
		////////////
		try {
			//conn.connect(host,port, new DialogHostKeyVerification(frame, null));
			conn.connect(host,port, new myHostKeyValidator());
		} catch (NumberFormatException e) {
			logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
					"Can not establish a connection: NumberFormatException");
			em.createError("NumberFormatException: Error trying to establish connection." +
					"["+ user+"@"+host +":"+ port +"].",
					"connect():Record.java");
			return null;
		} catch (IOException e) {
			logger.error("([connect(ErrorManager, JFrame, String, String, int)] " +
					"IOException: Can not establish a connection: IOException " + e.getMessage() +
					"\n " + (e.getStackTrace()[0]).toString());
			em.createError("Error trying to establish " +
					"ssh connection " +
					"["+ user+"@"+host +":"+ port +"].",
					"connect:Project.java");
			return null;
		}
		
		///////////////
		/////authenticate the connection
		///////////////
		
		//flags if we have a key-board interactive
		boolean kyboardI = false;
		try {
			List <String> options = conn.getAvailableAuthMethods(user);
		
			String allOptions = "";
			for(String name: options){allOptions += " ["+ name + "]";}
			
			logger.info("(connect) We have the following auterization options:"+allOptions);
			//if there is a keyboard interactive option
			kyboardI = options.contains("keyboard-interactive");
		} catch (IOException e2) {
			logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
					"Can not establish ssh auth methods");
			em.createError("Error trying to establish " +
					"ssh auth methods" +
					"["+ user+"@"+host +":"+ port +"]",
					"connect():Project.java");
			return null;
		}
		
		//by detault we fail the autherization
		int result = AuthenticationProtocolState.FAILED;
		logger.info("[connect(ErrorManager, JFrame, String, String, int)] " +
				"keyboard interactive flag is: [" + kyboardI + 
				"] result is: [" + result + "]");
		//create authenticators and authenticate the connection
		if(kyboardI){
			KBIAuthenticationClient kbi =
				new KBIAuthenticationClient();
			KBIRequestHandlerDialog dialog = 
	              new KBIRequestHandlerDialog(frame);
			kbi.setKBIRequestHandler(dialog);
			kbi.setUsername(user);
			
			//get the result
			try {
				result = conn.authenticate(kbi);
				logger.info("[connect(ErrorManager, JFrame, String, String, int)] " +
						"result value is: " + result );
			} catch (IOException e) {
				logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
						"Can not authenticate " +
						"(keyboard-interative) ssh connection " +
						"["+ user + "@" +host+":"+ port+"]. \n");
				em.createError("Error trying to authenticate " +
						"(keyboard-interative) ssh connection " +
						"["+ user + "@" +host+":"+ port+"].",
						"launchLogin:ProjectDialog.java");
				return null;
			}
		} 
		if(result == AuthenticationProtocolState.FAILED){
			PasswordAuthenticationClient pwd = 
				new PasswordAuthenticationClient();
			PasswordAuthenticationDialog dialog =
				new PasswordAuthenticationDialog(frame);
			pwd.setUsername(user);
			
			try {
				pwd.setAuthenticationPrompt(dialog);
			} catch (AuthenticationProtocolException e) {
				logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
						"Can not set authenticate prompt" +
						"(password)" +
						"["+ user + "@" +host+":"+ port+"]. \n");
				em.createError("Can not set authenticate prompt" +
						"(password) ["+ user + "@" +host+":"+ port+"].",
						"launchLogin:ProjectDialog.java");
				return null;
			}
			try {
				result = conn.authenticate(pwd);
			} catch (IOException e) {
				logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
						"Can not authenticate " +
						"(password) ssh connection " +
						"["+ user + "@" +host+":"+ port+"]. \n");
				em.createError("Error trying to authenticate " +
						"(password) ssh connection " +
						"["+ user + "@" +host+":"+ port+"].",
						"launchLogin:ProjectDialog.java");
				return null;
			}
		}
		
		/////////////////
		////// Check the results.
		/////////////////
	  
		if(result==AuthenticationProtocolState.FAILED){
			logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
					"Error failure to " +
					"establish ssh connection,  AuthenticationProtocolState: FAILED"+
					"["+ user + "@" +host +":"+ port+"].");
			em.createError("Error failure to " +
					"establish ssh connection"+
					"["+ user + "@" +host +":"+ port+"].",
					"connect:Project.java");
			return null;
		}
		if(result==AuthenticationProtocolState.PARTIAL){
			logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
					" Error failure to " +
					"establish full ssh connection, AuthenticationProtocolState: PARTIAL"+
					"["+ user + "@" +host +":"+ port+"].");
			em.createError("Error failure to " +
					"establish full ssh connection"+
					"["+ user + "@" +host +":"+ port+"].",
					"connect:Project.java");
		    return null;
		}
		
		if(result==AuthenticationProtocolState.COMPLETE){
			logger.info("[connect(ErrorManager, JFrame, String, String, int)] " +
					"Auth complete!");
			return conn;
		}
		logger.error("[connect(ErrorManager, JFrame, String, String, int)] " +
				"Didn't complete authorization!");
		//we don't know what happened if we get here but we don't like it!
		return null;
	}
	
	
	/**
	 * Create a Record with null or empty fields.
	 *
	 */
	public Record(){
		super(new DefaultMutableTreeNode());
		logger.info("[Record()] Entering...");
		frame = null;
		globalConfig = new Properties();
		//frame.messenger = new ErrorManager(null);
		remoteConfig = new Properties();
		localConfig = new Properties();
		globalFileNotes = new HashMap<String, String>();
		myLock = null;
		conn = null;
		update = null;
		loadGlobalConfig();
		logger.info("[Record()] ...exiting");
	}
	/**
	 * Create a Record initialized to a particular StartFrame.
	 * @param f The StartFrame we are attaching this Record to.
	 */
	public Record(StartFrame f){
		this();
		logger.info("[Record(StartFrame)] Entering...");
		frame = f;
		DefaultMutableTreeNode temp = new DefaultMutableTreeNode("");
		this.setRoot(temp);
		this.nodeChanged(temp);
		//always reset the browse button
		if(f != null)
			f.setBrowseOnly(false);
		logger.info("[Record(StartFrame)] ...exiting");
	}
	/**
	 * Create a Record.
	 * @param f The StartFrame w are attaching this Record to.
	 * @param localName A String that is the name of the local 
	 * project folder for this project.
	 */
	public Record(StartFrame f, String localName){
		this(f);
		logger.info("[Record(StartFrame, String)] Entering...");
		frame.messenger = new ErrorManager(frame);
		remoteConfig = new Properties();
		
		localConfig = new Properties();
		
		isNew = loadData(new File(localName + File.separator + META_FILE),
				localName, false);
		
		myLock = new Lock(frame, this);
		if(!frame.getBrowseOnly()){
			if(myLock.availableLock())
				myLock.lockFile();
			else
				frame.setBrowseOnly(!myLock.stealLock());
		}
		logger.info("[Record(StartFrame, String)] ...exiting");
	}
	/**
	 * Create a Record.
	 * @param f The StartFrame w are attaching this Record to.
	 * @param localName A String that is the name of the local 
	 * project folder for this project.
	 * @param remoteFileName A String that is the name of the remote
	 * project folder for this project.
	 * @param hostname A String that is the name of the remote host
	 * @param username A String that is the name of the user on the
	 * remote host.
	 * @param portnum A String that is the port number we wish to
	 * connect to.
	 * @param c A SshClient that is used to connect to the remote
	 * host.
	 */
	public Record(StartFrame f, String localName, 
			String remoteFileName, String hostname,
			String username, String portnum,
			SshClient c){
		this(f);
		logger.info("[Record(StartFrame, String, String, String, " +
				"String , String, SshClient)] Entering...");
		//save the properties to the correct fields
		conn = c;
		frame.messenger = new ErrorManager(frame);
		
		logger.info("[Record(StartFrame, String, String, String, " +
		"String , String, SshClient)] set properties");
		
		if(!remoteFileName.endsWith("/"))
			remoteFileName += "/";
		remoteConfig.setProperty(REMOTE_FOLDER_KEY, remoteFileName);
		remoteConfig.setProperty(HOST_KEY, hostname);
		remoteConfig.setProperty(USER_KEY, username);
		remoteConfig.setProperty(PORT_KEY, portnum);
		try {
			remoteConfig.store(new FileOutputStream(
					new File(getLocalFolder(), REMOTE_CONFIG_FILE)), "Remote Configuretion");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			frame.messenger.createError("Can not store remote information. File not found.",
					"saveInfo@Record.java");
		} catch (IOException e) {
			frame.messenger.createError("Can not store remote information.",
			"saveInfo@Record.java");
		}
		
		localConfig.setProperty(LOCAL_FOLDER_KEY, localName);
		
		logger.info("[Record(StartFrame, String, String, String, " +
		"String , String, SshClient)] download and load metafile");
		//***********Metafile
		//delete the old metafile
		new File(getLocalFolder(),META_FILE ).delete();
		//download the new one, ignore if there isn't a metafile
		frame.new Download(this, true, getLocalFolder(), new String [] {remoteFileName + META_FILE}).run();
		//load the metafile
		isNew = loadData(new File(localName + File.separator + META_FILE),
				remoteFileName, true);
		
		//************Locking
		logger.info("[Record(StartFrame, String, String, String, " +
			"String , String, SshClient)] locking");
		
		myLock = new Lock(frame, this);
		if(myLock.availableLock())
			myLock.lockFile();

		//************Load the remote configuration
		logger.info("[Record(StartFrame, String, String, String, " +
		"String , String, SshClient)] calling loadRemoteConfig()");
		
		loadRemoteConfig();
		
		logger.info("[Record(StartFrame, String, String, String, " +
		"String , String, SshClient)] ...exiting");
	}
	
	/**
	 * Close the Record up properly. Set everything to null.
	 *
	 */
	public void properClosing(){
		logger.info("[properClosing()] Entering...");
		saveConfig();
		if(update != null){
			update.cancel();
			update = null;
		}
		if(myLock != null && myLock.hasLock()){
			saveInfo();
			myLock.unlockFile();
			myLock = null;
		}
		if(conn != null)
			conn.disconnect();
		
		remoteConfig = null;
		globalConfig = null;
		localConfig = null;
		this.setRoot(null);
		logger.info("[properClosing()] ...exiting");
		
	}	
}
