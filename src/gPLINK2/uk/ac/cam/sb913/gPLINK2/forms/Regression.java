package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;
/**
 * A linear/logistic regression form.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class Regression extends Form {
	/**
	 * The name of this form
	 */
	public static String name = "Linear/logistic regression";
	//buttons that allow the user to select the --freq and
	//...--count commands
	private JRadioButton freqButton, genoButton;
	//allows the user to specify an optional --within
	private JCheckBox covarButton;
	//picks the --within file
	private JTextField covarField;
	//button pick the file associated with --within
	BrowseButton pickCovar;

		
	public Regression(FormCreator f) {
		super(f, name);
		validBody =  true;
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		covarField = new JTextField(10);
		covarField.getDocument().addDocumentListener(validateBodyDL);
		pickCovar = new BrowseButton(covarField, ".cov", "Covariate File");
		pickCovar.setEnabled(false);
		
		freqButton = new JRadioButton("Linear/logistic regression (--linear)");
		freqButton.addActionListener(validateBodyAL);
		freqButton.setSelected(true);
		genoButton = new JRadioButton("2df genotypic model (--linear --genotypic)");
		genoButton.addActionListener(validateBodyAL);
		covarButton = new JCheckBox("Covariate file (--covar)"); 
		covarButton.addActionListener(validateBodyAL);
		bundel(covarButton, covarField, pickCovar);
		
		//put the --freq and --count in a buton group so that
		//...the user can only use one or the other and is forced
		//...to select atleast one to excicute the form.
		ButtonGroup pickOne = new ButtonGroup();
		pickOne.add(freqButton);
		pickOne.add(genoButton);
		
		//set the layout for the form
		body.setLayout(new GridBagLayout());
		//declare and initalze the constraits for this layout
		GridBagConstraints c = new GridBagConstraints();
		//expand all the components in this layout to fill all
		//...avaliable horizontal space
		c.fill = GridBagConstraints.BOTH;
		//add the frequency button to the layout with the given
		//...constraints
		body.add(freqButton,c);
		//move down before adding the next one
		c.gridy = 1;
		body.add(genoButton,c);
		//move down again
		c.gridy = 2;
		body.add(covarButton, c);
		//let the next component resize its self to fill all avaliable
		//...space
		c.weightx = 1;
		c.gridx = 1;
		body.add(covarField, c);
		//we don't want the next component to resize
		c.weightx = 0;
		c.gridx = 2;
		
		body.add(pickCovar, c);
		return body;
		
	}

	@Override
	protected void isBodyValid() {
		if( covarButton.isSelected() 
				&& covarField.getText().length() == 0){
//			information.setText("When --covar is selected you must specify a file.");
			validBody =false;
		}else 
			validBody = true;
		
		okForm();
	}

	@Override
	protected String processBody() {
		String ans = "--linear";
		
		if(genoButton.isSelected())
			ans += " --genotypic";
	
		if(covarButton.isSelected())
			//since within takes a filename we need to throw
			//...quotes around it if we are in a window's os
			ans += " --covar " + FileInfo.quote(covarField.getText());

		
		return ans;
	}

}
