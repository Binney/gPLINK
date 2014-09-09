package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class HomozRuns extends Form {
	/**
	 * Optionally log messages.
	 */
	private static Logger logger = Logger.getLogger(HomozRuns.class);
	
	public static String name = "Runs of homozygosity";
	private JRadioButton homSNPButton, homKBButton;
	private JCheckBox		
		homGroupButton, homMatchButton, homHetButton, homVerboseButton;
	
	private JTextField homSNPText, homKBText, homMatchText, homHetText;

	public HomozRuns(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();

		homSNPText = new JTextField(4);
		homSNPText.getDocument().addDocumentListener(validateBodyDL);
		homKBText = new JTextField(4);
		homKBText.getDocument().addDocumentListener(validateBodyDL);
		homHetText = new JTextField(4);
		homHetText.getDocument().addDocumentListener(validateBodyDL);
		homMatchText = new JTextField(4);
		homMatchText.getDocument().addDocumentListener(validateBodyDL);
		
		homSNPButton = new JRadioButton("Homozygous run length, SNPs" +
				" (--homozyg-snp)");
		homSNPButton.addActionListener(validateBodyAL);
		bundel(homSNPButton, homSNPText);
		homKBButton = new JRadioButton("Homozygous run length, kb " +
				"(--homozyg-kb)");
		homKBButton.addActionListener(validateBodyAL);
		bundel(homKBButton, homKBText); 
		
		homHetButton = new JCheckBox("Heterozygotes allowed in " +
				"homozygous run (--homozyg-het)");
		homHetButton.addActionListener(validateBodyAL);
		bundel(homHetButton, homHetText);
		homGroupButton = new JCheckBox("Group overlapping segments " +
				"(--homozyg-group)");
		homGroupButton.addActionListener(validateBodyAL);
		homMatchButton = new JCheckBox("Threshold for allelic match " +
				"(--homozyg-match)");
		homMatchButton.addActionListener(validateBodyAL);
		bundel(homMatchButton, homMatchText);
		homVerboseButton = new JCheckBox("Verbose segment listing " +
				"(--homozyg-verbose)");
		homVerboseButton.addActionListener(validateBodyAL);
		
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		body.add(homKBButton, c);
		c.gridx = 1;
		c.weightx = 1;
		body.add(homKBText, c);
		
		c.weightx = 0;
		c.gridy = 1;
		c.gridx = 0;
		body.add(homSNPButton, c);
		
		c.gridx = 1;
		c.weightx = 1;
		body.add(homSNPText,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		body.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridy = 3;
		c.gridx = 0;
		body.add(homHetButton, c);
		c.gridx = 1;
		body.add(homHetText, c);
		
		c.gridy = 4;
		c.gridx = 0;
		body.add(homGroupButton, c);
		
		c.gridy = 5;
		body.add(homMatchButton, c);
		
		c.gridx = 1;
		body.add(homMatchText, c);
		
		c.gridy = 6;
		c.gridx = 0;
		body.add(homVerboseButton, c);
		
		validBody = true;
		
		return body;
	}

	@Override
	protected String processBody() {
		String ans = " --homozyg";

		if(homVerboseButton.isSelected())
			ans = " --homozyg-verbose";
		if(homKBButton.isSelected())
			ans += " --homozyg-kb " + homKBText.getText();
		if(homSNPButton.isSelected())
			ans += " --homozyg-snp " + homSNPText.getText();
		if(homHetButton.isSelected())
			ans += " --homozyg-het " + homHetText.getText();
		if(homGroupButton.isSelected())
			ans += " --homozyg-group";
		if(homMatchButton.isSelected())
			ans += " --homozyg-match " + homMatchText.getText();
		
		
		ans = ans.replaceFirst(" ", "");
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		logger.info("[isBodyValid()] Entering..." );
		if(homKBButton.isSelected() && !homKBText.getText().matches("\\d+")){
//			information.setText("Enter the associated value for --homozyg-kb.");
			validBody = false;
		} else if(homSNPButton.isSelected() && !homSNPText.getText().matches("\\d+")){
//			information.setText("Enter the associated value for --homozyg-snp.");
			validBody = false;
		} else if(homHetButton.isSelected() && !homHetText.getText().matches("\\d+")){
//			information.setText("Enter the associated value for --homozyg-het.");
			validBody = false;
		} else if(homMatchButton.isSelected() && 
				!homMatchText.getText().matches("\\d*\\.?\\d+")){
//			information.setText("Enter the associated value for --homozyg-match.");
			validBody = false;
		} else 
			validBody = true;
		
		okForm();
		logger.info("[isBodyValid()] Exiting..." );
		
	}
}
