package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.RestoreDataDialog;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.PLINK_CreateQueue;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.PLINK_Execute;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.FormMenuCreator;

/**
 * A Menu Bar that is specific to PLINK.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class PlinkMenuBar extends JMenuBar{
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(PlinkMenuBar.class);
	
	/**
	 * A Project that we are going to pass
	 * ...to the form dialogs.
	 */
	private Project data;
	/**
	 * A GPLINK that this menu bar is attached to.
	 */
	private GPLINK frame;
	
	//These are needed as class variables because
	//...we access them from within on the fly action
	//...listeners.
	/**
	 * A String holding the most recent project that
	 * have been opened.
	 */
	private String project1;
	/**
	 * A String holding the second most recent project that
	 * have been opened.
	 */
	private String project2;
	/**
	 * A String holding the third most recent project that
	 * have been opened.
	 */
	private String project3;
	/**
	 * A String holding the fourth most recent project that
	 * have been opened.
	 */
	private String project4;
	
	private JMenu projectMenu;
	private JMenu plinkMenu;
	private JMenu queueMenu;
	private JMenu advancedMenu;
	private JMenu helpMenu;
	private JMenuItem config;
	private JMenuItem save;
	private JMenuItem backup;
	private JMenuItem restore;
	
	void setEnabledMenu(){
		boolean state = data != null
						&& data.getLocalFolder() != null
						&& frame.getBrowseOnly() == false;
		logger.info("[setEnabledMenu()] setting the items: " + state);
		plinkMenu.setEnabled(state);
		queueMenu.setEnabled(state);
		advancedMenu.setEnabled(state);
		config.setEnabled(state);
		save.setEnabled(state);
		backup.setEnabled(state);
		restore.setEnabled(state);
	}
	
	/**
	 * Open up a project with the appropriate remote configurations.
	 * @param folder A String that is the name of the local project
	 * folder.
	 */
	
	private void launchOldProject(String folder){
		File remoteConfig = new File(folder, Project.REMOTE_CONFIG_FILE);
		if(remoteConfig.exists()){
			Properties remote = new Properties();
			try {
				remote.load(new FileInputStream(remoteConfig));
			} catch (FileNotFoundException e1) {
				logger.error("[launchOldProject()] FileNotFoundException " +
						"occured when trying to load remote config file");
			} catch (IOException e1) {
				logger.error("[launchOldProject()] IOException " +
					"occured when trying to load remote config file");
			}
			String remoteName = remote.getProperty(Project.REMOTE_FOLDER_KEY);
			String host = remote.getProperty(Project.HOST_KEY);
			String user = remote.getProperty(Project.USER_KEY);
			String port = remote.getProperty(Project.PORT_KEY);
			if(remoteName != null 
					&& host != null 
					&& user != null 
					&& port != null){
				new PLINK_Open(frame, folder, remoteName, host, user, port);
			} else{
				new PLINK_Open(frame, folder);
			}
		}else
			new PLINK_Open(frame, folder);
	}
	
	/**
	 * Create the menu for general project operations.
	 * This includes: Open, Save, Export, Rescan, Configure,
	 * previous projects and Exit.
	 * @return JMenu that holds the general options.
	 */
	private JMenu createProjectMenu(){
		logger.info("[creatingProjectMenu()] Entering");
		
		JMenu projMenu = new JMenu("Project");

		//open a project file, save this as "folderName.gCLINE"
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("open is clicked!");
				new PLINK_Open(frame);
			}
		});
		
		//save the project file
		save = new JMenuItem("Save");
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!data.saveInfo()){
					logger.info("save is clicked!");
					frame.messenger.createError(
							"Can not save information.", "createProjectMenu");
				}
			}
		});
		
		//backup the project to xml
		backup = new JMenuItem("Save As XML");
		backup.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!data.backupInfo()){
					logger.info("[createProjectMenu()] Can not create backup file");
					frame.messenger.createError(
							"Can not backup information.", "createProjectMenu");
				}
			}
		});
		
		restore = new JMenuItem("Restore from XML");
		restore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new RestoreDataDialog(frame, data);
			}
		});
		
		//export the project to text
		
		//open the configuration dialog
		config = new JMenuItem("Configure");
		config.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("config is clicked!");
				new PLINK_Config(frame).makeVisible();
			}
		});
		
		//add these to the menu
		projMenu.add(open);
		projMenu.addSeparator();
		projMenu.add(save);
		projMenu.add(backup);
		projMenu.add(restore);
		projMenu.addSeparator();
		projMenu.add(config);
		if(data != null){
			project1 = data.getP1();
			if(project1 != null && !project1.matches("^\\s*$")){
				projMenu.addSeparator();
				JMenuItem firstProject = new JMenuItem(project1);
				logger.info("adding " + project1);
				firstProject.addActionListener(new ActionListener(){
					
					public void actionPerformed(ActionEvent e) {
						logger.info(project1 + 
								" is firing opening dialog Param:" 
								+ e.paramString() 
								+" ID:"
								+ e.getID()
								);
						launchOldProject(project1);
					}
				});
				projMenu.add(firstProject);
			}
			
			project2 = data.getP2();
			if(project2 != null&& !project2.matches("^\\s*$")){
				JMenuItem secondProject = new JMenuItem(project2);
				secondProject.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						logger.info(project2+ " is firing opening dialog");
						launchOldProject(project2);
					}
				});
				projMenu.add(secondProject);
			}
			
			project3 = data.getP3();
			if(project3 != null && !project3.matches("^\\s*$")){
				JMenuItem thirdProject = new JMenuItem(project3);
				thirdProject.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						logger.info(project3 + " is firing opening dialog");
						launchOldProject(project3);
					}
				});
				projMenu.add(thirdProject);
			}
			
			project4 = data.getP4();
			if(project4 != null && !project4.matches("^\\s*$")){
				JMenuItem fourthProject = new JMenuItem(project4);
				fourthProject.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						logger.info(project4 + " is firing opening dialog");
						launchOldProject(project4);
					}
				});
				projMenu.add(fourthProject);
			}
		}
		//add an exit
		JMenuItem exitButton = new JMenuItem("Exit");
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("GPLINK is closing (1)");
				if(data != null)
					data.properClosing();
				System.exit(0);
			}});
		projMenu.addSeparator();
		projMenu.add(exitButton);
		
		return projMenu;
		
	}
	
	/**
	 * Create the menu for command specific forms.
	 * @return A JMenu that has the command specific forms.
	 */
	private JMenu createFormsMenu(){
		FormMenuCreator menuGenerator = new FormMenuCreator();
		logger.info("[PlinkMenuBar].createFormsMenu() returning blank");
		PLINK_Execute execute_form = new PLINK_Execute(frame);
		return menuGenerator.createFormMenu(execute_form);
	}
	
	/**
	 * Create the menu for managing queues of PLINK commands.
	 * @return A JMenu with queue management options on.
	 *
	 */
	private JMenu createQueueMenu(){
		queueMenu = new JMenu("Queue PLINK commands");

		JMenuItem newQueue = new JMenuItem("New queue");
		newQueue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new PLINK_CreateQueue(frame);
			}
		});
		queueMenu.add(newQueue);

		JMenuItem importQueue = new JMenuItem("Import queue");
		importQueue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO import queue from a txt file of 1 plink command per line
				// ^ as in, it should automatically recognise input and outputs
				// relative to previous commands' input and output
			}
		});

		return queueMenu;
	}

	/**
	 * Create the advanced command entry menu.
	 * @return A JMenu with advanced options on.
	 *
	 */
	private JMenu createAdvMenu(){
		advancedMenu = new JMenu("Advanced");
		
		JMenuItem importOp = new JMenuItem("Import PLINK operation");
		importOp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new PLINK_Import(frame, data);
			}
		});
		advancedMenu.add(importOp);
		
		JMenuItem basicCLine = new JMenuItem("Create PLINK command");
		basicCLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new PLINK_Execute(frame);
			}
		});
		advancedMenu.add(basicCLine);
		
		JMenuItem inputNonPlink = new JMenuItem("Add non-PLINK command");
		inputNonPlink.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new AddGenericOp(frame);
			}
		});
		advancedMenu.add(inputNonPlink);
		
		return advancedMenu;
	}

	/**
	 * Create a menu for help and support.
	 * @return A JMenu with help, documentation, and tutorials on.
	 * TODO could have About here as well? as a sub-menu item?
	 */
	private JMenu createHelpMenu() {
		helpMenu = new JMenu("Help");
		// TODO!!
		return helpMenu;
	}

	private JMenuItem createAboutMenu() {
		JMenuItem abtMenu = new JMenuItem("About");
		abtMenu.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				frame.messenger.createMessage(
						"gPLINK vs" + frame.version +
						"\n Released under GNU GPLv2\n" +
						" Writen by: Kathe Todd-Brown, Shaun Purcell\n" +
						"http://pngu.mgh.harvard.edu/~purcell/plink/", "");
			}
		});
		abtMenu.setMaximumSize(new Dimension(
				abtMenu.getPreferredSize().width, abtMenu.getMaximumSize().height));
		
		return abtMenu;
	}
	
	/**
	 * Create an instance of this plink specific menu bar.
	 * @param givenFrame The gPLINK instance that this
	 * is attached to.
	 */
	public PlinkMenuBar(GPLINK givenFrame){
		super();
		logger.info("[PlinkMenuBar(GPLINK)] Entering...");
		//initialise most of the fields
		this.data = givenFrame.data;
		frame = givenFrame;

		projectMenu = createProjectMenu();
		plinkMenu = createFormsMenu();
		queueMenu = createQueueMenu();
		advancedMenu = createAdvMenu();
		helpMenu = createHelpMenu();

		//layout the menu bar
		add(projectMenu);
		add(plinkMenu);
		add(queueMenu);
		add(advancedMenu);
		add(helpMenu);
		setEnabledMenu();
		add(Box.createHorizontalGlue());
		add(createAboutMenu());

		logger.info("[PlinkMenuBar(GPLINK)] ...exiting");
	}
	
}
