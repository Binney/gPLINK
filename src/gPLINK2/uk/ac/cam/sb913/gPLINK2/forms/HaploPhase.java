package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class HaploPhase extends Form {
	public static String name = "Haplotype phases";
	BrowseButton hapListBrowse;
	JRadioButton hapListButton, hapWindowButton;
	JTextField hapListText, hapWindowText;

	public HaploPhase(FormCreator f) {
		super(f, name);
		pack(); 
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		hapWindowText = new JTextField(10);
		hapListText = new JTextField(20);
		
		hapListBrowse = new BrowseButton(hapListText, ".hlist", "Haplotype list (*.hlist)");
		
		hapWindowButton = new JRadioButton("Sliding window (--hap-window)");
		bundel(hapWindowButton, hapWindowText);
		
		hapListButton = new JRadioButton("Haplotype list (--hap)");
		bundel(hapListButton, hapListText, hapListBrowse);
	
		hapListButton.setSelected(true);
		
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(hapWindowButton);
		pickOne.add(hapListButton);
		
		body.setLayout(new GridBagLayout());
		body.setBorder(new TitledBorder("Haplotype specification"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 3;
		body.add(new JLabel("Warning: creates one file per haplotype" +
				" locus specified"), c);
		c.gridwidth = 1;
		c.gridy = 1;
		body.add(hapListButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(hapListText,c);
		c.weightx = 0;
		c.gridx = 2;
		body.add(hapListBrowse, c);
		c.gridy = 2;
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
		}
			
		if( hapListButton.isSelected() 
				&& hapListText.getText().length() == 0){
//			information.setText("Must select a file associated with the --hap flag.");
			validBody = false;
			
		}

		if( hapWindowButton.isSelected() 
				 && !hapWindowText.getText().matches("\\d+")){
//			information.setText("Must enter a value associated with " +
//					"the --hap-window flag.");
			validBody = false;
		}

		validBody = true;
		/*
		return ( ( hapListButton.isSelected() 
				  	&& ( new File(hapListText.getText()).exists() ) ) 
				|| ( hapWindowButton.isSelected() 
						&& !hapWindowText.getText().equals("")));
						*/
	}

	@Override
	protected String processBody() {
		String ans = " --hap-phase";
		if(hapListButton.isSelected())
			ans += " --hap " + FileInfo.quote(hapListText.getText());
		if(hapWindowButton.isSelected())
			ans += " --hap-window " + hapWindowText.getText();
		return ans;
	}

}
