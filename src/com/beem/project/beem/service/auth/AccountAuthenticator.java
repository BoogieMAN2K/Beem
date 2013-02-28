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
package com.beem.project.beem.service.auth;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.util.Log;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.NameCallback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.apache.harmony.javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.harmony.javax.security.sasl.RealmCallback;
import org.jivesoftware.smack.util.StringUtils;

/**
 * The AccountAuthenticator use an Android Account to authenticate.
 */
public class AccountAuthenticator implements CallbackHandler {
    private static final String GOOGLE_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/googletalk";
    private static final String TAG = AccountAuthenticator.class.getSimpleName();

    private AccountManager accountMgr;
    private Account account;

    /**
     * Create an AccountAuthenticator.
     *
     * @param context the Android context
     * @param account the account to use
     *
     */
    public AccountAuthenticator(final Context context, final Account account) {
	accountMgr = AccountManager.get(context);
	this.account = account;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
	String tmpJid = account.name;
	String service = StringUtils.parseServer(tmpJid);

	for (int i = 0; i < callbacks.length; i++) {
	    if (callbacks[i] instanceof NameCallback) {
		String authenticationId = StringUtils.parseName(tmpJid);
		if (useFullJid(account)) {
		    authenticationId = tmpJid;
		}
		NameCallback ncb = (NameCallback) callbacks[i];
		ncb.setName(authenticationId);
	    } else if (callbacks[i] instanceof PasswordCallback) {
		PasswordCallback pcb = (PasswordCallback) callbacks[i];
		// skip if password is asked for PKCS11 (SSL keystore)
		String prompt = pcb.getPrompt();
		if (prompt != null && prompt.startsWith("PKCS11 Password:"))
		    continue;
		String password;
		if (useToken(account))
		    password = getToken();
		else
		    password = accountMgr.getPassword(account);
		if (password == null)
		    password = "";
		pcb.setPassword(password.toCharArray());
	    } else if (callbacks[i] instanceof RealmCallback) {
		RealmCallback rcb = (RealmCallback) callbacks[i];
		rcb.setText(service);
	    } else {
		throw new UnsupportedCallbackException(callbacks[i]);
	    }
	}
    }

    /**
     * Test if the accout use the full jid to authenticate.
     *
     * @param accountt the account to test
     *
     * @return true if the account use full jid false otherwise
     */
    private boolean useFullJid(Account accountt) {
	String type = accountt.type;
	return "com.google".equals(type);
    }

    /**
     * Test if the account use authentication token.
     *
     * @param accountt the account to test
     *
     * @return true if the account use token false otherwise
     */
    private boolean useToken(Account accountt) {
	String type = accountt.type;
	return "com.google".equals(type);
    }

    /**
     * Get a authentication token from the Account.
     *
     * @return the token or en empty string if an error occurs
     */
    private String getToken() {
	try {
	    return accountMgr.blockingGetAuthToken(account, GOOGLE_TOKEN_TYPE, true);
	} catch (OperationCanceledException e) {
	    Log.v(TAG, "Token request canceled", e);
	} catch (AuthenticatorException e) {
	    Log.d(TAG, "Unable to get token", e);
	} catch (IOException e) {
	    Log.d(TAG, "Unable to get token", e);
	}
	return "";
    }
}
