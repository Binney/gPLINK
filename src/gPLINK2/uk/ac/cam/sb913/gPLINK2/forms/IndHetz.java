package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class IndHetz extends Form {
	public static String name = "Individual heterozygosity";
	public IndHetz(FormCreator f) {
		super(f, name);
		validBody = true;
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		isBodyValid();
		JPanel body = new JPanel();
		body.add(new JLabel("Individual heterozygosity (--het)"));
		return body;
	}

	@Override
	protected String processBody() {
		return "--het";
	}

	@Override
	protected void isBodyValid() {
		//this is always valid
	}

}
