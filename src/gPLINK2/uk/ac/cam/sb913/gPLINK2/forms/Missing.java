package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Create a Form to test for missing data using PLINK "--missing".
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class Missing extends Form {
	public static String name = "Missingness";
	/**
	 * A JRadioButton to flag the "--missing" option
	 */
	private JRadioButton rates;
	/**
	 * A JRadioButton to flag the "--test-missing" option
	 * that looks for missing phenotype
	 */
	private JRadioButton testByPheno;
	/**
	 * A JRadioButton to flag (--test-mishap) with optional
	 *  (--mishap-window)
	 */
	private JRadioButton testByGeno;
	/**
	 * A JTextField holding the --mishap-window arguemnt.
	 */
	private JTextField mishapNum;

	/**
	 * Create a Missing form.
	 * @param mf The FormCreator instance that this is working
	 * with.
	 */
	public Missing(FormCreator f) {
		super(f, name);
		//Form does most of the create here
		pack();
		setVisible(true);
	}
	
	@Override
	protected JPanel createBody() {
		
		//intalize everything and set up the various listeners
		rates = new JRadioButton("Rates Summary (--missing)");
		rates.addActionListener(validateBodyAL);
		testByPheno = new JRadioButton("Test by Phenotype (--test-missing)");
		testByPheno.addActionListener(validateBodyAL);
		mishapNum = new JTextField("1", 3);
		mishapNum.getDocument().addDocumentListener(validateBodyDL);
		testByGeno = new JRadioButton("Test by Geonotype (--test-mishap) Optional window (--mishap-window)");
		testByGeno.addActionListener(validateBodyAL);
		//tie things together
		bundel(testByGeno, mishapNum);
	
		//make sure that one of the options is selected
		//...and only one
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(rates);
		pickOne.add(testByPheno);
		pickOne.add(testByGeno);
		rates.setSelected(true);
		//we know this makes the body of the form
		//...ok so let's not run the function
		validBody = true;
		
		//lay it all out
		JPanel body = new JPanel();
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;	
		c.gridwidth = 2;
		c.weightx = 1;
		body.add(rates, c);
		c.gridy = 1;
		body.add(testByPheno, c);
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		body.add(testByGeno, c);
		c.weightx = 1;
		c.gridx = 1;
		body.add(mishapNum, c);
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "";
		
		if(rates.isSelected())
			ans = "--missing";
		if(testByPheno.isSelected())
			ans = "--test-missing";
		if(testByGeno.isSelected()){
			ans = "--test-mishap";
			if(mishapNum.getText().matches("^\\d+$"))
				ans +=" --mishap-window " + mishapNum.getText();
		}
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		//make sure one of the options is selected and that there is something
		//...valid in the mishap number field (blank or a whole number is valid)
		if((rates.isSelected() 
				|| testByPheno.isSelected() 
				|| (testByGeno.isSelected()
						&& (mishapNum.getText().matches("^\\d+$") 
								|| mishapNum.getText().matches("^\\s*$"))))){
			validBody = true;
		} else
			validBody = false;
		
		okForm();
	}


}
