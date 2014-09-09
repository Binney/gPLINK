package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import javax.swing.JPanel;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Create a form that checks the Hardy-Weinburg
 * equilibrium of a given data set.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public class HardyWinEq extends Form {
	/**
	 * The name of the form.
	 */
	public static String name = "Hardy Weinberg";
	/**
	 * Create a Hardy-Weinburg form.
	 * @param mf The GPLINK instance this is attached to.
	 */
	public HardyWinEq(FormCreator f) {
		super(f, name);
		validBody = true;
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		return new JPanel();
	}

	@Override
	protected void isBodyValid() {
		//nothing here since it is always true
	}

	@Override
	protected String processBody() {
		return "--hardy";
	}

}
