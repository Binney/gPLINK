package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import java.io.File;

/**
 * This class holds the key words used.
 * @author Kathe Todd-Brown
 *
 */
public interface KeyWords {
	
	/***** Files ****/
	/**
	 * Holds the location of the users current directory.
	 * We will uses this to store the global variables.
	 */
	static public String LOCAL_DIR = System.getProperty("user.home");

	static public String REMOTE_GLOBAL_CONFIG = ".gplink_config";
	
	static public String GLOBAL_CONFIG = LOCAL_DIR + 
		File.separator + REMOTE_GLOBAL_CONFIG;
	
	
	/***** Remote Connection keys*****/
	/**
	 * A string that states the remote host key.
	 */
	static  public String HOST_KEY = "host";
	/**
	 * A string that states the remote port key.
	 */
	static public String PORT_KEY = "port";
	/**
	 * A string that states the remote user key.
	 */
	static public String USER_KEY = "user";
	/**
	 * the remote project folder name
	 */
	static public String REMOTE_FOLDER_KEY = "remote_folder";
	
	
	/***** Record keys *****/
	/**
	 * key word for queue job nodes
	 */
	static public String QUEUE_KEY = "queue";
	/**
	 * key word for the operations nodes
	 */
	static public String CALC_KEY = "calculation";
	
	/**
	 * key word for the name attributes
	 */
	static public String NAME_KEY = "name";
	
	/**
	 * key word for the command line attributes
	 */
	static public String CLINE_KEY = "cline";
	
	static public String TIMESTAMP_KEY = "time";
	
	/**
	 * key word for the input files group
	 */
	static public String INFILE_KEY = "input";
	
	/**
	 * key word for the output file group
	 */
	static public String OUTFILE_KEY = "output";
	
	/**
	 * key word for the file node types
	 */
	static public String FILE_KEY = "entity";
	
	/**
	 * key word for the global file notations
	 */
	static public String FOLDER_KEY = "folder";
	
	/***** Machine config specific keys *****/
	/**
	 * haploview path key
	 */
	//static public String HAPLO_KEY = "haploview";
	
	/**
	 * haploview append key
	 */
	//static public String HAPLO_APPEND_KEY = "haploview_append";
	
	/**
	 * user editor key
	 */
	static public String USER_EDITOR_KEY = "editor";
	
	/**
	 * local update interval key
	 */
	static public String LOCAL_UPDATE_INTERVAL_KEY = "local_update";
	
	/**
	 * remote update interval key
	 */
	static public String REMOTE_UPDATE_INTERVAL_KEY = "remote_update";
	
	/**
	 * 
	 */
	static public String BROWSE_KEY = "browse";
	/***** Project config specific keys *****/
	
	/**
	 * local project file
	 */
	static public String LOCAL_FOLDER_KEY="local_folder";
	/**
	 * command key
	 */
	static public String COMMAND_KEY = "cPath";
	
	/**
	 * PLINK prefix key
	 */
	static public String COMMAND_PREFIX_KEY = "cPrefix";
	
	/**
	 * version key
	 */
	static public String VS_KEY = "vs";
	
	/**
	 * project file key
	 */
	static public String HOME_KEY = "home";
	
	/**
	 * key for the project that was opened latest
	 */
	static public String P1_KEY = "project1";
	/**
	 * key for the second project
	 */
	static public String P2_KEY = "project2";
	/**
	 * key for the third project
	 */
	static public String P3_KEY = "project3";
	/**
	 * key for the forth project
	 */
	static public String P4_KEY = "project4";
	
	/***** File Extentions*****/
	/**
	 * A string that identifies the file extention
	 * for the storing of the operaiton "status".
	 */
	//static public String GCLINE_EXT = ".gcline";
	
	/**
	 * String that identifies the file extention
	 * for
	 */
	static public String META_FILE = ".metafile_gPLINK";
	
	/**
	 * file that holds the project lock
	 */
	static public String LOCK_FILE = ".lock_gPLINK";
	
	/**
	 * file that holds the remote configuration
	 */
	static public String REMOTE_CONFIG_FILE = ".remote_gPLINK";
	
}
