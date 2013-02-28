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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.isode.stroke.base.ByteArray;
import com.isode.stroke.sasl.SCRAMSHA1ClientAuthenticator;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.NameCallback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.apache.harmony.javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.harmony.javax.security.sasl.SaslClient;
import org.apache.harmony.javax.security.sasl.SaslException;

/**
 * A SaslClient which uses the SCRAM-SHA-! mechanism.
 * This implementation is based on Stroke (http://swift.im/git/stroke)
 */
public class ScramSaslClient implements SaslClient {

    private static final int   NONCE_BYTE_COUNT = 32;
    private static final int   NONCE_HEX_COUNT = 2 * NONCE_BYTE_COUNT;
    private static final char[] hexChars = {
	'0', '1', '2', '3', '4', '5', '6', '7',
	'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private SCRAMSHA1ClientAuthenticator clientAuthenticator;
    private CallbackHandler cbh;
    private String authzid;

    /**
     * Create a ScramSaslClient.
     * @param authorizationId the authorizationId uses by the client
     * @param cbh a CallbackHandler to get more informations
     */
    public ScramSaslClient(final String authorizationId, final CallbackHandler cbh) {
	this.cbh = cbh;
	this.authzid = authorizationId;
    }

    /**
     * Disposes of any system resources or security-sensitive information the
     * SaslClient might be using. Invoking this method invalidates the
     * SaslClient instance. This method is idempotent.
     *
     * @exception SaslException  if a problem was encountered while disposing
     *                           of the resources
     */
    @Override
    public void dispose() throws SaslException {
    }

    /**
     * Evaluates the challenge data and generates a response. If a challenge
     * is received from the server during the authentication process, this
     * method is called to prepare an appropriate next response to submit to
     * the server.
     *
     * @param challenge  The non-null challenge sent from the server. The
     *                   challenge array may have zero length.
     *
     * @return    The possibly null reponse to send to the server. It is null
     *            if the challenge accompanied a "SUCCESS" status and the
     *            challenge only contains data for the client to update its
     *            state and no response needs to be sent to the server.
     *            The response is a zero-length byte array if the client is to
     *            send a response with no data.
     *
     * @exception SaslException   If an error occurred while processing the
     *                            challenge or generating a response.
     */
    @Override
    public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
	if (clientAuthenticator == null) {
	    Object[] userInfo = getUserInfo();
	    String authcid = (String) userInfo[0];
	    byte[] passwdBytes = (byte[]) userInfo[1];
	    String passwd = new String(passwdBytes);
	    String nonce = getClientNonce();
	    clientAuthenticator = new SCRAMSHA1ClientAuthenticator(nonce);
	    clientAuthenticator.setCredentials(authcid, passwd, authzid);
	    return clientAuthenticator.getResponse().getData();
	}
	clientAuthenticator.setChallenge(new ByteArray(challenge));
	return clientAuthenticator.getResponse().getData();
    }

    /**
     * Returns the IANA-registered mechanism name of this SASL client.
     *  (e.g. "CRAM-MD5", "GSSAPI")
     *
     * @return  "SCRAM-SHA-!" the IANA-registered mechanism name of this SASL
     *          client.
     */
    @Override
    public String getMechanismName() {
	return "SCRAM-SHA-1";
    }

    /**
     * Retrieves the negotiated property. This method can be called only after
     * the authentication exchange has completed (i.e., when isComplete()
     * returns true); otherwise, an IllegalStateException is thrown.
     *
     *
     * @param propName   The non-null property name
     *
     * @return  The value of the negotiated property. If null, the property was
     *          not negotiated or is not applicable to this mechanism. This
     *          implementation allways returns null.
     *
     * @exception IllegalStateException   if this authentication exchange has
     *                                    not completed
     */
    @Override
    public Object getNegotiatedProperty(String propName) {
	return null;
    }

    /**
     * Determines if this mechanism has an optional initial response. If true,
     * caller should call evaluateChallenge() with an empty array to get the
     * initial response.
     *
     * @return  true if this mechanism has an initial response. This
     * 		implementation always return true.
     */
    @Override
    public boolean hasInitialResponse() {
	return true;
    }

    /**
     * Determines if the authentication exchange has completed. This method
     * may be called at any time, but typically, it will not be called until
     * the caller has received indication from the server (in a protocol-
     * specific manner) that the exchange has completed.
     *
     * @return  true if the authentication exchange has completed;
     *           false otherwise.
     */
    @Override
    public boolean isComplete() {
	if (clientAuthenticator == null)
	    return false;
	return clientAuthenticator.getResponse() == null;
    }

