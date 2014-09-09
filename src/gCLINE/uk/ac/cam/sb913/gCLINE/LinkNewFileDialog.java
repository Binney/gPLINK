package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenEvent;
import gCLINE.uk.ac.cam.sb913.gCLINE.general.GCFileChooser.FileChoosenListener;

/**
 * A JDailgo that allow the user to link a file to an operation.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class LinkNewFileDialog extends JDialog {

	/**
	 * A Record that this operation is stored in.
	 */
	private Record data;
	/**
	 * A String that identifies the operation you
	 * want to link the file to.
	 */
	private String opName;
	/**
	 * A JTextField that holds the file name.
	 */
	private JTextField addFile;
	/**
	 * A JRadioButton that flags this as an input
	 * file.
	 */
	private JRadioButton infile;
	
	/**
	 * The ActionListener that adds the file to the operation.
	 * If there is nothing in the text field then it does nothng.
	 */
	ActionListener process = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			if(addFile.getText().equals("") ||addFile.getText().matches("\\s"))
				LinkNewFileDialog.this.dispose();
			
			String fileType;
			
			if(infile.isSelected())
				fileType = Record.INFILE_KEY;
			else
				fileType = Record.OUTFILE_KEY;
			
			LinkNewFileDialog.this.data.getOp(opName)
				.addFile(fileType, addFile.getText(), "", "");
			LinkNewFileDialog.this.dispose();
		}
	};
	/**
	 * An ActionListener that closes the dialog without
	 * doing anything.
	 */
	ActionListener cancel = new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			LinkNewFileDialog.this.dispose();
		}
	};
	/**
	 * Create the panel that holds the file text box
	 * and corrisponding browse button.
	 * @return A JPanel that holds the file information.
	 */ 
	private JPanel createFilePane(){
		JPanel filePane = new JPanel();
		addFile = new JTextField(10);
		filePane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		filePane.add(addFile, c);
		c.weightx = 0;
		c.gridx = 1;
		filePane.add(new BrowseButton(addFile));
		return filePane;
	}
	/**
	 * Create a JPanel that allows the user to
	 * select the file as either an input or output
	 * file.
	 * @return A JPanel that contains in/output flags.
	 */
	private JPanel createInOutPane(){
		JPanel inOutPane = new JPanel();
		
		infile = new JRadioButton("Input file");
		infile.setSelected(true);
		JRadioButton outfile = new JRadioButton("Output file");
		
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(infile);
		pickOne.add(outfile);
		
		inOutPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.gridwidth = 2;
		inOutPane.add(new JLabel("Link to: " + opName), c);
		c.weightx = 0.5;
		c.gridwidth = 1;
		c.gridy = 1;
		inOutPane.add(infile, c);
		c.gridx = 1;
		inOutPane.add(outfile, c);
		
		return inOutPane;
	}
	/**
	 * Create a panel to hold the ok/cancel buttons.
	 * @return A JPanel that has the ok/cancel buttons.
	 */
	private JPanel createButtonPane(){
		JPanel buttonPane = new JPanel();
		
		JButton ok = new JButton("Ok");
		ok.addActionListener(process);
		JButton quit = new JButton("Cancel");
		quit.addActionListener(cancel);
		
		buttonPane.add(ok);
		buttonPane.add(quit);
		return buttonPane;
	}

	/**
	 * Create a Dialog that will link a file to a given operation.
	 * @param mf The StartFrame that this dialog will be 
	 * attached to.
	 * @param d The Record that this operation is in.
	 * @param given_opName A String that indeitifies the operation.
	 */
	public LinkNewFileDialog(StartFrame mf, Record d, String given_opName){
		super(mf, "Link New File to " + given_opName);
		
		data = d;
		opName = given_opName;
		
		getContentPane().setLayout(new BoxLayout(this.getContentPane(),
				BoxLayout.PAGE_AXIS));
		getContentPane().add(createFilePane());
		getContentPane().add(createInOutPane());
		getContentPane().add(createButtonPane());
		
		pack();
		setVisible(true);
	}
	@SuppressWarnings({ "serial", "unused" })
	private class BrowseButton extends JButton{
		
		private String suffix;
		private String suffixDescription;
		private JTextField target;
		private GCFileChooser pick;
		BrowseButton(JTextField givenTarget){
			super("Browse");
			target = givenTarget;
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					pick = new GCFileChooser(
							LinkNewFileDialog.this, 
							null, 
							!data.isRemote(), 
							false,
							data.getConn(), 
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
