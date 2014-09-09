package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gCLINE.uk.ac.cam.sb913.gCLINE.data.infos.FileInfo;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;

/**
 * A Quantitative trait interaction form.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class QuantTrtInt extends Form {
	/**
	 * The name of this form
	 */
	public static String name = "Quantitative trait interaction";
	/**
	 * The covarienance file associated with this test
	 */
	private JTextField coverFile;
	/**
	 * Create a form.
	 * @param mf The FormCreator that this form is attached to.
	 */
	public QuantTrtInt(FormCreator f) {
		super(f, name);
		pack();
		setVisible(true);
	}

	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		coverFile = new JTextField(10);
		coverFile.addActionListener(validateBodyAL);
		
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
		body.add(Box.createRigidArea(new Dimension(0,10)));
		
		body.add(new JLabel("Quantitative phenotype, binary covariate " +
				"test for interaction"));
		body.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel gxePane = new JPanel();
		gxePane.setAlignmentX(Component.LEFT_ALIGNMENT);
		gxePane.setLayout(new BoxLayout(gxePane, BoxLayout.LINE_AXIS));
		gxePane.add(new JLabel("--gxe --covar "));
		gxePane.add(coverFile);
		gxePane.add(new BrowseButton(coverFile, ".cov", "Covariate File" ));
		body.add(gxePane);
		body.add(Box.createRigidArea(new Dimension(0,10)));
		return body;
	}

	@Override
	protected String processBody() {
		String ans = "--gxe --covar " + FileInfo.quote(coverFile.getText());
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		if(coverFile.getText().length() == 0){
//			information.setText("Select a valid file for --gxe --cover");
			validBody = false;
		} else
			validBody = true;
		
		okForm();
	}

}
