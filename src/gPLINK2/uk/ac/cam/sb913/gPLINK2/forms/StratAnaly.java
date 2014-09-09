package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class StratAnaly extends Form{

	public static String name = "Stratified Analyses";
	JRadioButton mhButton, mh2Button, homogButton, 
						withinButton, familyButton;
	JCheckBox ciButton, bdButton,  adjustButton;
	JTextField withinText, ciText;
	BrowseButton withinBrowse;
	
	static Double CI_DEFAULT = new Double(0.95);
	
	public StratAnaly(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		validBody = true;
		JPanel body = new JPanel();
		
		withinText = new JTextField(10);
		withinText.getDocument().addDocumentListener(validateBodyDL);
		withinBrowse = new BrowseButton(withinText, ".cluster2","CLUSTER");
		ciText = new JTextField(CI_DEFAULT.toString(), 4);
		ciText.getDocument().addDocumentListener(validateBodyDL);
		
		mhButton = new JRadioButton("Cochran-Mantel-Haenszel, " +
				"				SNP-DISEASE | CLUSTER (--mh)");
		mhButton.addActionListener(validateBodyAL);
		bdButton = new JCheckBox("Breslow-Day test of heterogeneous " +
				"				CMH ORs (--bd) ");
		bdButton.addActionListener(validateBodyAL);
		mh2Button = new JRadioButton("Cochran-Mantel-Haenszel, " +
				"				SNP-CLUSTER | DISEASE (--mh2)");
		mh2Button.addActionListener(validateBodyAL);
		adjustButton = new JCheckBox("Adjusted p-values (--adjust)");
		adjustButton.addActionListener(validateBodyAL);
		homogButton = new JRadioButton("Test of homogeneous association " +
				"				(--homog) ");
		homogButton.addActionListener(validateBodyAL);
		withinButton = new JRadioButton("Cluster file (--within) ");
		withinButton.addActionListener(validateBodyAL);
		bundel(withinButton, withinText, withinBrowse); 
		familyButton = new JRadioButton("Cluster by family (--family)");
		familyButton.addActionListener(validateBodyAL);
		ciButton = new JCheckBox("Confidence intervals (--ci) ");
		ciButton.addActionListener(validateBodyAL);
		bundel(ciButton, ciText);
		
		ButtonGroup forceOne = new ButtonGroup();
		forceOne.add(mhButton);
		mhButton.setSelected(true);
		forceOne.add(mh2Button);
		forceOne.add(homogButton);
		
		ButtonGroup forceOne2 = new ButtonGroup();
		forceOne2.add(familyButton);
		familyButton.setSelected(true);
		forceOne2.add(withinButton);
		
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		body.add(mhButton, c);
		c.gridy = 1;
		body.add(mh2Button, c);
		c.gridy = 2;
		body.add(homogButton, c);
		c.gridy = 3;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridy = 4;
		body.add(familyButton, c);
		c.weightx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		body.add(withinButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(withinText, c);
		c.gridx = 2;
		c.weightx = 0;
		body.add(withinBrowse, c);
		c.gridx = 0;
		c.gridwidth = 3;
		c.gridy = 6;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.gridy = 7;
		body.add(bdButton, c);
		c.gridy = 8;
		body.add(adjustButton, c);
		c.gridy = 9;
		c.gridwidth = 1;
		body.add(ciButton, c);
		c.gridx = 1;
		body.add(ciText, c);
		
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "";
		
		if(bdButton.isSelected())
			ans += " --bd";
		if(homogButton.isSelected())
			ans += " --homog";
		if(mhButton.isSelected())
			ans += " --mh";
		if(mh2Button.isSelected())
			ans += " --mh2";
		if(adjustButton.isSelected())
			ans += " --adjust";
		if(withinButton.isSelected())
			ans += " --within " + FileInfo.quote(withinText.getText());
		if(familyButton.isSelected())
			ans += " --family";
		if(ciButton.isSelected())
			ans += " --ci " + ciText.getText();
		
		ans = ans.replaceFirst(" ", "");
		return ans;
	}

	@Override
	protected void isBodyValid() {
		if (! (mhButton.isSelected() || mh2Button.isSelected() 
				|| homogButton.isSelected())){
//			information.setText("Must select either --mh, --mh2 or --homog.");
			validBody = false;
		}else if(!(withinButton.isSelected() || familyButton.isSelected())){
//			information.setText("Must select either --family or --within.");
			validBody = false;
		}else if(withinButton.isSelected() && withinText.getText().length() == 0 ){
//			information.setText("Must select a valid file for --within.");
			validBody = false;
		}else
			validBody = true;
		
		okForm();
	}

}