    /**
     * Unwraps a byte array received from the server. This method can be called
     * only after the authentication exchange has completed (i.e., when
     * isComplete() returns true) and only if the authentication exchange has
     * negotiated integrity and/or privacy as the quality of protection;
     * otherwise, an IllegalStateException is thrown.
     *
     * This implementation always throw an IllegalStateException.
     *
     * incoming is the contents of the SASL buffer as defined in RFC 2222
     * without the leading four octet field that represents the length.
     * offset and len specify the portion of incoming to use.
     *
     * @param incoming   A non-null byte array containing the encoded bytes
     *                   from the server
     * @param offset     The starting position at incoming of the bytes to use
     *
     * @param len        The number of bytes from incoming to use
     *
     * @return           A non-null byte array containing the decoded bytes
     * @throws SaslException if an error occurs
     *
     */
    @Override
    public byte[] unwrap(byte[] incoming, int offset, int len) throws SaslException {
	throw new IllegalStateException("SCRAM-SHA-1: this mechanism supports "
		+ "neither integrity nor privacy");
    }

    /**
     * Wraps a byte array to be sent to the server. This method can be called
     * only after the authentication exchange has completed (i.e., when
     * isComplete() returns true) and only if the authentication exchange has
     * negotiated integrity and/or privacy as the quality of protection;
     * otherwise, an IllegalStateException is thrown.
     *
     * This implementation always throw an IllegalStateException.
     *
     * The result of this method will make up the contents of the SASL buffer as
     * defined in RFC 2222 without the leading four octet field that represents
     * the length. offset and len specify the portion of outgoing to use.
     *
     * @param outgoing   A non-null byte array containing the bytes to encode
     * @param offset     The starting position at outgoing of the bytes to use
     * @param len        The number of bytes from outgoing to use
     *
     * @return A non-null byte array containing the encoded bytes
     *
     * @exception SaslException  if incoming cannot be successfully unwrapped.
     *
     * @exception IllegalStateException   if the authentication exchange has
     *                   not completed, or if the negotiated quality of
     *                   protection has neither integrity nor privacy.
     */
    @Override
    public byte[] wrap(byte[] outgoing, int offset, int len) throws SaslException {
	throw new IllegalStateException("SCRAM-SHA-1: this mechanism supports "
		+ "neither integrity nor privacy");
    }


    /**
     * Calculates the Nonce value of the Client.
     *
     * @return   Nonce value of the client
     *
     * @exception   SaslException If an error Occurs
     */
    private String getClientNonce() throws SaslException {
        byte[]          nonceBytes = new byte[NONCE_BYTE_COUNT];
        SecureRandom    prng;
        char[]          hexNonce = new char[NONCE_HEX_COUNT];

        try {
            prng = SecureRandom.getInstance("SHA1PRNG");
            prng.nextBytes(nonceBytes);
            for (int i = 0; i < NONCE_BYTE_COUNT; i++) {
                final int val = nonceBytes[i] & 0xff;
                //low nibble
                hexNonce[i * 2] = hexChars[val / 0x10];
                //high nibble
                hexNonce[(i * 2) + 1] = hexChars[val % 0x10];
            }
            return new String(hexNonce);
        } catch (NoSuchAlgorithmException e) {
            throw new SaslException("No random number generator available", e);
        }
    }

    /**
     * Get informations supplied by the user of the SaslClient.
     * These informations are retrived by using the CallbackHandler.
     *
     * @return an array of object
     * @throws SaslException if the informations cannot be retrieved
     */
    private Object[] getUserInfo() throws SaslException {
        try {
            final String userPrompt = "Authentication id: ";
            final String pwPrompt = "Password: ";
            NameCallback nameCb = new NameCallback(userPrompt);
            PasswordCallback passwordCb = new PasswordCallback(pwPrompt, false);
            cbh.handle(new Callback[] {nameCb, passwordCb });
            String userid = nameCb.getName();
            char[] pwchars = passwordCb.getPassword();
            byte[] pwbytes;
            if (pwchars != null) {
                pwbytes = (new String(pwchars)).getBytes("UTF8");
                passwordCb.clearPassword();
            } else {
                pwbytes = null;
            }
            return new Object[] {userid, pwbytes };
        } catch (IOException e) {
            throw new SaslException("Cannot get password", e);
        } catch (UnsupportedCallbackException e) {
            throw new SaslException("Cannot get userid/password", e);
        }
    }
}
