package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Create an ibs distance matrix form.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class IBSdist extends Form {
	/**
	 * The name of the form.
	 */
	public static String name = "IBS distances";
	/**
	 * flag the creation of a ibs distatnce matrix
	 */
	private JCheckBox ibsDistButton;
	/**
	 * Form constructer.
	 * @param mf A FormCreator that this form is attached to.
	 */
	public IBSdist(FormCreator f) {
		super(f, name);
		validBody = true;
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		ibsDistButton = new JCheckBox("IBS distance matrix (--matrix)");
		body.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		body.add(new JLabel ("Create IBS distance file (--genome)"), c);
		c.gridy = 1;
		body.add(ibsDistButton, c);
		return body;
	}

	@Override
	protected void isBodyValid() {
		//never actually called since it is always valid
	}

	@Override
	protected String processBody() {
		String ans = "--genome";
		if(ibsDistButton.isSelected()){
			ans += " --matrix --cluster"; 
		}
		return ans;
	}

}
