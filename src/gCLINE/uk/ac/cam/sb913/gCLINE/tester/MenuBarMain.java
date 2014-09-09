package gCLINE.uk.ac.cam.sb913.gCLINE.tester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import gCLINE.uk.ac.cam.sb913.gCLINE.general.Configure;

/**
 * Create a menu bar that will hold both the 
 * general project menu and the command specific forms.
 * @author Kathe Todd-Brown
 */
@SuppressWarnings("serial")
public final class MenuBarMain extends JMenuBar {

	private TestFrame parent;
	
	Configure myConfig;
	
	String project1;
	String project2;
	String project3;
	String project4;
	
	/**
	 * Create the menu for command specific forms.
	 * @return A JMenu that has the command specific forms.
	 */
	private JMenu createFormsMenu(){
		JMenu ans = new JMenu();
		return ans;
	}
	
	/**
	 * Create the menu for general project operatoins.
	 * This includes: Open, Save, Export, Rescan, Configure,
	 * previous projects and Exit.
	 * @return JMenu that holds the general options.
	 */
	private JMenu createProjectMenu(){
		JMenu ans = new JMenu("Project");
		
		//open a project file, save this as "folderName.gCLINE"
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new Open(parent);
			}
		});
		
		//save the project file
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!parent.data.saveInfo())
					parent.messenger.createError(
							"Can not save information.", "createProjectMenu");
			}
		});
		
		//backup the project to xml
		JMenuItem backup = new JMenuItem("Save As xml");
		backup.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!parent.data.backupInfo())
					parent.messenger.createError(
							"Can not backup information.", "createProjectMenu");
			}
		});
		
		//export the project to text
		
		//open the configureation dialog
		JMenuItem config = new JMenuItem("Configure");
		config.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				myConfig.makeVisible();
			}
		});
		
//		add these to the menu
		ans.add(open);
		ans.add(save);
		ans.add(backup);
		ans.add(config);
		
		project1 = parent.data.getP1();
		if(project1 != null){
			JMenuItem firstProject = new JMenuItem(project1);
			firstProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Open(parent, project1);
				}
			});
			ans.add(firstProject);
		}
		
		project2 = parent.data.getP2();
		if(project2 != null){
			JMenuItem secondProject = new JMenuItem(project2);
			secondProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Open(parent, project2);
				}
			});
			ans.add(secondProject);
		}
		
		project3 = parent.data.getP3();
		if(project3 != null){
			JMenuItem thirdProject = new JMenuItem(project3);
			thirdProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Open(parent, project3);
				}
			});
			ans.add(thirdProject);
		}
		
		project4 = parent.data.getP4();
		if(project4 != null){
			JMenuItem fourthProject = new JMenuItem(project4);
			fourthProject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					new Open(parent, project4);
				}
			});
			ans.add(fourthProject);
		}
		
		//add an exit
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.properClosing.windowClosing(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		ans.add(exit);
		
		return ans;
		
	}
	
	public MenuBarMain(TestFrame givenFrame, Configure setConfig){
		super();
		
		parent = givenFrame;
		myConfig = setConfig;

		add(createProjectMenu());
		
		add(createFormsMenu());
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.messenger.createMessage(
						" gCLINE vs" + parent.version+
						"\n Released under GNU GPLv2 \n" +
						" Writen by: Kathe Todd-Brown, Shaun Purcell \n" + 
						"Cannibalised by Sarah Binney", "");
			}
		});
		add(about);
		
	}
	
}
