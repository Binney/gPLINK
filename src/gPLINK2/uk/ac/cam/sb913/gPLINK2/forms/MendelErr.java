package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import javax.swing.JPanel;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

@SuppressWarnings("serial")
public class MendelErr extends Form {
	public static String name = "Mendel Error Rates";
	
	public MendelErr(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		isBodyValid();
		return new JPanel();
	}

	@Override
	protected void isBodyValid() {
		validBody = true;
	}

	@Override
	protected String processBody() {
		return " --mendel";
	}

}
