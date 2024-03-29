/*
 *  SSHTools - Java SSH2 API
 *
 *  Copyright (C) 2002-2003 Lee David Painter and Contributors.
 *
 *  Contributions made by:
 *
 *  Brett Smith
 *  Richard Pernavas
 *  Erwin Bolwidt
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.sshtools.common.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class NewWindowAction extends StandardAction {
    /**  */
    protected Log log = LogFactory.getLog(NewWindowAction.class);

    //
    private SshToolsApplication application;

    /**
* Creates a new NewWindowAction object.
*
* @param application
*/
    public NewWindowAction(SshToolsApplication application) {
        this.application = application;
        putValue(Action.NAME, "New Window");
        putValue(Action.SMALL_ICON,
            getIcon("/com/sshtools/common/ui/newwindow.png"));
        putValue(LARGE_ICON,
            getIcon("/com/sshtools/common/ui/largenewwindow.png"));
        putValue(Action.SHORT_DESCRIPTION, "Create new window");
        putValue(Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.ALT_MASK));
        putValue(Action.LONG_DESCRIPTION, "Create a new SSHTerm window");
        putValue(Action.MNEMONIC_KEY, new Integer('w'));
        putValue(Action.ACTION_COMMAND_KEY, "new-window");
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_NAME, "File");
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(0));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(90));
        putValue(StandardAction.ON_TOOLBAR, new Boolean(true));
        putValue(StandardAction.TOOLBAR_GROUP, new Integer(0));
        putValue(StandardAction.TOOLBAR_WEIGHT, new Integer(90));
    }

    /**
*
*
* @param evt
*/
    public void actionPerformed(ActionEvent evt) {
        try {
            application.newContainer();
        } catch (SshToolsApplicationException stae) {
            log.error(stae);
        }
    }
}
