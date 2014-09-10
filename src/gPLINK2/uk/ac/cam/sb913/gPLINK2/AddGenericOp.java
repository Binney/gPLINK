package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.OperationInfo;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;

/**
 * Create a dialog to import a non-PLINK operation. 
 * <p> Nothing in this import dialog is automated. The 
 * user must specify the 1) operations 2) input files 
 * 3) output files
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class AddGenericOp extends JDialog {
	/**
	 * The project that this dialog adds the operation to.
	 */
	private Project data;
	/**
	 * A ArrayList of strings that contain all the operation
	 * names
	 */
	private ArrayList <String> allOps;
	/**
	 * The JTextField that holds the operation name
	 */
	private JTextField opName;
	/**
	 * The JTextField that holds the operation description
	 */
	private JTextField opDesc;
	/**
	 * The JTextField that holds an input file. This field
	 * is not actually show to the user
	 */
	private JTextField tempIn;
	/**
	 * The JTextField that holds the new output file. This
	 * field is not actually shown to the user
	 */
	private JTextField tempOut;
	/**
	 * The JTextField that holds the commandline
	 */
	private JTextField cline;
	/**
	 * A list that holds all the current inputfiles
	 */
	private JList infiles;
	/**
	 * A DefaultListModel that holds the data for the
	 * input list.
	 */
	private DefaultListModel inlistmodel;
	/**
	 * A list that holds all the current output files
	 */
	private JList outfiles;
	/**
	 * A DefaultListModel that holds the data for the
	 * output list
	 */
	private DefaultListModel outlistmodel;
	/**
	 * pickAFileButton that browse to select the
	 * input files
	 */
	private BrowseButton addIn;
	/**
	 * pickAFileButton that browse to select the
	 * output files
	 */
	private BrowseButton addOut;
	/**
	 * A JButton that signals the form is complete
	 */
	private JButton ok;
	/**
	 * A JButton that signals the cancellation of
	 * the form.
	 */
	private JButton cancel;
	
	/**
	 * Listen to the temporary JTextField for input file
	 * to add to the operation.
	 */
	private DocumentListener processIn = new DocumentListener(){
		public void changedUpdate(DocumentEvent arg0) {}
		public void removeUpdate(DocumentEvent arg0) {}
		public void insertUpdate(DocumentEvent arg0) {
			inlistmodel.addElement(tempIn.getText());
		}
	};
	
	/**
	 * Listen to the temporary JTextField for the output
	 * file to add to the operation
	 */
	private DocumentListener processOut = new DocumentListener(){
		public void changedUpdate(DocumentEvent arg0) {}
		public void removeUpdate(DocumentEvent arg0) {}
		public void insertUpdate(DocumentEvent arg0) {
			outlistmodel.addElement(tempOut.getText());
		}
	};
	
	/**
	 * Once there is a unique operation name the ok button
	 * is enabled.
	 */
	private DocumentListener validateForm = new DocumentListener(){
		public void changedUpdate(DocumentEvent arg0) {}
		public void insertUpdate(DocumentEvent arg0) {
			ok.setEnabled(isvalid());
		}

		public void removeUpdate(DocumentEvent arg0) {
			ok.setEnabled(isvalid());
		}
		
	};
	/**
	 * Close everything up nicely
	 */
	private ActionListener closeForm = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			AddGenericOp.this.dispose();
		}
	};
	/**
	 * Add the operation to the project
	 */
	private ActionListener addOp = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			ArrayList <String []> inputfiles = new ArrayList <String []>();
			inlistmodel.trimToSize();
			int numInput = inlistmodel.getSize();
			for(int i = 0; i < numInput; i ++){
				inputfiles.add(new String[]{inlistmodel.get(i).toString(), ""});
			}
			
			ArrayList <String []> outputfiles = new ArrayList <String []>();
			outlistmodel.trimToSize();
			int numOutput = outlistmodel.getSize();
			for(int i = 0; i < numOutput; i ++){
				outputfiles.add(new String[]{outlistmodel.get(i).toString(), ""});
			}
			
			data.addCalculation(opName.getText(), opDesc.getText(),
					cline.getText(), null,
					inputfiles, outputfiles, null);
			
			// TODO ensure it's actually being executed!!

			//Assume that all imported operation are done and create
			//...a .gplink file if it's not already there.
			try {
				File gplinkfile = new  File(data.getLocalFolder(), opName.getText()+ Project.GPLINK_EXT);
				if(gplinkfile.createNewFile()){
					BufferedWriter out = new BufferedWriter(new FileWriter(gplinkfile));
				    out.write("0"); // TODO OH GOD WHY WHY WOULD YOU DO THAT SERIOUSLY
				    out.close();
				   
				    if( data.isRemote()){
				    	data.frame.new Upload(data, false, data.getRemoteFolder(), new File[] {gplinkfile});
					}
				    
				}
			} catch (IOException e2) {
				data.frame.messenger.createError("Error trying to create the .gplink file " +
						"for imported operation.","process:ImportOpDialog()");
			}
			
			AddGenericOp.this.dispose();
		}
	};
	
	/**
	 * Check to see if the form is valid
	 * @return a boolean reflecting the validity
	 */
	private boolean isvalid(){
		//check to make sure the operation doesn't
		//...already exist
		String newOp = opName.getText();
		for(String opName: allOps){
			if(opName.equals(newOp))
				return false;
		}
		//make sure the name format is valid
		return newOp.matches("^[\\w\\-]+$");
	}
	/**
	 * Layout the form
	 */
	private void buildLayout(){
		JPanel buttonPane = new JPanel();
		buttonPane.add(ok);
		buttonPane.add(cancel);
		
		JPanel root  = (JPanel)this.getContentPane();
		
		root.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		root.add(new JLabel("Operation name:"), c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		root.add(opName, c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 1;
		root.add(new JLabel("Operation description (optional):"), c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 2;
		root.add(opDesc, c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 2;
		root.add(new JLabel("Command line (optional):"), c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 2;
		root.add(cline, c);
		c.gridy ++;
		c.gridwidth = 1;
		root.add(new JLabel("Input files:"), c);
		c.gridy ++;
		c.gridwidth = 2;
		root.add(new JScrollPane(infiles), c);
		c.gridy ++;
		c.gridwidth = 1;
		c.gridx = 1;
		root.add(addIn, c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 1;
		root.add(new JLabel("Output files:"), c);
		c.gridy ++;
		c.gridwidth = 2;
		root.add(new JScrollPane(outfiles), c);
		c.gridy ++;
		c.gridwidth = 1;
		c.gridx = 1;
		root.add(addOut, c);
		c.gridx = 0;
		c.gridy ++;
		c.gridwidth = 2;
		root.add(buttonPane, c);
	}
	/**
	 * Create a dialog to add an operation that is not
	 * automatically processed by our plink import.
	 * @param mf MainFrame that the dialog is attached to
	 */
	public AddGenericOp(GPLINK mf){
		super(mf, "Import a Non-PLINK operation");
		data = mf.data;
		
		allOps = new ArrayList<String> ();
		ArrayList <OperationInfo> temp = data.getAllOp();
		for(OperationInfo tempOp: temp){
			allOps.add(tempOp.getName());
		}
		
		tempIn = new JTextField("");
		tempIn.getDocument().addDocumentListener(processIn);
		tempOut = new JTextField("");
		tempOut.getDocument().addDocumentListener(processOut);
		opName = new JTextField(10);
		opName.getDocument().addDocumentListener(validateForm);
		opDesc = new JTextField(20);
		cline = new JTextField(20);
		outlistmodel = new DefaultListModel();
		inlistmodel = new DefaultListModel();
		infiles = new JList(inlistmodel);
		outfiles = new JList(outlistmodel);
		addIn = new BrowseButton(tempIn);
		addOut = new BrowseButton(tempOut);
		ok = new JButton("OK");
		ok.setEnabled(false);
		ok.addActionListener(addOp);
		cancel = new JButton("Cancel");
		cancel.addActionListener(closeForm);
		
		buildLayout();
		
		pack();
		setVisible(true);
	}
	
	/**
	 * 
	 * Create a BrowseButton for this form.
	 * @author Kathe Todd-Brown
	 *
	 */
	protected class BrowseButton extends JButton{
		
		private JTextField target;
		private GCFileChooser pick;
		
		public BrowseButton(JTextField givenTarget){
			super("Browse");
			target = givenTarget;
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					
					pick = new GCFileChooser(AddGenericOp.this, 
							null, !data.isRemote(), false, data.getConn(), 
							data.getHomeFolder());
					pick.addFileChoosenListener(new FileChoosenListener(){
						public void fileChoosenOccures(FileChoosenEvent evt) {
							target.setText(pick.fileName);
						}});
					pick.showChooser();
				}
			});
		}
	}
}
