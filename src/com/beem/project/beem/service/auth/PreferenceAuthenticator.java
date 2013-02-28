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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.beem.project.beem.BeemApplication;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.NameCallback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.apache.harmony.javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.harmony.javax.security.sasl.RealmCallback;
import org.jivesoftware.smack.util.StringUtils;

/**
 * An Authenticator which look for credentials stored in preferences.
 */
public class PreferenceAuthenticator implements CallbackHandler {

    private final SharedPreferences settings;

    /**
     * Create a PreferenceAuthenticator.
     *
     * @param context the Android context.
     */
    public PreferenceAuthenticator(final Context context) {
	settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
	String tmpJid = settings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, "").trim();
	String service = StringUtils.parseServer(tmpJid);

	for (int i = 0; i < callbacks.length; i++) {
	    if (callbacks[i] instanceof NameCallback) {
		String authenticationId = StringUtils.parseName(tmpJid);
		if (settings.getBoolean(BeemApplication.FULL_JID_LOGIN_KEY, false)
			|| "gmail.com".equals(service) || "googlemail.com".equals(service)) {
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
		String password = settings.getString(BeemApplication.ACCOUNT_PASSWORD_KEY, "");
		pcb.setPassword(password.toCharArray());
	    } else if (callbacks[i] instanceof RealmCallback) {
		RealmCallback rcb = (RealmCallback) callbacks[i];
		rcb.setText(service);
	    } else {
		throw new UnsupportedCallbackException(callbacks[i]);
	    }
	}
    }

}
