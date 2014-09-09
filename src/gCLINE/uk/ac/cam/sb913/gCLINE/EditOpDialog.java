package gCLINE.uk.ac.cam.sb913.gCLINE;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.Record;
import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;

/**
 * A dialog that allows the user to edit the annotations on a
 * given operation and associated files.
 * 
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public class EditOpDialog extends JDialog {

	// used to populate the fields and store the
	// ...new operations
	private Record data;

	// The components of this dialog box
	private JLabel descLabel;

	private JTextField descText;

	private JTextArea clineText;

	private JLabel[] infileLabel, outfileLabel;

	private JTextField[] infileText, outfileText;

	private JPanel input, output, buttons;

	private JScrollPane command;

	private JButton ok, cancel;

	// store the old name so that we can delete it if we
	// ...want to replace the operation
	private String oldname;

	private String time;
	// store the number of input files of the operation
	private int numInfile;

	// store the number of output files of the operation
	private int numOutfile;

	/**
	 * Create a dialog to edit an operations entry
	 */
	public EditOpDialog(String givenName, StartFrame givenMF,
			Record d) {
		super(givenMF, "Edit Operation");
		
//		 set the given variables
		data = d;
		oldname = givenName;
		
		time = data.getOp(oldname).getTimeStamp();
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		descLabel = new JLabel("Description :");
		getContentPane().add(descLabel, c);
		descText = new JTextField(data.getOp(oldname).getDescription(), 40);
		c.gridx = 1;
		c.weightx = 1;
		getContentPane().add(descText, c);

		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weighty = 1;
		clineText = new JTextArea(data.getOp(oldname).getCline());
		clineText.setEditable(false);
		command = new JScrollPane(clineText);
		getContentPane().add(command, c);
		
		input = new JPanel();
		input.setBorder(new TitledBorder("Input Files"));
		ArrayList <FileInfo> infiles = data.getOp(oldname).getInputFiles();
		numInfile = infiles.size();
		
		input.setLayout(new GridLayout(numInfile, 2));
		infileLabel = new JLabel[numInfile];
		infileText = new JTextField[numInfile];
		for (int i = 0; i < numInfile; i++) {
			infileLabel[i] = 
				new JLabel(infiles.get(i).toString());
			input.add(infileLabel[i]);
			infileText[i] =
				new JTextField(infiles.get(i).getLocalDesc());
			input.add(infileText[i]);
		}
		JScrollPane inscroll = new JScrollPane(input);
		//inscroll.setPreferredSize(new Dimension(100, 100));
		//inscroll.revalidate();
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridheight = 5;
		c.weighty = 1;
		getContentPane().add(inscroll, c);

		output = new JPanel();
		output.setBorder(new TitledBorder("Output Files"));
		ArrayList <FileInfo> outfiles = data.getOp(oldname).getOutputFiles();
		numOutfile = outfiles.size();
		
		output.setLayout(new GridLayout(numOutfile, 2));
		outfileLabel = new JLabel[numOutfile];
		outfileText = new JTextField[numOutfile];
		int i = 0;
		for (int j = 0; j < numOutfile; j ++){
			
			outfileLabel[i] = 
				new JLabel(outfiles.get(i).toString());
			output.add(outfileLabel[i]);
			outfileText[i] = 
				new JTextField(outfiles.get(i).getLocalDesc());
			output.add(outfileText[i]);
			i++;
			
		}
		
		JScrollPane outscroll = new JScrollPane(output);
		//outscroll.setPreferredSize(new Dimension(100, 100));
		//outscroll.revalidate();
		c.gridy = 7;
		getContentPane().add(outscroll, c);

		ok = new JButton("OK");
		ok.addActionListener(replaceOp);
		cancel = new JButton("Cancel");
		cancel.addActionListener(closeDialog);
		buttons = new JPanel();
		buttons.add(ok);
		buttons.add(cancel);

		c.weighty = 0;
		c.gridheight = 1;
		c.gridy = 12;
		getContentPane().add(buttons, c);

		pack();
		setVisible(true);
	}

	ActionListener replaceOp = new ActionListener() {
		public void actionPerformed(ActionEvent event) {

			String descript, cline, name;
			ArrayList <String[]> infiles = new ArrayList<String[]>();
			ArrayList <String[]> outfiles = new ArrayList<String[]>();
			
			name = oldname;
			descript = EditOpDialog.this.descText.getText();
			cline = EditOpDialog.this.clineText.getText();

			for (int i = 0; i < infileLabel.length; i++) {
				infiles.add(new String[]{infileLabel[i].getText(),
						infileText[i].getText()});
			}

			for (int i = 0; i < outfileLabel.length; i++) {
				outfiles.add(new String []{outfileLabel[i].getText(),
						outfileText[i].getText()});
			}

			EditOpDialog.this.data.addOperation(name, descript, cline, time, infiles, outfiles, null);
			
			EditOpDialog.this.dispose();
		}
	};

	ActionListener closeDialog = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			EditOpDialog.this.dispose();
		}
	};
	
	

}
