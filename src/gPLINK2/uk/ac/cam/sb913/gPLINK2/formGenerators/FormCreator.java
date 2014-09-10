package gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators;

import com.sshtools.j2ssh.SshClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import gCLINE.uk.ac.cam.sb913.gCLINE.RestoreDataDialog;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.AddGenericOp;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.FormMenuCreator;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.GPLINK;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.data.Project;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.AlleleAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.AlleleFreq;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.CCAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Clustering;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.CondHaploAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.FamAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.GenFileSet;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HapCCAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HapTDTAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HaploFreq;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HaploPhase;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HardyWinEq;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.HomozRuns;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.IBSdist;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.IndHetz;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.MendelErr;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Merge;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Missing;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.NearNeigh;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.ProxyAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.QuantTrtInt;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Regression;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.SetAssoc;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.StratAnaly;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.forms.Validate;

@SuppressWarnings("serial")
public abstract class FormCreator extends JDialog {

	public enum FormType {
		NONE,
		ALLELE_ASSOC,
		ALLELE_FREQ,
		CC_ASSOC,
		CLUSTERING,
		COND_HAPLO_ASSOC,
		FAM_ASSOC,
		GEN_FILE_SET,
		HAP_CC_ASSOC,
		HAP_TDT_ASSOC,
		HAPLO_FREQ,
		HAPLO_PHASE,
		HARDY_WIN_EQ,
		HOMOZ_RUNS,
		IBS_DIST,
		IND_HETZ,
		MENDEL_ERR,
		MERGE,
		MISSING,
		NEAR_NEIGH,
		PROXY_ASSOC,
		QUANT_TRT_INT,
		REGRESSION,
		SET_ASSOC,
		STRAT_ANALY,
		VALIDATE
	}

	protected GPLINK frame;

	public GPLINK getFrame() {
		return frame; // TODO is this required? feels messy - each class should only know about its parents, right? so frame should be local
	}

	/* The Swing components which are common to all forms. */

	/**
	 * A button to save and execute the command.
	 */
	protected JButton okButton;

	/**
	 * A button to edit the selected command.
	 */
	protected JButton editCommandButton;

	/**
	 * A button to cancel command creation and close this form.
	 */	
	protected JButton cancelButton;

	protected JTextField commandClineField;
	protected JLabel commandClineLabel;
	protected JTextField commandDescriptionField;
	protected JLabel commandDescriptionLabel;

	public FormCreator(GPLINK mf, String name) {
		super(mf, name);

	}

	// TODO the following three methods are hacks and should be removed
	public SshClient getConn() {
		return frame.data.getConn();
	}

	public boolean isRemote() {
		return frame.data.isRemote();
	}

	public String getHomeFolder() {
		return frame.data.getHomeFolder();
	}

	/**
	 * Create a JPopUpMenu which gives all command
	 * addition options, both PLINK-specific and generic commands.
	 */
	protected JPopupMenu createFormPopupMenu() {

		JPopupMenu popUpMenu = new JPopupMenu();

		popUpMenu.add(FormMenuCreator.createFormMenu(this));

		popUpMenu.add(createGenericCommandMenu());

		return popUpMenu;

	}

	protected JMenuItem createGenericCommandMenu() {

		// TODO this has been copied and pasted out of PlinkMenuBar, DRY and refactor!
		JMenuItem inputNonPlink = new JMenuItem("Add non-PLINK command");
		inputNonPlink.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// TODO targets may not be correct
				new AddGenericOp(frame);
			}
		});
		return inputNonPlink;
	}

	/**
	 * Show the form corresponding to the specified number and
	 * hide this form.
	 * @param formType the type of form to create instantly.
	 */
	public void showForm(FormType formType) {
		System.out.println("Showing a form TODO remove me " + formType.toString());
		switch (formType) {
			case NONE: break; // Do nothing; empty string represents an empty "basic" form.
			case ALLELE_ASSOC: new AlleleAssoc(this);
					this.setEnabled(false);
					break;
			case ALLELE_FREQ: new AlleleFreq(this);
					this.setEnabled(false);
					break;
			case CC_ASSOC: new CCAssoc(this);
					this.setEnabled(false);
					break;
			case CLUSTERING: new Clustering(this);
					this.setEnabled(false);
					break;
			case COND_HAPLO_ASSOC: new CondHaploAssoc(this);
					this.setEnabled(false);
					break;
			case FAM_ASSOC: new FamAssoc(this);
					this.setEnabled(false);
					break;
			case GEN_FILE_SET: new GenFileSet(this);
					this.setEnabled(false);
					break;
			case HAP_CC_ASSOC: new HapCCAssoc(this);
					this.setEnabled(false);
					break;
			case HAP_TDT_ASSOC: new HapTDTAssoc(this);
					this.setEnabled(false);
					break;
			case HAPLO_FREQ: new HaploFreq(this);
					this.setEnabled(false);
					break;
			case HAPLO_PHASE: new HaploPhase(this);
					this.setEnabled(false);
					break;
			case HARDY_WIN_EQ: new HardyWinEq(this);
					this.setEnabled(false);
					break;
			case HOMOZ_RUNS: new HomozRuns(this);
					this.setEnabled(false);
					break;
			case IBS_DIST: new IBSdist(this);
					this.setEnabled(false);
					break;
			case IND_HETZ: new IndHetz(this);
					this.setEnabled(false);
					break;
			case MENDEL_ERR: new MendelErr(this);
					this.setEnabled(false);
					break;
			case MERGE: new Merge(this);
					this.setEnabled(false);
					break;
			case MISSING: new Missing(this);
					this.setEnabled(false);
					break;
			case NEAR_NEIGH: new NearNeigh(this);
					this.setEnabled(false);
					break;
			case PROXY_ASSOC: new ProxyAssoc(this);
					this.setEnabled(false);
					break;
			case QUANT_TRT_INT: new QuantTrtInt(this);
					this.setEnabled(false);
					break;
			case REGRESSION: new Regression(this);
					this.setEnabled(false);
					break;
			case SET_ASSOC: new SetAssoc(this);
					this.setEnabled(false);
					break;
			case STRAT_ANALY: new StratAnaly(this);
					this.setEnabled(false);
					break;
			case VALIDATE: new Validate(this);
					this.setEnabled(false);
					break;
			default: // Do nothing (same as 0)
					break;
		}
	}

	public abstract void addCommand(String cmd);
	public abstract void addCommand(String cmd, String description);

	public abstract void submit();

	public abstract void setEnabled(boolean value);

	public abstract void cancelOperation();

}