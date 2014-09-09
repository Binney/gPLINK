package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;

/**
 * Create a Dialog to open a new PLINK project.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class PLINK_Open extends JDialog {
	/**
	 * A logger for this class
	 */
	private Logger logger = Logger.getLogger(PLINK_Open.class);
	/**
	 * The JFrame of the program.
	 */
	private GPLINK parent;
	/**
	 * Our file choosing class for the local
	 * directory.
	 */
	private GCFileChooser localChooser;
	
	private JButton remoteBrowse;
	/**
	 * Our file choosing class for the remote
	 * directory.
	 */
	private GCFileChooser remoteChooser;
	/**
	 * Let the user look at the local project
	 * directory.
	 */
	private JTextField localProject;
	/**
	 * Let the user look at the remote project
	 * directory
	 */
	private JTextField remoteProject;
	/**
	 * A JTextField that keeps the host name.
	 */
	private JTextField host;
	/**
	 * A JTextField that keeps the user name.
	 */
	private JTextField user;
	/**
	 * A JTextField that keeps the port number 
	 * as a string.
	 */
	private JTextField port;
	/**
	 * A SshClient that is used to
	 * open the remote browser.
	 */
	private SshClient conn;
	/**
	 * Let the user flag this project as remote.
	 */
	private JCheckBox remoteFlag;
	
	private JButton ok;
	
	/**
	 * A panel containing all the fields pertinate to
	 * the remote directory.
	 */
	private JPanel remotePanel;
	
	/**
	 * A FileFilter that will look for directories 
	 * 
	 * @author Kathe Todd-Brown
	 *
	 */
	private class gCLINEFilter extends FileFilter{
		/**
		 * Checks to see if the file is valid.
		 * @return true if the file ends in the
		 * is a directory, false
		 * otherwise
		 */
		
		public boolean accept(File pathname) {
			if(pathname.isDirectory())
				return true;
			else
				return false;
		}
		/**
		 * Grab a valid description of this filter.
		 * @return a valid description of the filter,
		 * "directory"
		 */
		@Override
		public String getDescription() {
			return "directory";
		}
		
	}
	
	/**
	 * Enable the ok and remote browse buttons
	 * as appropreate.
	 *
	 */
	private void checkButtons(){
		//set everything to false
		ok.setEnabled(false);
		remoteBrowse.setEnabled(false);
		remoteFlag.setEnabled(false);
		//if there is a local project that isn't blank
		if(!localProject.getText().matches("^\\s*$")){
			remoteFlag.setEnabled(true);
			//if the remote flag is selected
			if(remoteFlag.isSelected()){
				//if the connection is set
				if(conn != null){
					//set the browse to true
					remoteBrowse.setEnabled(true);
					//if the remote project is not blank
					if(!remoteProject.getText().matches("^\\s*$"))
						//set the ok button to show
						ok.setEnabled(true);
				}
			}else
				//set the ok button to show
				ok.setEnabled(true);
		}
	}
	
	/**
	 * Create the panel that holds the local project
	 * field and associated button.
	 * @return a JPanel that holds the local project
	 * components.
	 */
	private JPanel createLocal(){
		JPanel ans = new JPanel();
		//initalzie the local project field
		localProject = new JTextField(20);
		
		//create a chooser for the browse button
		localChooser = new GCFileChooser(parent, new gCLINEFilter(), 
				true, true,	null, "");
		//put the string from the chooser in the localProject field
		localChooser.addFileChoosenListener(new FileChoosenListener(){
			public void fileChoosenOccures(FileChoosenEvent evt) {
				logger.info("(createLocal()) adding the localProject");
				localProject.setText(localChooser.fileName);
			}
		});
		
		//create the browse button
		JButton browse = new JButton("Browse");
		//link up the local browse button with the chooser
		browse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				localChooser.showChooser();
			}
		});
		
		//add the components to the panel
		ans.add(localProject);
		ans.add(browse);
		return ans;
	}
	
	/**
	 * Create a panel to hold the remote flag. We don't
	 * put this in the remote panel because that panel's
	 * visiblity is tied to weather or not the flag is
	 * selected.
	 * @return Return a Panel that holds the remote flag.
	 */
	private JPanel createRemoteFlag(){
		JPanel ans = new JPanel();
		//initalize check box
		remoteFlag = new JCheckBox("SSH link to remote project (Linux/Unix host)");
		//show the lower half of the open dialog if the 
		//remote option is flagged
		remoteFlag.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				checkButtons();
				//check the visibility of the rest of the
				//...remote panel
				remotePanel.setVisible(remoteFlag.isSelected());
				//repack the dialog
				PLINK_Open.this.pack();
			}
		});
		remoteFlag.setEnabled(false);
		//add this to the panel
		ans.add(remoteFlag);
		return ans;
	}
	/**
	 * Create a panel that holds the connection information.
	 * The connection information includes: host name,
	 * user name and port number.
	 * @return A JPanel that contains the connection information.
	 */
	private JPanel createConnection(){
		//create a seperate panel for formating isses
		JPanel ans = new JPanel();
		//initalize the JTextFields
		user =  new JTextField(10);
		host = new JTextField(30);
		port = new JTextField("22",5);
		//create the login button
		JButton login = new JButton("Login");
		//if the login button is clicked then establish the
		//...connection
		login.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				conn = Project.connect(new ErrorManager(parent),
						parent,
						host.getText(),
						user.getText(), 
						new Integer(port.getText()));
				//check to see if the buttons are now valid
				checkButtons();
			}
		});
		
		//lay everything out in the panel
		ans.add(new JLabel("user: "));
		ans.add(user);
		ans.add(new JLabel("host: "));
		ans.add(host);
		ans.add(new JLabel("port: "));
		ans.add(port);
		ans.add(login);
		return ans;
	}
	
	/**
	 * Create a panel that holds the remote project name.
	 * @return A JPanel that contains the remote project 
	 * directory.
	 */
	private JPanel createRemote(){
		JPanel ans = new JPanel();
		//initalize the remote project field
		remoteProject = new JTextField(20);
		
		//create the browse button
		remoteBrowse = new JButton("Browse");
		//create a remote chooser and show it when the browse is
		//...clicked
		remoteBrowse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//create a chooser for the browse button
				remoteChooser = new GCFileChooser(parent, null, 
					false, true, conn, "");
				//set the text for the remoteProject based on the file selected
				remoteChooser.addFileChoosenListener(new FileChoosenListener(){
					public void fileChoosenOccures(FileChoosenEvent evt) {
						remoteProject.setText(remoteChooser.fileName);
					}
				});
				//set the chooser as visible
				remoteChooser.showChooser();
			}
		});
		
		//default the enabled to false, this is switched
		//...in checkButtons()
		remoteBrowse.setEnabled(false);
		
		//lay everything else
		ans.add(new JLabel("Remote Project:"));
		ans.add(remoteProject);
		ans.add(remoteBrowse);
		return ans;
	}
	/**
	 * Create a panel to hold the butttons. These
	 * buttons include: ok and cancel.
	 * @return A JPanel that contains the buttons.
	 */
	private JPanel createButton(){
		logger.info("(createButton()) Create the panel");
		JPanel ans = new JPanel();
		//initalize the ok button and tie 
		//...it to creating a new project
		ok = new JButton("OK");
		//tell the ok button what to do
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("[createButton()] The ok button is activated");
				//if the data isn't null
				if(parent.data != null){
					//close the old project
					parent.data.properClosing();
					//set everything to null just incase
					parent.data = null;
				}
				//otherwise check if it's a remote
				//...project via the flag
				if(remoteFlag.isSelected()){
					//reset the data to a new remote project
					parent.data = new Project(parent, 
							localProject.getText(), 
							remoteProject.getText(),
							host.getText(),
							user.getText(),
							port.getText(),
							conn);
					
				}else{
					//reset the data to a new local project
					parent.data = new Project(parent, 
							localProject.getText());
					
				}
				//redo the panels based on this new project
				parent.layoutPanels(parent.data);
				//close up this dialog
				PLINK_Open.this.dispose();
			}
		});
		//intialzie the cancel button and 
		//...set it to clear any data laying around
		//...the frame.
		JButton cancel = new JButton("Cancel");
		//when cancel is clicked
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//close up the old project
				if(parent.data != null)
					parent.data.properClosing();
				//set it to a blank project
				parent.data = new Project(parent);
				//lay things out again
				parent.layoutPanels(parent.data);
				//close out the dialog
				PLINK_Open.this.dispose();
			}
		});
		
		//add it to the panel
		ans.add(ok);
		ans.add(cancel);
		
		return ans;
	}
	
	/**
	 * Create a dialog that will open up a new 
	 * project and establish an ssh connection.
	 * @param p A StartFrame that the dialog is 
	 * attached to.
	 */
	public PLINK_Open(GPLINK p){
		super(p, "Open project");
		logger.info("PLINK_Open(GPLINK) Starting the constructor");
		parent = p;
		
		//create the remote panel
		remotePanel = new JPanel();
		remotePanel.setLayout(new BoxLayout(remotePanel, BoxLayout.PAGE_AXIS));
		//remotePanel.setBorder(new LineBorder(Color.BLACK));
		remotePanel.add(createConnection());
		remotePanel.add(createRemote());
		remotePanel.setVisible(false);
		
		//set the layout for the dialog
		this.getRootPane().setLayout(new BoxLayout(getRootPane(), BoxLayout.PAGE_AXIS));
		//add the panels
		this.getRootPane().add(createLocal());
		this.getRootPane().add(createRemoteFlag());
		this.getRootPane().add(new JSeparator());
		this.getRootPane().add(remotePanel);
		this.getRootPane().add(createButton());
		checkButtons();
		//add document listeners to check for changes
		//in the remoteProject field
		remoteProject.getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				checkButtons();
			}
			public void removeUpdate(DocumentEvent e) {
				checkButtons();
			}
		});
		//add a document listener to check for changes
		//in the localProject field
		localProject.getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				checkButtons();
			}
			public void removeUpdate(DocumentEvent e) {
				checkButtons();
			}	
		});
		
		//pack everything up and set it as visible
		pack();
		setVisible(true);
	}
	/**
	 * Create a dialog that will open a new Project.
	 * @param f The main window (GPLINK) this dialog is attached to and
	 * where it assigns the new Project.
	 * @param local A String that holds the name of the local folder
	 * we wish to use as a default.
	 */
	public PLINK_Open(GPLINK f, String local){
		this(f);
		//set the defaults
		localProject.setText(local);
	}
	/**
	 * Create a dialog that will open a new Project.
	 * @param f The main window (GPLINK) this dialog is attached to and
	 * where it assigns the new Project.
	 * @param local A String that is the default local folder.
	 * @param givenRemote A String that is the default remote folder.
	 * @param givenHost A String that is the default host.
	 * @param givenUser A String that is the default user.
	 * @param givenPort A String that is the default port.
	 */
	public PLINK_Open(GPLINK f, String local,
			String givenRemote, String givenHost, 
			String givenUser, String givenPort){
		this(f);
		//set the defaults
		localProject.setText(local);
		remoteFlag.setSelected(true);
		remoteProject.setText(givenRemote);
		host.setText(givenHost);
		user.setText(givenUser);
		port.setText(givenPort);
	}

}
