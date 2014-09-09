package gPLINK2.uk.ac.cam.sb913.gPLINK2.forms;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.baseForm.Form;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;
/**
 * Reformat a fileset.
 * @author Kathe Todd-Brown
 *
 */
@SuppressWarnings("serial")
public class GenFileSet extends Form {
	/**
	 * The name of this form.
	 */
	public static String name =  "Generate fileset";
	// the various options
	private JRadioButton recode, recodeAD, recode12, recodeHV, makeBin;
		
	public GenFileSet(FormCreator f){
		super(f, name);
		//there is always a recode option selected
		validBody = true;
		pack();
		setVisible(true);
	}

	@Override
	protected String processBody() {
		String ans = "";
		if(recode.isSelected())
			ans = "--recode";
		if(recodeAD.isSelected())
			ans = "--recodeAD";
		if(recode12.isSelected())
			ans = "--recode12";
		if(recodeHV.isSelected())
			ans = "--recodeHV";
		if(makeBin.isSelected())
			ans = "--make-bed";
		
		return ans;
	}

	@Override
	protected void isBodyValid() {
		//don't do anyting since we are always true
	}
	
	@Override
	protected JPanel createBody() {
		JPanel body = new JPanel();
		
		recode = new JRadioButton("Standard fileset (--recode)");
		recode12 = new JRadioButton("Standard fileset w/ allele recoding " +
				"(--recode12)");
		recodeAD = new JRadioButton("Raw genotype file (--recodeAD)");
		recodeHV = new JRadioButton("Haploview fileset (--recodeHV)");
		makeBin = new JRadioButton("Binary fileset (--make-bed)");
	
		
	    //	 put them all in a group so we can only select one at a
		// ...time
		ButtonGroup recodeGroup = new ButtonGroup();
		recodeGroup.add(recode);
		recode.setSelected(true);
		recodeGroup.add(recodeAD);
		recodeGroup.add(recode12);
		recodeGroup.add(recodeHV);
		recodeGroup.add(makeBin);
		
		//body.setBorder(new TitledBorder("Output Options"));
		body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS));
		body.add(recode);
		body.add(recode12);
		body.add(recodeAD);
		body.add(recodeHV);
		body.add(makeBin);
		return body;
	}
	

}
