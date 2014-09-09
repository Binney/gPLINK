package gCLINE.uk.ac.cam.sb913.gCLINE.data;

import gPLINK2.uk.ac.cam.sb913.gPLINK2.formGenerators.FormCreator.FormType;

public class QueueItem {

	private String cline;
	private String description;
	private FormType formType;

	public String toString() {
		return description;
	}
	
	public String getCline() {
		return cline;
	}
	
	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType ft) {
		formType = ft;
	}
	
	public void setCline(String cmd) {
		cline = cmd;
	}

	public void	setDescription(String desc) {
		description = desc;
	}

	public QueueItem (String cmd) {
		setCline(cmd);
	}

	public QueueItem (String cmd, String description) {
		setCline(cmd);
		setDescription(description);
		formType = FormType.NONE;
	}
	
	public QueueItem (String cmd, String description, FormType ft) {
		setCline(cmd);
		setDescription(description);
		formType = ft;
	}

}