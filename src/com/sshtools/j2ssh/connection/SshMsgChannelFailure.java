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
package com.sshtools.j2ssh.connection;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.transport.InvalidMessageException;
import com.sshtools.j2ssh.transport.SshMessage;

import java.io.IOException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SshMsgChannelFailure extends SshMessage {
    /**  */
    protected final static int SSH_MSG_CHANNEL_FAILURE = 100;
    private long channelId;

    /**
     * Creates a new SshMsgChannelFailure object.
     *
     * @param recipientChannelId
     */
    public SshMsgChannelFailure(long recipientChannelId) {
        super(SSH_MSG_CHANNEL_FAILURE);
        channelId = recipientChannelId;
    }

    /**
     * Creates a new SshMsgChannelFailure object.
     */
    public SshMsgChannelFailure() {
        super(SSH_MSG_CHANNEL_FAILURE);
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_MSG_CHANNEL_FAILURE";
    }

    /**
     *
     *
     * @return
     */
    public long getRecipientChannelId() {
        return channelId;
    }

    /**
     *
     *
     * @param baw
     *
     * @throws InvalidMessageException
     */
    protected void constructByteArray(ByteArrayWriter baw)
        throws InvalidMessageException {
        try {
            baw.writeInt(channelId);
        } catch (IOException ioe) {
            throw new InvalidMessageException("Invalid message data");
        }
    }

    /**
     *
     *
     * @param bar
     *
     * @throws InvalidMessageException
     */
    protected void constructMessage(ByteArrayReader bar)
        throws InvalidMessageException {
        try {
            channelId = bar.readInt();
        } catch (IOException ioe) {
            throw new InvalidMessageException("Invalid message data");
        }
    }
}
