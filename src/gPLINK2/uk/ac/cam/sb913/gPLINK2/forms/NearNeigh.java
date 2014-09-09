package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Allow the user to create a nearest neighbour command.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class NearNeigh extends Form {
	/**
	 * The name of this form.
	 */
	public static String name = "Nearest Neighbour";
	JTextField neigh1Text, neigh2Text, genomicText;
	JCheckBox neighButton;
	/**
	 * Create the form.
	 * @param mf The FormCreator that this form is attached to.
	 */
	public NearNeigh(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		genomicText = new JTextField(10);
		genomicText.getDocument().addDocumentListener(validateBodyDL);
		neigh1Text = new JTextField(4);
		neigh1Text.getDocument().addDocumentListener(validateBodyDL);
		neigh2Text = new JTextField(4);
		neigh2Text.getDocument().addDocumentListener(validateBodyDL);
		
		JTextField [] temp = {neigh1Text, neigh2Text};
		
		neighButton = new JCheckBox("Outlier statistics");
		neighButton.addActionListener(validateBodyAL);
		bundel(neighButton, temp);
		
		body.setLayout(new GridBagLayout());
		
		JPanel genoPanel = new JPanel();
		genoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		genoPanel.add(new JLabel("IBS distance file (--read-genome)"), c);
		c.weightx = 1;
		c.gridx = 1;
		genoPanel.add(genomicText, c);
		c.gridx = 2;
		c.weightx = 0;
		genoPanel.add(new BrowseButton(genomicText, ".genome", 
				"GENOME"), c);
		
		JPanel neighPanel = new JPanel();
		neighPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		c = new GridBagConstraints();
		neighPanel.add(neighButton, c);
		c.gridx = 1;
		neighPanel.add(neigh1Text, c);
		c.gridx = 2;
		neighPanel.add(neigh2Text, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1;
		c.weightx = 1;
		body.add(genoPanel, c);
		c.gridy = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		body.add(neighPanel, c);
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "";
		ans += "--cluster ";
		ans += "--read-genome " + FileInfo.quote(genomicText.getText());
		if(neighButton.isSelected())
			ans += " --neighbour " 
				+ neigh1Text.getText() 
				+ " " + neigh2Text.getText();
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		if(genomicText.getText().length() < 0){
//			information.setText("Must select a genome file to read from.");
			validBody = false;
		} else if (neighButton.isSelected() 
				&& ! ( neigh1Text.getText().matches("\\d+")
					  && neigh2Text.getText().matches("\\d+") )){
//			information.setText("Must set valid --neighbour parameters.");
			validBody = false;
		} else
			validBody = true;
		
		okForm();
	
	}

}
