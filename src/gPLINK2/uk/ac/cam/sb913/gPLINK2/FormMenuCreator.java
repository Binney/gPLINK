package gPLINK2.uk.ac.cam.sb913.gPLINK2;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator;
import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator.FormType;
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

public class FormMenuCreator {
	
	public JMenu createFormMenu(FormCreator dialog) {

		JMenu formMenu = new JMenu("PLINK command");

		formMenu.add(createDataManagementMenu(dialog));
		formMenu.add(createSummaryStatisticsMenu(dialog));
		formMenu.add(createStratificationMenu(dialog));
		formMenu.add(createAssociationMenu(dialog));
		formMenu.add(createIBDMenu(dialog));

		return formMenu;

	}

	private JMenu createDataManagementMenu(FormCreator dialog) {
		JMenu dataMangMenu = new JMenu("Data Management");

		dataMangMenu.add(createFilesets(dialog));
		dataMangMenu.add(createMerge(dialog));

		return dataMangMenu;
	}

	// <editor-fold defaultstate="collapsed" desc="Data management menu items">

	private JMenuItem createFilesets(final FormCreator dialog) {
		JMenuItem filesets = new JMenuItem(GenFileSet.name);
		filesets.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.GEN_FILE_SET);
			}
		});	
		return filesets;
	}

	private JMenuItem createMerge(final FormCreator dialog) {
		JMenuItem merge = new JMenuItem(Merge.name);
		merge.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.MERGE);
			}
		});
		return merge;
	}

	// </editor-fold>
	
	private JMenu createSummaryStatisticsMenu(FormCreator dialog) {
		JMenu summaryMenu = new JMenu("Summary Statistics");

		summaryMenu.add(createValidate(dialog));
		summaryMenu.add(createMissing(dialog));
		summaryMenu.add(createHWE(dialog));
		summaryMenu.add(createMendelErr(dialog));
		summaryMenu.add(createAlleleFreq(dialog));
		summaryMenu.add(createHapFreq(dialog));
		summaryMenu.add(createHapPhase(dialog));

		return summaryMenu;
	}

	// <editor-fold defaultstate="collapsed" desc="Statistics menu items">

	private JMenuItem createValidate(final FormCreator dialog) {
		JMenuItem validate = new JMenuItem(Validate.name);
		validate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.VALIDATE);
			}
		});
		return validate;
	}

	private JMenuItem createMissing(final FormCreator dialog) {
		JMenuItem missing = new JMenuItem(Missing.name);
		missing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.MISSING);
			}
		});
		return missing;
	}

	private JMenuItem createHWE(final FormCreator dialog) {
		JMenuItem hwe = new JMenuItem(HardyWinEq.name);
		hwe.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HARDY_WIN_EQ);
			}
		});
		return hwe;
	}

	private JMenuItem createMendelErr(final FormCreator dialog) {
		JMenuItem mendel = new JMenuItem(MendelErr.name);
		mendel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.MENDEL_ERR);
			}
		});
		return mendel;
	}

	private JMenuItem createAlleleFreq(final FormCreator dialog) {
		JMenuItem alleleFreq = new JMenuItem(AlleleFreq.name);
		alleleFreq.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.ALLELE_FREQ);
			}
		});
		return alleleFreq;
	}

	private JMenuItem createHapFreq(final FormCreator dialog) {
		JMenuItem hapFreq = new JMenuItem(HaploFreq.name);
		hapFreq.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HAPLO_FREQ);
			}
		});
		return hapFreq;
	}

	private JMenuItem createHapPhase(final FormCreator dialog) {
		JMenuItem hapPhase = new JMenuItem(HaploPhase.name);
		hapPhase.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HAPLO_PHASE);
			}
		});
		return hapPhase;		
	}

	// </editor-fold>

	private JMenu createStratificationMenu(final FormCreator dialog) {
		JMenu stratMenu = new JMenu("Stratification");
		
		stratMenu.add(createIBS(dialog));
		stratMenu.add(createClust(dialog));
		stratMenu.add(createNeighbours(dialog));

		return stratMenu;
	}

	// <editor-fold defaultstate="collapsed" desc="Stratification menu items">

	private JMenuItem createIBS(final FormCreator dialog) {
		JMenuItem ibs = new JMenuItem(IBSdist.name);
		ibs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.IBS_DIST);
			}
		});
		return ibs;
	}

	private JMenuItem createClust(final FormCreator dialog) {
		JMenuItem clust = new JMenuItem(Clustering.name);
		clust.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.CLUSTERING);
			}
		});
		return clust;
	}

	private JMenuItem createNeighbours(final FormCreator dialog) {
		JMenuItem neigh = new JMenuItem(NearNeigh.name);
		neigh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.NEAR_NEIGH);
			}
		});
		return neigh;
	}

	// </editor-fold>

	private JMenu createAssociationMenu(FormCreator dialog) {

		JMenu associationMenu = new JMenu("Association");
		
		associationMenu.add(createAlleleAssoc(dialog));
		associationMenu.add(createCCAssoc(dialog));
		associationMenu.add(createFamAssoc(dialog));
		associationMenu.add(createStratAnaly(dialog));
		associationMenu.add(createQTI(dialog));
		associationMenu.add(createRegress(dialog));
		associationMenu.add(createSetAssoc(dialog));
		associationMenu.add(createHaplotypeCC(dialog));
		associationMenu.add(createHaplotypeTDT(dialog));
		associationMenu.add(createCondHap(dialog));
		associationMenu.add(createProxy(dialog));
		
		return associationMenu;
	}

	// <editor-fold defaultstate="collapsed" desc="Association menu items">

	private JMenuItem createAlleleAssoc(final FormCreator dialog) {
		JMenuItem alleleA = new JMenuItem(AlleleAssoc.name);
		alleleA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.ALLELE_ASSOC);
			}
		});
		return alleleA;
	}

	private JMenuItem createCCAssoc(final FormCreator dialog) {
		JMenuItem caseControl =  new JMenuItem(CCAssoc.name);
		caseControl.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.CC_ASSOC);
			}
		});
		return caseControl;
	}

	private JMenuItem createFamAssoc(final FormCreator dialog) {
		JMenuItem famAssoc = new JMenuItem(FamAssoc.name);
		famAssoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.FAM_ASSOC);
			}
		});
		return famAssoc;
	}

	private JMenuItem createStratAnaly(final FormCreator dialog) {
		JMenuItem stratAnaly = new JMenuItem(StratAnaly.name);
		stratAnaly.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.showForm(FormType.STRAT_ANALY);
			}
		});
		return stratAnaly;
	}

	private JMenuItem createQTI(final FormCreator dialog) {
		JMenuItem qti = new JMenuItem(QuantTrtInt.name);
		qti.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.QUANT_TRT_INT);
			}
		});
		return qti;
	}

	private JMenuItem createRegress(final FormCreator dialog) {
		JMenuItem regress = new JMenuItem(Regression.name);
		regress.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.REGRESSION);
			}
		});
		return regress;
	}

	private JMenuItem createSetAssoc(final FormCreator dialog) {
		JMenuItem setAssoc = new JMenuItem(SetAssoc.name);
		setAssoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.SET_ASSOC);
			}
		});
		return setAssoc;
	}

	private JMenuItem createHaplotypeCC(final FormCreator dialog) {
		JMenuItem haplotypeCC = new JMenuItem(HapCCAssoc.name);
		haplotypeCC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HAP_CC_ASSOC);
			}
		});
		return haplotypeCC;
	}

	private JMenuItem createHaplotypeTDT(final FormCreator dialog) {
		JMenuItem haplotypeTDT = new JMenuItem(HapTDTAssoc.name);
		haplotypeTDT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HAP_TDT_ASSOC);
			}
		});
		return haplotypeTDT;
	}

	private JMenuItem createCondHap(final FormCreator dialog) {
		JMenuItem condHap = new JMenuItem(CondHaploAssoc.name);
		condHap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.COND_HAPLO_ASSOC);
			}
		});
		return condHap;		
	}

	private JMenuItem createProxy(final FormCreator dialog) {
		JMenuItem proxy = new JMenuItem(ProxyAssoc.name);
		proxy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.PROXY_ASSOC);
			}
		});
		return proxy;
	}
	
	// </editor-fold>

	private JMenu createIBDMenu(FormCreator dialog) {
		JMenu ibdMenu = new JMenu("IBD Estimation");

		ibdMenu.add(createHetz(dialog));
		ibdMenu.add(createHomoz(dialog));
		
		return ibdMenu;
	}

	// <editor-fold defaultstate="collapsed" desc="IBD menu items">

	private JMenuItem createHetz(final FormCreator dialog) {
		JMenuItem hetz = new JMenuItem(IndHetz.name);
		hetz.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.IND_HETZ);
			}
		});
		return hetz;
	}

	private JMenuItem createHomoz(final FormCreator dialog) {
		JMenuItem homoz = new JMenuItem(HomozRuns.name);
		homoz.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.showForm(FormType.HOMOZ_RUNS);
			}
		});
		return homoz;
	}

	// </editor-fold>

}