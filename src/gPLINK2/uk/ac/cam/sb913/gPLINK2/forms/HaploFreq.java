package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * 
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class HaploFreq extends Form {

	public static String name = "Haplotype Frequencies";
	BrowseButton hapListBrowse;
	JRadioButton hapListButton, hapWindowButton;
	JTextField hapListText, hapWindowText;

	public HaploFreq(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		hapWindowText = new JTextField(10);
		hapWindowText.getDocument().addDocumentListener(validateBodyDL);
		hapListText = new JTextField(20);
		hapListText.getDocument().addDocumentListener(validateBodyDL);
		
		hapListBrowse = new BrowseButton(hapListText, 
				".hlist", "Haplotype list (*.hlist)");
		
		hapWindowButton = new JRadioButton("Sliding window (--hap-window)");
		hapWindowButton.addActionListener(validateBodyAL);
		bundel(hapWindowButton, hapWindowText);
		hapListButton = new JRadioButton("Haplotype list (--hap)");
		hapListButton.addActionListener(validateBodyAL);
		bundel(hapListButton, hapListText, hapListBrowse);

		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(hapWindowButton);
		pickOne.add(hapListButton);
		
		body.setLayout(new GridBagLayout());
		body.setBorder(new TitledBorder("Haplotype specification"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		body.add(hapListButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(hapListText,c);
		c.weightx = 0;
		c.gridx = 2;
		body.add(hapListBrowse, c);
		c.gridy = 1;
		c.gridx = 0;
		body.add(hapWindowButton, c);
		c.gridwidth = 2;
		c.gridx = 1;
		c.weightx = 1;
		body.add(hapWindowText, c);
		return body;
	}

	@Override
	protected void isBodyValid() {
		if(!(hapListButton.isSelected() || hapWindowButton.isSelected())){
//			information.setText("Must select either --hap-window or --hap flag.");
			validBody = false;
		} else if( hapListButton.isSelected() 
				&& hapListText.getText().length() == 0){
//			information.setText("Must select a file associated with the --hap flag.");
			validBody = false;
			
		} else if( hapWindowButton.isSelected() 
				 && !hapWindowText.getText().matches("\\d+")){
//			information.setText("Must enter a value associated with " +
//					"the --hap-window flag.");
			validBody = false;
		} else
			validBody = true;
		
		okForm();
	}

	@Override
	protected String processBody() {
		String ans = "--hap-freq";
		if(hapListButton.isSelected())
			ans += " --hap " + FileInfo.quote(hapListText.getText());
		if(hapWindowButton.isSelected())
			ans += " --hap-window " + hapWindowText.getText();
		return ans;
	}

}
