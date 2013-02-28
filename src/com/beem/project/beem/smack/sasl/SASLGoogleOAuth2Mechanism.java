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
import java.util.HashMap;
import java.util.Map;

import de.measite.smack.Sasl;

import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.Base64;

/**
 * An implementation of the SASL OAuth2 mechanism made by Google.
 * The extension is described here :
 * https://developers.google.com/talk/jep_extensions/oauth
 */
public class SASLGoogleOAuth2Mechanism extends SASLMechanism {

    /**
     * The name of the Google Oauth mechanism.
     */
    public static final String MECHANISM_NAME = "X-OAUTH2";

    /**
     * Create a SASLGoogleOAuth2Mechanism.
     *
     * @param saslAuthentication the smack SASLAuthentication.
     *
     */
    public SASLGoogleOAuth2Mechanism(final SASLAuthentication saslAuthentication) {
	super(saslAuthentication);
    }

    @Override
    public void authenticate(String username, String host, String password) throws IOException, XMPPException {
	//Since we were not provided with a CallbackHandler, we will use our own with the given
	//information

	//Set the authenticationID as the username, since they must be the same in this case.
	this.authenticationId = username;
	this.password = password;
	this.hostname = host;

	String[] mechanisms = {"PLAIN" };
	Map<String, String> props = new HashMap<String, String>();
	sc = Sasl.createSaslClient(mechanisms, username, "xmpp", host, props, this);
	authenticate();
    }

    @Override
    public void authenticate(String username, String host, CallbackHandler cbh) throws IOException, XMPPException {
	String[] mechanisms = {"PLAIN" };
	Map<String, String> props = new HashMap<String, String>();
	sc = Sasl.createSaslClient(mechanisms, username, "xmpp", host, props, cbh);
	authenticate();
    }

    @Override
    protected void authenticate() throws IOException, XMPPException {
	String authenticationText = null;
	try {
	    if (sc.hasInitialResponse()) {
		byte[] response = sc.evaluateChallenge(new byte[0]);
		authenticationText = Base64.encodeBytes(response, Base64.DONT_BREAK_LINES);
	    }
	} catch (SaslException e) {
	    throw new XMPPException("SASL authentication failed", e);
	}

	// Send the authentication to the server
	getSASLAuthentication().send(new GoogleOAuthMechanism(authenticationText));
    }

    @Override
    protected String getName() {
	return MECHANISM_NAME;
    }

    /**
     * Initiating SASL authentication by select a mechanism.
     */
    public static class GoogleOAuthMechanism extends Packet {
	private final String authenticationText;

	/**
	 * Create a GoogleOAuthMechanism.
	 *
	 * @param authenticationText the authentification token
	 *
	 */
	public GoogleOAuthMechanism(final String authenticationText) {
	    this.authenticationText = authenticationText;
	}

	@Override
	public String toXML() {
	    StringBuilder stanza = new StringBuilder();
	    stanza.append("<auth mechanism=\"").append(MECHANISM_NAME);
	    stanza.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\" "
		    + "auth:service=\"oauth2\" "
		    + "xmlns:auth=\"http://www.google.com/talk/protocol/auth\">");
	    if (authenticationText != null
		    && authenticationText.trim().length() > 0) {
		stanza.append(authenticationText);
	    }
	    stanza.append("</auth>");
	    return stanza.toString();
	}
    }
}
