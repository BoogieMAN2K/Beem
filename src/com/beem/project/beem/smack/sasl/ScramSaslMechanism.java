/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009-2012 by Frederic-Charles Barthelery,
                               Nikita Kozlov,
                               Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://www.beem-project.com/

*/
package com.beem.project.beem.smack.sasl;

import java.io.IOException;


import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;

/**
 * An implementation of the SCRAM-SHA-1 SASL mechanism.
 * This implementation is based on Stroke http://swift.im/git/stroke.
 */
public class ScramSaslMechanism extends SASLMechanism {

    /**
     * The name of the SASL mechanism.
     */
    public static final String MECHANISM_NAME = "SCRAM-SHA-1";

    /**
     * Create a ScramSaslMechanism.
     *
     * @param saslAuthentication the smack SASLAuthentication.
     *
     */
    public ScramSaslMechanism(final SASLAuthentication saslAuthentication) {
	super(saslAuthentication);
    }

    @Override
    public void authenticate(String username, String host, String password) throws IOException, XMPPException {
	this.authenticationId = username;
	this.password = password;
	this.hostname = host;

	sc = new ScramSaslClient(username, this);
	authenticate();
    }

    @Override
    public void authenticate(String username, String host, CallbackHandler cbh) throws IOException, XMPPException {
	this.authenticationId = username;
	this.hostname = host;
	sc = new ScramSaslClient(username, cbh);
	authenticate();
    }

    @Override
    protected String getName() {
	return MECHANISM_NAME;
    }

}
