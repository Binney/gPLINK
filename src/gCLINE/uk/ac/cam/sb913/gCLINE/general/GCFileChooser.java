package gCLINE.uk.ac.cam.sb913.gCLINE.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EventObject;
import java.util.EventListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.sshtools.j2ssh.SshClient;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;

/**
 * This class launches dailogs that allow the user
 * to pick files on either a remote or local computer.
 * This file name selection then launches an event (defined as a 
 * subclass) that can then be passed to the appropreate
 * object.
 * @author Kathe Todd-Brown
 *
 */
public class GCFileChooser{
	/**
	 * Log comments and errors
	 */
	private static Logger logger = Logger.getLogger(GCFileChooser.class);
	/**
	 * Attached the dialogs to our main window and
	 * access the record through
	 */
	private JFrame parent;
	
	private JDialog parent2;
	/**
	 * The file name that is selected
	 */
	public String fileName;
	/**
	 * A dialog that alows the user to select
	 * a remote file.
	 */
	private JDialog remotePicker;
	/**
	 * A dialog that allows hte user to select
	 * a local file.
	 */
	private JFileChooser localPicker;
	/**
	 * Keep a ArrayList of the listeners
	 */
	private ArrayList <FileChoosenListener> allListeners;
	/**
	 * Flag file we are looking for as local or remote.
	 */
	private boolean onlyLocal;
	
	private boolean onlyDir;
	
	private SshClient conn;
	
	private String currentDir;
	
	private JList fileList;
	
	private JTextField remotePicks;
	
	private boolean dirSelected = false;
	private DefaultListModel editableList;
	
	private void initalize(FileFilter f){
		//set up a ArrayList of the listeners
		allListeners = new ArrayList <FileChoosenListener>();
		//if the record is remote and we are flaged as remote
		if(onlyLocal == false){
			if(currentDir.endsWith("/"))
				currentDir = currentDir.substring(0, currentDir.length() -1);
			logger.info("[intalize(FileFilter)]onlyLocal is FALSE");
			remotePicks = new JTextField(30);
			remotePicks.setText(currentDir);
			//set up the remote file picker
			if(parent != null)
				remotePicker = new JDialog(parent, "Open Remote File");
			else
				remotePicker = new JDialog(parent2, "Open Remote File");
			
			remotePicker.getRootPane().setLayout(new BoxLayout(remotePicker.getRootPane(), 
					BoxLayout.PAGE_AXIS));
			remotePicker.getRootPane().add(createFileList());
			remotePicker.getRootPane().add(createButton());
		}else{
			logger.info("[initalize(FileFilter)] onlyLocal is TRUE");
			//set up the local file picker
			localPicker = new JFileChooser();
			
			localPicker.setCurrentDirectory(new File(currentDir));
			if(f != null)
				localPicker.setFileFilter(f);
			if(onlyDir)
				// only look at directories
				localPicker.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
	}
	
	/**
	 * Create an instance of our custom file chooser.
	 * @param p JFrame that we attach the file picking
	 * dialogs too and access the Record
	 * @param f FileFilter that filters the files.
	 * @param pickLocal A boolean that flags if the
	 * chooser is local or remote.
	 */
	public GCFileChooser(JFrame p, FileFilter f, 
			boolean pickLocal, boolean pickDirectory,
			SshClient c, String startDir){
		currentDir = startDir;
		conn = c;
		//set the flag
		onlyLocal = pickLocal;
		
		onlyDir = pickDirectory;
		//set the frame
		parent = p;
		initalize(f);
	}
	/**
	 * Create an instance of our custom file chooser.
	 * @param p JDialog that we attach the file picking
	 * dialogs too and access the Record
	 * @param f FileFilter that filters the files.
	 * @param pickLocal A boolean that flags if the
	 * chooser is local or remote.
	 * @param pickDirectory A boolean that flags if
	 * directory are valid selections
	 * @param c The SshClient that used to connect to a remote
	 * server
	 * @param startDir A String that is the defines the directory
	 * to start looking for files in.
	 */
	public GCFileChooser(JDialog p, FileFilter f, 
			boolean pickLocal, boolean pickDirectory,
			SshClient c, String startDir){
		currentDir = startDir;
		conn = c;
		//set the flag
		onlyLocal = pickLocal;
		
		onlyDir = pickDirectory;
		//set the frame
		parent2 = p;
		initalize(f);
	}
	
	/**
	 * Show the file chooser.
	 */
	public void showChooser(){
		if(onlyLocal == false){
			remotePicker.pack();
			remotePicker.setVisible(true);
		} else{
			int returnVal;
			if(parent != null)
				returnVal = localPicker.showOpenDialog(parent);
			else
				returnVal = localPicker.showOpenDialog(parent2);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				fileName = localPicker.getSelectedFile().getAbsolutePath();
				logger.info("(showChooser()) Picked filename: "+fileName);
				FileChoosenEvent openEvent = 
					new FileChoosenEvent(GCFileChooser.this);
				for(FileChoosenListener f : allListeners){
					f.fileChoosenOccures(openEvent);
				}
			} 
		}
			
	}
	
