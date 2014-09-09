package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import gCLINE.uk.ac.cam.sb913.gCLINE.PopUpMenu;
import gCLINE.uk.ac.cam.sb913.gCLINE.StartFrame;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.FileView;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.FolderView;
import gCLINE.uk.ac.cam.sb913.gCLINE.pane.OpView;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.PLINK_Execute;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.PLINK_CreateQueue;

/**
 * The entry class for gPLINK. Creates a frame from
 * which you can interact with the operations tree,
 * file list, and forms that compose gPLINK.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public final class GPLINK extends StartFrame {
	static public String versionHistory;
	@Override
	protected void setVersion() {
		version = "2.050"; 
		versionHistory = 
			"2.050 BUGFIX sort by fileroots in the forms \n" +
			"2.049 FEATURE UPDATE polished up the proxy assoc form \n"+
			"2.048 BUGFIX & FEATURE UPDATE 1)add first pass drag and drop from folder list to operations" +
			" 2) prevent inclusion of numbers as file names 3) updated forms fixed link file to operation" +
			" 4) changed how the opTree is updated \n" + 
			"2.047 FEATURE UPDATE add conditional haplotype form - 01.07.08 \n" +
			"2.046 BUGFIX catch trivial roots in log files (ie [.log] and [.gplink] "+
			"are not valid files to construct an operation from) - 01.04.08 \n"+
			"2.045 FEATURE UPDATE add proxy association form and commenting - 1.3.08 \n" +
			"2.044 BUGFIX if one autherization fails for some reason "+
			"then go to the second. \n"+
			"2.043 BUGFIX alternative editor now looks localy all the time - 11.16.07 \n" +
			"2.042 FEATURE UPDATE auto updater is set to default 5 seconds in local mode "+
			"and 30 seconds in remote mode. These are now to different config parameters -11.16.07 \n" +
			"2.041 FEATURE UPDATE remove browse option from menu option and the default to " +
			"not browse, browse is only activated when user chooses not to steal the "+
			"lock for a project -11.16.07 \n" +
			"2.040 FEATURE UPDATE add text to alternative pheno panel, add \"Advance\" menu, "+
			"add ok button check for threshold settings -11.15.07 \n" +
			"2.039 BUGFIX remote time stamps imported correctly with operation -11.08.07 \n"+
			"2.038 BUGFIX fixed optree rendering issue when rescan "+
			"timing changed in config dialog -11.02.07 \n"+
			"2.037 formating fixes -11.02.07 \n"+
			"2.036 icon's auto refresh when an op completes -11.02.07 \n"+
			"2.035 fixed popup trigger -11.01.07 \n"+
			"2.034 format forms -10.30.07 \n"+
			"2.033 do not ad the exit status file to the optree -10.30.07 \n"+
			"2.032 import plink operations from remote directory correctly -10.30.07 \n"+
			"2.031 reactive buttons on form when filter/threshold closed via x -10.30.07 \n"+
			"2.030 fix bug with remote config file not being there "+
			"2)error dialog always on top 3)fixed lable/parent of plink_exe dialog -10.30.07 \n"+
			"2.029 quote output file name appropreately -10.30.07 \n"+
			"2.028 cetch nulls in file transfure to disconnect cleanly. "+
			"2.027 freeze remote flag on opening "+
			"until a local file is selected -10.30.07 \n"+
			"2.026 fixed null pointer error trying to close "+
			"a remote connection without a connection \n"+
			"2.025 more fun with the drop down list "+
			"in the import dialog. -10.17.07 \n"+
			"2.024 autoUpdater waits for files to download now -10.17.07 \n"+
			"2.023 fix homozygosity form -10.16.07 \n"+
			"2.022 fixed null pointer in relationship "+
			"heirarchy dialog -10.15.07 \n"+
			"2.021 fix error message popup that happened "+
			"when there was no metafile in the remote directory to download -10.04.07 \n"+
			"2.020 import bug fix \n"+
			"2.019 import drop down is now works - 10.03.07 \n"+
			"2.018 import no longer assumes that an "+
			"operation is completed successfully and checks the log "+
			"file to see what the .gplink file should be set to. \n"+
			"2.017 1)merge form fixed 2) null pointer "+
			"caught in opTree pop-up menu - 10.03.07 \n"+
			"2.016 back button in remote file browser "+
			"now only steps back one directory at a time - 10.03.07 \n"+
			"2.015 major bug fix for remote mode "+
			"including 1) full file names in command line "+
			"2) upload/download lock/metafile fix - 10.01.07 \n"+
			"2.014 added form - 9.28.07 \n"+
			"2.013 never adds --gplink if it's already in "+
			"the command line-9.26.07 \n"+
			"2.012 bug fix adding alternative phenotype - 9.26.07 \n"+
			"2.011 bug fix selecting remote file names - 9.26.07 \n"+
			"2.010 fix import so that it quotes filenames - 09.19.07 \n"+
			"2.009 fixed null pointer exception for "+
			"short binary inport in forms (when it was empty) - 09.19.07 \n"+
			"2.008 file lists now go through a bubble sort "+
			"to make sure they are lists alphabetically \n"+
			"2.007 fixed loading error \n" +
			"2.006 can now launch Haploview \n"+
			"from gPLINK - 08.06.07 \n" +
			"2.005 add restore from backup option - 08.02.07 \n" +
			"2.004 add time stamps to ops - 8.02.07 \n"+
			"2.003 added crtl+1/2 for updown on "+
			"optree - 8.02.07 \n"+
			"2.002 fixed downloading file and "+
			"opening file in alt and default editors \n" +
			"2.001 reworded API for gPLINK. Shrink lines of code from 15K to 10K and fixed threading issue";
	}
	
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(GPLINK.class);
	
	private static Logger gCLINELogger;
	private static Logger gPLINK2Logger;
	
	/**
	 * The current project we are looking at.
	 */
	public Project data;
	/**
	 * The menu bar for our gPLINK window.
	 */
	PlinkMenuBar menu;
	
	/**
	 * Determines whether verbose output mode is on; can be switched
	 * by passing command line argument "verbose".
	 */
	static boolean verbose;

	public void setVerbose(boolean value) {
		verbose = value;

		if (verbose) {
			Logger.getLogger("com.sshtools").setLevel(Level.INFO);
			gCLINELogger.setLevel(Level.INFO);
			gPLINK2Logger.setLevel(Level.INFO);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd-HH-mm-ss");
	        java.util.Date date = new java.util.Date();
	        String logFile = System.getProperty("user.home") + File.separator + dateFormat.format(date)+"gplinkvs_"+version+".log";
			System.out.println(logFile);
			new File(logFile).delete();
			try {
				gPLINK2Logger.addAppender(new FileAppender(new PatternLayout("%r [%t] %-5p %c - %m%n"), logFile, true));
				gCLINELogger.addAppender(new FileAppender(new PatternLayout("%r [%t] %-5p %c - %m%n"), logFile, true));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			setTitle("gPLINK-"+version+" logging: [" + logFile + "]" );
			logger.info("[GPLINK] switched logger on; now logging activity");
		} else {
			// TODO switch logger off, ie make it stop printing everything - reverse of above
		}
	}
	
	public static boolean getVerbose() {
		return verbose;
	}

	/**
	 * Create a windowListener that will properly
	 * close this class.
	 */
	WindowListener properClosing = new WindowListener(){

		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {
			logger.info("GPLINK is closing (2)");
			if(data != null)
				data.properClosing();
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}
	};
	/**
	 * Re-set the panels of based on given project.
	 * @param d A given Project we want the panels to
	 * reflect.
	 */
	public void layoutPanels(Project d){
		//set the old parameters to null to make
		//sure that we don't get any memory leaks
		data = null;
		fileViewer = null;
		folderViewer = null;
		opViewer = null;
		
		//reset the data
		data = d;
		fileViewer = new FileView();
		folderViewer = new myFolderView(data);
		opViewer = new myOpView(data, fileViewer);
		//set up the menu bar
		menu = new PlinkMenuBar(this);
		
		// place everything in split panels to get
		// things organised
		JSplitPane splitPaneHorz = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                opViewer.toJScrollPane(),
				fileViewer);
		JSplitPane splitPaneVert = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				folderViewer.toJScrollPane(),
				splitPaneHorz);
		//remove everything
		getContentPane().removeAll();
		//add the split pane
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.PAGE_END);
		
		setJMenuBar(menu);
		//pack everything up
		pack();
	}

	
	@Override
	public void layoutPanels() {
		layoutPanels(new Project(this));
	}
	
	@Override
	public void setBrowseOnly(boolean state) {
		//set the browse
		isBrowseOnly = state;
		//check to see if we need to release a lock
		if(state 
				&& data != null 
				&& data.myLock != null
				&& data.myLock.hasLock()){
			//release the lock
			data.myLock.unlockFile();
		}

		
		if(data != null){
			data.saveConfig();
		}
		
		if(menu != null)
			//shift over the menu
			menu.setEnabledMenu();
		
	}
	
	/**
	 * Create a new instance of gPLINK.
	 *
	 */
	public GPLINK(){
		super("gPLINK");
		this.setTitle("gPLINK-"+this.version);
		//deal with the loggers
		//this is configured in the original StartFrame
		//BasicConfigurator.configure();
		Logger.getLogger("com.sshtools").setLevel(Level.OFF);
		gCLINELogger = Logger.getLogger("gCLINE.uk.ac.cam.sb913.gCLINE");
		gPLINK2Logger = Logger.getLogger("gPLINK2.uk.ac.cam.sb913.gPLINK2");
		logger.info("User OS: " + System.getProperty("os.name") + ": " + System.getProperty("os.arch") + ": " + System.getProperty("os.version") );
		logger.info("Java: " + System.getProperty("java.version"));
		gCLINELogger.setLevel(Level.OFF);
		gPLINK2Logger.setLevel(Level.OFF);
		//Logger.getLogger("gCLINE.uk.ac.cam.sb913.gCLINE.data.Recode").setLevel(Level.INFO);


		filesInTransit = new ArrayList<String>();
		
		//add proper closing
		this.addWindowListener(properClosing);
		
	}
	
	/**
	 * The main function that is where it all starts
	 * @param args
	 */
	public static void main(String[] args) {
		//try to create a system based look a feel
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Couldn't set \"system\" look and feel.");
	    	try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				System.err.println("Could not set \"cross platform\" look and feel. Going to default.");
			} 
	    }

		// Verbose mode is on if "verbose" is passed as an argument to gPLINK. If you double-click
		// gPLINK in Windows, say, verbose will be off as no arguments were passed.
	   verbose = (args.length>0 && args[0].equals("verbose"));

	    //create gPLINK
		GPLINK mw = new GPLINK();
		//pack everything up
		mw.pack();
		//set it to visible
		mw.setVisible(true);
	}
	/**
	 * An operations viewer (OpView) specific to the
	 * gPLINK window.
	 * @author Kathe Todd-Brown
	 *
	 */
	class myOpView extends OpView{
		/**
		 * Create the OpView for a gPLINK window.
		 * @param d The Record you are looking at in the window.
		 * @param f The FileView attached to the gPLINK window so that 
		 * you can look at the log file attached to a particular operation.
		 */
		public myOpView(Record d, FileView f) {
			super(d, f);

			if(data.update != null){				
				//Set the icon for leaf nodes.
		        ImageIcon successIcon = createImageIcon("images/success.gif");
		        ImageIcon failIcon = createImageIcon("images/fail.gif");
		        ImageIcon runningIcon = createImageIcon("images/running.gif");
		        ImageIcon fileInfoIcon = createImageIcon("images/fileInfoIcon.GIF");
		        ImageIcon queuedIcon = createImageIcon("images/queuedIcon.gif");
		        //if these all exist then use our renderer
		        if (successIcon != null && failIcon != null && runningIcon != null && queuedIcon != null) {
		            myTree.setCellRenderer(
		            		data.update.new MyRenderer(successIcon, failIcon, runningIcon, queuedIcon, fileInfoIcon));
		        } else {
		        	logger.warn("[myOpView(Record, FileView)] one of the icons is null!");
		        }
			} else {
				logger.warn("[myOpView(Record, FileView)] data.update is null!");
			}
		}

		@Override
		protected PopUpMenu createPopUp(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			return new myPopUpMenu(mf, d, givenFiles, givenOp);
		}
		
		/** 
		 * Intelligently create an image URL
		 * @return an ImageIcon, or null if the path was invalid.
		 */
	    public ImageIcon createImageIcon(String path) {
	    	java.net.URL imgURL = GPLINK.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	        	logger.error("[createImageIcon()] Can't find file: " + path);
	            return null;
	        }
	    }
		
	}
	/**
	 * The FolderView for gPLINK.
	 * @author Kathe Todd-Brown
	 *
	 */
	class myFolderView extends FolderView{

		public myFolderView(Record d) {
			super(GPLINK.this, d);
		}

		@Override
		protected PopUpMenu createPopUp(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			return new myPopUpMenu(mf, d, givenFiles, givenOp);
		}
		
	}
	
	/**
	 * The PopUpMenu for gPLINK.
	 * @author Kathe Todd-Brown
	 *
	 */
	class myPopUpMenu extends PopUpMenu{

		public myPopUpMenu(StartFrame mf, Record d, String[] givenFiles, String[] givenOp) {
			super(mf, d, givenFiles, givenOp, Project.GPLINK_EXT);
			
			if(GPLINK.this.data.getHaploPath() != null &&
					!GPLINK.this.data.getHaploPath().matches("^\\s*$") &&
					fileNames != null){
				JMenuItem hapOpen = new JMenuItem("Open in Haploview");
				hapOpen.addActionListener(openHaploview());
				
				this.insert(hapOpen, 1);
			}
		}

		@Override
		protected ActionListener createExecute() {
			return new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					// TODO If the operation is a queue, load it into a CreateQueue dialog;
					new PLINK_CreateQueue(GPLINK.this);
					// if it's a one-command calculation, load a PLINK_Execute.
					new PLINK_Execute(GPLINK.this);//, data.getOp(opnames[0]).getCline());
				}
			};
		}
		
		private ActionListener openHaploview(){
			return new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(fileNames != null){
						if(opnames != null)
							new OpenHapDialog(GPLINK.this, opnames[0], fileNames[0]);
						else
							new OpenHapDialog(GPLINK.this, null, fileNames[0]);
					}
				}
			};
		}
		
	}
}
