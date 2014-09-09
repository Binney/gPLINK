package gCLINE.uk.ac.cam.sb913.gCLINE.tester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.general.ErrorManager;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;
import gCLINE.uk.ac.cam.sb913.gCLINE.tester.TestFrame.myProject;

/**
 * A dialog that opens a new project.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public final class Open extends JDialog {
	/**
	 * A logger for this clas
	 */
	private Logger logger = Logger.getLogger(Open.class);
	/**
	 * The main frame of the program.
	 */
	protected TestFrame parent;
	/**
	 * Our file choosing class for the local
	 * directory.
	 */
	private GCFileChooser localChooser;
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
	private JTextField host;
	private JTextField user;
	private JTextField port;
	private SshClient conn;
	private TestFrame.myProject data;
	/**
	 * Let the user flag this project as remote.
	 */
	private JCheckBox remoteFlag;
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
		localChooser.addFileChoosenListener(new FileChoosenListener(){
			public void fileChoosenOccures(FileChoosenEvent evt) {
				logger.info("(createLocal()) adding the localProject");
				localProject.setText(localChooser.fileName);
			}
		});
		
		//create the browse button
		JButton browse = new JButton("Browse");
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
		remoteFlag = new JCheckBox("Project is remote.");
		remoteFlag.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				//check the visibility of the rest of the
				//...remote panel
				remotePanel.setVisible(remoteFlag.isSelected());
				//repack the dialog
				Open.this.pack();
			}
		});
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
		JPanel ans = new JPanel();
		user =  new JTextField(10);
		host = new JTextField(30);
		port = new JTextField(5);
		
		JButton login = new JButton("Login");
		login.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
				conn = myProject.connect(new ErrorManager(parent),
						parent,
						host.getText(),
						user.getText(), 
						new Integer(port.getText()));
			}
		});
		
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
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//create a chooser for the browse button
				remoteChooser = new GCFileChooser(parent, null, 
					false, true, conn, "");
				remoteChooser.addFileChoosenListener(new FileChoosenListener(){
					public void fileChoosenOccures(FileChoosenEvent evt) {
						logger.info("(createLocal()) adding the localProject");
						remoteProject.setText(remoteChooser.fileName);
					}
				});
				remoteChooser.showChooser();
			}
		});
		
		ans.add(new JLabel("Remote Project:"));
		ans.add(remoteProject);
		ans.add(browse);
		return ans;
	}
	/**
	 * Create a panel to hold the butttons. These
	 * buttons include: ok and cancel.
	 * @return A JPanel that contains the buttons.
	 */
	private JPanel createButton(){
		JPanel ans = new JPanel();
		//initalize the ok button and tie 
		//...it to creating a new project
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//close the old project
				parent.data.properClosing();
				if(remoteFlag.isSelected()){
					parent.data = null;
					data = parent.new myProject(parent, localProject.getText(), 
							remoteProject.getText(),
							host.getText(),
							user.getText(),
							port.getText(),
							conn);
					//if(data.isNew)
						//new StartFrame.StartFrameConfig(data);
				}
				else{
					parent.data = null;
					String localFolder = localProject.getText();
					if(!(new File(localFolder).isDirectory()))
						localFolder = new File(localFolder).getParent();
					data = parent.new myProject(parent, 
							localFolder);
					//if(data.isNew)
					//new StartFrame.StartFrameConfig(data);
				}
				parent.layoutPanels(data);
				Open.this.dispose();
			}
		});
		//intialzie the cancel button and 
		//...set it to clear any data laying around
		//...the frame.
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(data != null)
					data.properClosing();
				data = parent.new myProject(parent);
				parent.layoutPanels(data);
				Open.this.dispose();
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
	public Open(TestFrame p){
		super(p, "Open project");
		parent = p;
		
		//create the remote panel
		remotePanel = new JPanel();
		remotePanel.setLayout(new BoxLayout(remotePanel, BoxLayout.PAGE_AXIS));
		remotePanel.add(createConnection());
		remotePanel.add(createRemote());
		remotePanel.setVisible(false);
		
		//set the layout for the dialog
		this.getRootPane().setLayout(new BoxLayout(getRootPane(), BoxLayout.PAGE_AXIS));
		//add the panels
		this.getRootPane().add(createLocal());
		this.getRootPane().add(createRemoteFlag());
		this.getRootPane().add(remotePanel);
		this.getRootPane().add(createButton());
		
		//pack everything up and set it as visible
		pack();
		setVisible(true);
	}
	
	public Open(TestFrame f, String local){
		this(f);
		localProject.setText(local);
	}
}
