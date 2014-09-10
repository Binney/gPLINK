package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;

/**
 * Dialog to delete a given operation and the files associated with it.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public class DeleteOpDialog extends JDialog {
	/**
	 * A JCheckBox array that holds the 
	 * deletion options for the selected operations.
	 */
	private JCheckBox [] operations;
	/**
	 * A JCheckBox array that holds the 
	 * deletion options for the selected output files.
	 */
	private ArrayList <JCheckBox> outfiles;
	/**
	 * A JCheckBox array that holds the 
	 * deletion options for the selected input files.
	 */
	private ArrayList <JCheckBox> infiles;
	/**
	 * The Record that the operations are attached to.
	 */
	private Record data;
	/**
	 * An ActionListener that deletes the files and operations
	 * that are selected in the check boxes.
	 */
	ActionListener deleteFileOp = new ActionListener(){
		public void actionPerformed(ActionEvent event){
			File temp;
			if(data.isRemote() == false){
				//delete the output files
				for(JCheckBox tempBox: outfiles){
					if(tempBox != null && tempBox.isSelected()){
						temp = new File(data.getLocalFolder(), tempBox.getText());
						if(temp.delete()==false){
							data.frame.messenger.createError("Failed deletion of ["+
									tempBox.getText() + 
									"] You will need to manually check the" +
									"deletion of " +
									"this file.","deleteFileOp@DeleteOpDialog.java");
						}
					}
					
				}
				//delete the input files
				for(JCheckBox tempBox:infiles){
					if(tempBox.isSelected()){
						temp = new File(data.getLocalFolder(), tempBox.getText());
						if(temp.delete()==false){
							data.frame.messenger.createError("Failed deletion of ["+
									tempBox.getText() + 
									"] You will need to manually check the" +
									"deletion of " +
									"this file.","deleteFileOp@DeleteOpDialog.java");
						}
					}
					
				}
			}
			//remote the operations
			for(int i = 0; i < operations.length; i++){
				if(operations[i].isSelected()){
					data.removeOperation(operations[i].getText());
				}
			}
			
			dispose();
		}};
	/**
	 * Create a Panel that has all the operation
	 * names with the check box to flag confirmation
	 * of deletion
	 * @param opNames A String array that is the operation
	 * names
	 * @return A JPanel that contains all the string operations.
	 */
	private JPanel createOpPanel(String [] opNames){
		JPanel ans = new JPanel();
		ans.setLayout(new BoxLayout(ans, BoxLayout.PAGE_AXIS));
		ans.setBorder(new TitledBorder("Operations to Delete"));
		
		int numOps = opNames.length;
		
		operations = new JCheckBox[numOps];
		
		//populate the operations names
		for(int i = 0; i < numOps; i ++){
			operations[i] = new JCheckBox(opNames[i]);
			operations[i].setSelected(true);
			ans.add(operations[i]);
		}
		
		return ans;
	}
	/**
	 * Find files 
	 * attached to all the operations and initialise 
	 * the appropriate panels.
	 * @param opNames A String array that holds all the operations names
	 * that we are interested in.
	 * @param inFiles A boolean that flags the input vs output files.
	 * @return A JPanel containing the files you wish to delete.
	 */
	private JPanel createFilePanel(String [] opNames, boolean inFiles, String flagExt){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, 
				BoxLayout.PAGE_AXIS));
		//put the right title in it
		if(inFiles)
			panel.setBorder(new TitledBorder("Input files to Delete"));
		else
			panel.setBorder(new TitledBorder("Output files to Delete"));
		
		//create a hash set of the files we are interested in
		//...this allows us to avoid double listing a file if it shows
		//...up in multiple operations
		HashSet <String> files = new HashSet <String>();
		
		ArrayList <FileInfo> temp = new ArrayList <FileInfo>();
		
		//populate the infiles
		for(int i = 0; i < opNames.length; i++){
			//get the right file set
			//if(inFiles)
				//temp = data.getOp(opNames[i]).getInputFiles();
			//else // TODO
				//temp = data.getOp(opNames[i]).getOutputFiles();
			//add the files to the hash set
			for(FileInfo f: temp)
				files.add(f.toString());

			// check to see if there is a finished extension
			if(!inFiles && 
					(new File(data.getLocalFolder(), opNames[i] + flagExt)).exists())
				files.add(opNames[i]+flagExt);
		}
		
		
		//process the file info to checkboxes
		if(inFiles)
			infiles = new ArrayList<JCheckBox>();
		else
			outfiles = new ArrayList <JCheckBox>();
		//go through all the files you have lined up
		for(String file: files){
			JCheckBox tempBox = new JCheckBox(file);
			if(inFiles)
				infiles.add(tempBox);
			else{
				outfiles.add(tempBox);
				tempBox.setSelected(true);
			}
			//add them to the panel
			panel.add(tempBox);
		}
		
		return panel;
	}
	/**
	 * Create a JPanel that holds the buttons for this
	 * dialog.
	 * @return a JPanel that holds the buttons for this
	 * dialog.
	 */
	private JPanel createButtonsPanel(){
		JPanel buttonPanel = new JPanel();
		
		//set up the ok button
		JButton ok = new JButton("Delete Selection");
		ok.addActionListener(deleteFileOp);
		buttonPanel.add(ok);
		
		//set up the cancel button
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DeleteOpDialog.this.dispose();
			}
		});
		buttonPanel.add(cancel);
		
		return buttonPanel;
		
	}
	/**
	 * Create a deletion dialog for a select number
	 * of operations.
	 * @param givenFrame A StartFrame that this dialog will
	 * be attached to and will provide the error manager.
	 * @param d A Record that contains these operations.
	 * @param opNames A String array is the operation
	 * names that we want to delete.
	 */
	public DeleteOpDialog(StartFrame givenFrame,
			Record d, String [] opNames, String flagExt){
		//initialise stuff
		super(givenFrame, "Delete Operations");
		data = d;
		//set up the dialog
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//getContentPane().setLayout(new GridLayout(4,1));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		//add the operation panel
		getContentPane().add(new JScrollPane(createOpPanel(opNames)));
		//add the file panels if we can delete files
		if(data.isRemote() == false){
			getContentPane().add(new JScrollPane(createFilePanel(opNames, true, flagExt)));
			getContentPane().add(new JScrollPane(createFilePanel(opNames, false, flagExt)));
		}
		//add the button panels
		getContentPane().add(createButtonsPanel());
		//show it off
		pack();
		setVisible(true);
	}
	
}
