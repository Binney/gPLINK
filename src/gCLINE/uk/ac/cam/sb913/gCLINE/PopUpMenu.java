package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.RunCommand;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;

/**
 * An abstract class that lays out the general popup 
 * menu that appears in both the operations
 * and folder viewers.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public abstract class PopUpMenu extends JPopupMenu {
	/**
	 * Define a static logger variable so that it references the
	 */
	private static Logger logger = Logger.getLogger(PopUpMenu.class);
	
	/**
	 * An array of the full file names of selected local files
	 */
	protected String [] localFilenames;
	/**
	 * An array of the full file names of selected remote files
	 */
	protected String [] remoteFilenames;
	protected String [] fileNames;
	/**
	 * An array of the operation names
	 */
	protected String [] opnames;
	/**
	 * A StartFrame this menu is attached to
	 */
	private StartFrame frame;
	/**
	 * The Record this menu is attached to
	 */
	protected Record data;
	/**
	 * The String is the file extention that holds the exit status
	 */
	private String flagExt;
	/**
	 * Download selected files
	 */
	private ActionListener download = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			logger.info("[download] find the null! frame: " + (frame != null) 
												+ ", data: " + (data != null)
												+ ", localFolder: " + data.getLocalFolder()
												+ ", first remoteFile: " + remoteFilenames[0]);
			new Thread(frame.new Download(data, 
					false, data.getLocalFolder(), remoteFilenames)).start();
		}
	};
	/**
	 * Launch the user specified file viewer.
	 */
	private ActionListener openAlt = new ActionListener(){
		
		public void actionPerformed(ActionEvent event){
			//go through each localfile
			for(int i =0; i <localFilenames.length; i++){
				new Thread(new RunCommand(data.getAltEditor() + " " + 
						FileInfo.quote(localFilenames[i]),
						data, true)).start();
			}
		}
	};
	
	/**
	 * Launch the default file viewer
	 */
	private ActionListener openDefault = new ActionListener(){
		
		public void actionPerformed(ActionEvent event){
			//go through each local file
			for(int i = 0; i < localFilenames.length; i++){
				new Thread(new RunCommand(Record.DEFAULT_EDITOR 
						+ " " + FileInfo.quote(localFilenames[i]),
						data, true)).start();
			}
		}
	};
	
	private ActionListener showHierachyOp = new ActionListener(){
		public void actionPerformed(ActionEvent arg0){
			new HierarchyDialog(opnames[0],
					frame, data, opnames[0], null);
		}
	};
	
	private ActionListener showHierachyFile = new ActionListener(){
		public void actionPerformed(ActionEvent arg0){
			if(data.isRemote())
				new HierarchyDialog(FileInfo.fileName(remoteFilenames[0]),
					frame, data, null, remoteFilenames[0]);
			else
				new HierarchyDialog(FileInfo.fileName(localFilenames[0]),
						frame, data, null, localFilenames[0]);
		
		}
	};
	
	private ActionListener editOp = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			new EditOpDialog(opnames[0], frame, data);
		}
	};
	
	private ActionListener editFile = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			new EditFolderDialog(frame, data, fileNames);
		}
	};
	
	private ActionListener deleteOp = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			new DeleteOpDialog(frame, data, opnames, flagExt);
		}
	};
	
	private ActionListener addFileToOp = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			new LinkNewFileDialog(frame, data, opnames[0]);
		}
	};
	
	private ActionListener removeFileFromOp = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
//			Double check that you want to unlink
			int choice = JOptionPane.showConfirmDialog(frame,
				    "Are you sure you want to unlink " 
					+ fileNames[0] + " from " + opnames[0]+ "?",
				    "GPLINK confirmation",
				    JOptionPane.YES_NO_OPTION);
			if(choice != JOptionPane.YES_OPTION)
				return;
			
			//data.getOp(opnames[0]).removeFile(fileNames[0]); TODO
			
		}
	};
	
	protected abstract ActionListener createExecute();
	
	public PopUpMenu(StartFrame mf, Record d,
			String [] givenFiles, String [] givenOp,
			String flagExitStatus){
		super();
		opnames = givenOp;		
		frame = mf;
		data = d;
		flagExt = flagExitStatus;
		boolean openFile = false;
		
		
		
		//process the given file names
		if(givenFiles != null 
				&& givenFiles.length != 0){
			
			if(data.isRemote()){
				remoteFilenames = new String[givenFiles.length];
			}
			fileNames = new String[givenFiles.length];
			localFilenames = new String[givenFiles.length];
			for(int i = 0; i < givenFiles.length; i++){
				fileNames[i] = FileInfo.fileName(givenFiles[i]);
				localFilenames[i] = new File(data.getLocalFolder(),
						fileNames[i]).getAbsolutePath();
				if(data.isRemote())
					remoteFilenames[i] = data.getRemoteFolder() 
						+ fileNames[i];
			}
			openFile = true;
		
			if(openFile == true){
				JMenuItem getFile = new JMenuItem("Download file");
				getFile.addActionListener(download);
		
				//check that the file isn't already there
				boolean localCopy = true;
				for(int i = 0; i < localFilenames.length; i++){
					if( ! new File(localFilenames[i]).exists())
						localCopy = false;
				}
				
				if(data.isRemote()){
					//add download to the menu
					add(getFile);
					add(new JPopupMenu.Separator());
				}
				
				if(localCopy == true){
					JMenuItem open = new JMenuItem("Open in default viewer");
					open.addActionListener(openDefault);
					add(open);
					String altEdit = data.getAltEditor();
					if(altEdit != null
							&&!altEdit.matches("^\\s*$")){
						JMenuItem openAlter = new JMenuItem("Open in alternate viewer");
						openAlter.addActionListener(openAlt);
						add(openAlter);
					}
					add(new JPopupMenu.Separator());
				}
				
			}
			
			JMenuItem fileParents;
			if(data.isRemote())
				fileParents = new JMenuItem("Show relations of "+FileInfo.fileName(remoteFilenames[0]));
			else
				fileParents = new JMenuItem("Show relations of "+FileInfo.fileName(localFilenames[0]));
			fileParents.addActionListener(showHierachyFile);
			
			add(fileParents);
				
		}
		
		
		if(opnames != null){
			JMenuItem opParents = new JMenuItem("Show relations of " + opnames[0]);
			opParents.addActionListener(showHierachyOp);
			add(opParents);
		}
		add(new JPopupMenu.Separator());
		
		if(givenOp != null){
			
			if(fileNames != null){
				JMenuItem unlink = new JMenuItem("Unlink " + fileNames[0] + " from " + opnames[0]);
				unlink.addActionListener(removeFileFromOp);
				add(unlink);
			}
			
			JMenuItem opAdd = new JMenuItem("Link a file to " +opnames[0]);
			opAdd.addActionListener(addFileToOp);
			add(opAdd);
			
			add(new JPopupMenu.Separator());
		
			JMenuItem opCreate = new JMenuItem("Create new operation");
			opCreate.addActionListener(createExecute());
			add(opCreate);
			
			add(new JPopupMenu.Separator());
		}
		
		if(givenFiles != null && givenOp == null){
			JMenuItem fileEdit = new JMenuItem("Edit");
			fileEdit.addActionListener(editFile);
			add(fileEdit);
		}
		
		if(givenOp != null){
			JMenuItem opEdit = new JMenuItem("Edit");
			opEdit.addActionListener(editOp);	
			add(opEdit);
			
			JMenuItem opDelete = new JMenuItem("Delete operation");
			opDelete.addActionListener(deleteOp);
			add(opDelete);
		}
	}
}
