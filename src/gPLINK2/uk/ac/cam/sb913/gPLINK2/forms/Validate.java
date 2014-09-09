package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import javax.swing.JPanel;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * Create a form that will provide basic summary
 * statistics for a file set.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class Validate extends Form {
	/**
	 * The name of this form
	 */
	public static String name = "Validate Fileset";
	/**
	 * Create a form that will allow the user to select
	 * which fileset they want to validate and what they
	 * want to call the output files.
	 * @param p The FormCreator this is attached to.
	 */
	public Validate(FormCreator f) {
		super(f, name);
		validBody = true;
		okForm();
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		return new JPanel();
	}

	@Override
	protected String processBody() {
		String cmd = "";
		return cmd;
	}

	@Override
	protected void isBodyValid() {
		//nothing here
	}

}
