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
package com.sshtools.j2ssh.sftp;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.io.UnsignedInteger32;
import com.sshtools.j2ssh.io.UnsignedInteger64;
import com.sshtools.j2ssh.subsystem.SubsystemMessage;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SshFxpRead extends SubsystemMessage implements MessageRequestId {
    /**  */
    public static final int SSH_FXP_READ = 5;
    private UnsignedInteger32 id;
    private byte[] handle;
    private UnsignedInteger64 offset;
    private UnsignedInteger32 length;

    /**
     * Creates a new SshFxpRead object.
     *
     * @param id
     * @param handle
     * @param offset
     * @param length
     */
    public SshFxpRead(UnsignedInteger32 id, byte[] handle,
        UnsignedInteger64 offset, UnsignedInteger32 length) {
        super(SSH_FXP_READ);
        this.id = id;
        this.handle = handle;
        this.offset = offset;
        this.length = length;
    }

    /**
     * Creates a new SshFxpRead object.
     */
    public SshFxpRead() {
        super(SSH_FXP_READ);
    }

    /**
     *
     *
     * @return
     */
    public UnsignedInteger32 getId() {
        return id;
    }

    /**
     *
     *
     * @return
     */
    public byte[] getHandle() {
        return handle;
    }

    /**
     *
     *
     * @return
     */
    public UnsignedInteger64 getOffset() {
        return offset;
    }

    /**
     *
     *
     * @return
     */
    public UnsignedInteger32 getLength() {
        return length;
    }

    /**
     *
     *
     * @param bar
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     */
    public void constructMessage(ByteArrayReader bar)
        throws java.io.IOException, 
            com.sshtools.j2ssh.transport.InvalidMessageException {
        id = bar.readUINT32();
        handle = bar.readBinaryString();
        offset = bar.readUINT64();
        length = bar.readUINT32();
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_FXP_READ";
    }

    /**
     *
     *
     * @param baw
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     */
    public void constructByteArray(ByteArrayWriter baw)
        throws java.io.IOException, 
            com.sshtools.j2ssh.transport.InvalidMessageException {
        baw.writeUINT32(id);
        baw.writeBinaryString(handle);
        baw.writeUINT64(offset);
        baw.writeUINT32(length);
    }
}