	private void createContent(DefaultListModel editableList){
		editableList.removeAllElements();
		
		if(conn == null)
			return;
		
		RunCommand getFiles = new RunCommand("ls -FL " + currentDir, conn);
		//don't actually run this as a thread since we want
		getFiles.run();
		ArrayList <String> files = getFiles.outputLines;
		for(String file:files){
			editableList.addElement(file);
		}
	}
	
	private JScrollPane createFileList(){
		
		editableList = new DefaultListModel();
		createContent(editableList);
		fileList = new JList(editableList);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL);
		fileList.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
//				if it's a double click
				if(e.getClickCount() == 2
						&& dirSelected){
					currentDir = remotePicks.getText();
					createContent(editableList);
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		fileList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				String name = (String) fileList.getSelectedValue();
				if(name == null)
					return;
				if(name.endsWith("/"))
					dirSelected = true;
				if(name.endsWith("/")
						|| name.endsWith("@")
						|| name.endsWith("*")
						|| name.endsWith("=")
						|| name.endsWith("%")
						|| name.endsWith("|")){
					name = name.substring(0, name.length()-1);
				}
				if(currentDir.equals("")){
					remotePicks.setText(name);
				}
				else{
					if(currentDir.endsWith("/"))
						remotePicks.setText(currentDir + name);
					else
						remotePicks.setText(currentDir + "/" + name); 
				}
			}});
		fileList.ensureIndexIsVisible(0);
		//ans.add(fileList);
		return new JScrollPane(fileList);
	}
	/**
	 * Create a button panel for the remote dialog.
	 * @return JPanel that holds the buttons for the 
	 * remote panel.
	 */
	private JPanel createButton(){
		JPanel ans = new JPanel();
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
//				check to make sure that there is a parent directory
				if(currentDir.equals("/"))
					return;
				logger.info("[back.actionListener] current dir: " + currentDir);
				//cut off the last file in the directory name
				String newDir = currentDir.replaceFirst("[^/]+$", "");
				logger.info("[back.actionListener] newDir: " + newDir);
				
				//if this didn't do anything
				if(newDir.equals(currentDir)){
					//get the "real" full name
					RunCommand getFiles = new RunCommand("pwd " + currentDir, conn);
					//don't actually run this as a thread since we want
					//to wait for the results
					getFiles.run();
					ArrayList <String> fullName = getFiles.outputLines;
					newDir = fullName.get(0);
					logger.info("[back.actionListener] full name: " + newDir);
					//and run it again
					newDir = newDir.replaceFirst("/[^/]+$", "");
					logger.info("[back.actionListener] newDir: " + newDir);
					
				}
				//double check that we haven't cut off the home dir
				//if(newDir.equals(""))
				//	newDir = "/";
				
				//reset the directory
				currentDir = newDir;
				//show this to the field
				remotePicks.setText(currentDir);
				//remake the list
				createContent(editableList);
			}
		});
		ans.add(back);
		//add the text field where we can view
		//our selection
		ans.add(remotePicks);
		
		JButton ok = new JButton("Open");
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if((dirSelected && onlyDir)
						|| (!dirSelected || !onlyDir)){
					fileName = remotePicks.getText();
					FileChoosenEvent openEvent = new FileChoosenEvent(GCFileChooser.this);
					for(FileChoosenListener f : allListeners){
						f.fileChoosenOccures(openEvent);
					}
					remotePicker.dispose();
				}else if(dirSelected){
					currentDir = remotePicks.getText();
					createContent(editableList);
				}
			}
		});
		
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				remotePicker.dispose();
			}
		});
		ans.add(ok);
		ans.add(cancel);
		return ans;
	}
	
	
	/**
	 * Create an EventListener for our internally defined event.
	 * @author Kathe Todd-Brown
	 *
	 */
	public interface FileChoosenListener extends EventListener{
		public void fileChoosenOccures(FileChoosenEvent evt);
	}
	
	/**
	 * Create an EventObject to notify the selection
	 * of a file.
	 * @author Kathe Todd-Brown
	 *
	 */
	@SuppressWarnings("serial")
	public class FileChoosenEvent extends EventObject{

		public FileChoosenEvent(Object source) {
			super(source);
		}
	}
	/**
	 * Add a listener to the list.
	 * @param listener FileChooserListener that we want to 
	 * add to the list
	 */
	public void addFileChoosenListener(FileChoosenListener listener){
		allListeners.add(listener);
	}
	/**
	 * Remove a listener from the list.
	 * @param listener FileChooserListner that we want to remove
	 * from the list. If it's not in the list then we don't
	 * do anytking.
	 */
	public void removeFileChoosenListener(FileChoosenListener listener){
		allListeners.remove(listener);
	}
	
}
