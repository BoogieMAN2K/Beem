/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
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
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

*/
package com.beem.project.beem.ui.wizard;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beem.project.beem.BeemApplication;
import com.beem.project.beem.R;
import com.beem.project.beem.ui.Login;
import com.beem.project.beem.ui.Settings;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Fragment to enter the information required in order to configure a XMPP account.
 *
 */
public class AccountConfigureFragment extends Fragment implements OnClickListener {

    private static final String TAG = AccountConfigureFragment.class.getSimpleName();

    private static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    private static final int SELECT_ACCOUNT_CODE = 1;
    private static final int MANUAL_CONFIGURATION_CODE = 2;

    private Button mNextButton;
    private Button mManualConfigButton;
    private Button mSelectAccountButton;
    private TextView mErrorLabel;
    private TextView mSettingsWarningLabel;
    private EditText mAccountJID;
    private EditText mAccountPassword;
    private final JidTextWatcher mJidTextWatcher = new JidTextWatcher();
    private final PasswordTextWatcher mPasswordTextWatcher = new PasswordTextWatcher();
    private boolean mValidJid;
    private boolean mValidPassword;
    private String mSelectedAccountName;
    private String mSelectedAccountType;
    private SharedPreferences settings;
    private boolean useSystemAccount;

    private com.beem.project.beem.ui.wizard.AccountConfigureFragment.ConnectionTestTask task;

    /**
     * Create a new AccountConfigureFragment.
     *
     * @return a new AccountConfigureFragment
     */
    public static AccountConfigureFragment newInstance() {
    	return new AccountConfigureFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d(TAG, "onCreate");
    	setRetainInstance(true);

    	settings =  PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.wizard_account_configure, container, false);
    	mManualConfigButton = (Button) v.findViewById(R.id.manual_setup);
    	mManualConfigButton.setOnClickListener(this);
    	mNextButton = (Button) v.findViewById(R.id.next);
    	mNextButton.setOnClickListener(this);
    	mSelectAccountButton = (Button) v.findViewById(R.id.select_account_btn);
    	mSelectAccountButton.setOnClickListener(this);
    	mErrorLabel = (TextView) v.findViewById(R.id.error_label);
    	mSettingsWarningLabel = (TextView) v.findViewById(R.id.settings_warn_label);
    	mAccountJID = (EditText) v.findViewById(R.id.account_username);
    	mAccountPassword = (EditText) v.findViewById(R.id.account_password);
    	InputFilter[] orgFilters = mAccountJID.getFilters();
    	InputFilter[] newFilters = new InputFilter[orgFilters.length + 1];
    	int i;
    	for (i = 0; i < orgFilters.length; i++)
    	    newFilters[i] = orgFilters[i];
    	newFilters[i] = new LoginFilter.UsernameFilterGeneric();
    	mAccountJID.setFilters(newFilters);
    	mAccountJID.addTextChangedListener(mJidTextWatcher);
    	mAccountPassword.addTextChangedListener(mPasswordTextWatcher);
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) // true to disable the feature until ready
    		v.findViewById(R.id.account_layout).setVisibility(View.GONE);
    	return v;
    }

    @Override
    public void onStart() {
	super.onStart();
	useSystemAccount = settings.getBoolean(BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
	// temporaly disable jid watcher
	mAccountJID.removeTextChangedListener(mJidTextWatcher);
	mAccountJID.setText(settings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, ""));
	if (useSystemAccount) {
	    mAccountPassword.setText("*******"); //dummy password
	    mAccountJID.setText(settings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, ""));
	    mAccountPassword.setEnabled(false);
	    mNextButton.setEnabled(true);
	}
	mAccountJID.addTextChangedListener(mJidTextWatcher);
	if (settings.getBoolean(BeemApplication.ACCOUNT_SPECIFIC_SERVER_KEY, false)
	    || settings.getBoolean(BeemApplication.PROXY_USE_KEY, false)) {
	    mSettingsWarningLabel.setVisibility(View.VISIBLE);
	} else
	    mSettingsWarningLabel.setVisibility(View.GONE);

    }

    @TargetApi(14)
    @Override
    public void onClick(View v) {
	if (v == mNextButton) {
	    if (useSystemAccount) {
		onDeviceAccountSelected(settings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, ""),
			settings.getString(BeemApplication.ACCOUNT_SYSTEM_TYPE_KEY, ""));
	    } else {
		String jid = mAccountJID.getText().toString();
		jid = StringUtils.parseBareAddress(jid);
		String password = mAccountPassword.getText().toString();
		task = new ConnectionTestTask();
		if (settings.getBoolean(BeemApplication.ACCOUNT_SPECIFIC_SERVER_KEY, false)) {
		    String server = settings.getString(BeemApplication.ACCOUNT_SPECIFIC_SERVER_HOST_KEY, "");
		    String port = settings.getString(BeemApplication.ACCOUNT_SPECIFIC_SERVER_PORT_KEY, "5222");
		    task.execute(jid, password, server, port);
		} else
		    task.execute(jid, password);
	    }
	} else if (v == mManualConfigButton) {
	    onManualConfigurationSelected();
	} else if (v == mSelectAccountButton) {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		Intent i = AccountManager.newChooseAccountIntent(null, null,
			new String[] {GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
		startActivityForResult(i, SELECT_ACCOUNT_CODE);
	    }
	}
    }

    /**
     * Callback called when the Manual configuration button is selected.
     *
     */
    public void onManualConfigurationSelected() {
	Intent i = new Intent(getActivity(), Settings.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivityForResult(i, MANUAL_CONFIGURATION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == SELECT_ACCOUNT_CODE && resultCode == Activity.RESULT_OK) {
	    mSelectedAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	    mSelectedAccountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
	    onDeviceAccountSelected(mSelectedAccountName, mSelectedAccountType);
	} else if (requestCode == MANUAL_CONFIGURATION_CODE) {
	    String login = settings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, "");
	    String password = settings.getString(BeemApplication.ACCOUNT_PASSWORD_KEY, "");
	    mAccountJID.setText(login);
	    mAccountPassword.setText(password);
	} else {
	    super.onActivityResult(requestCode, resultCode, data);
	}
    }

    /**
     * Callback called when the account was connected successfully.
     *
     * @param jid the jid used to connect
     * @param password the password used to connect
     *
     */
    private void onAccountConnectionSuccess(String jid, String password) {
	Activity a = getActivity();
	saveCredential(jid, password);
	// launch login
	Intent i = new Intent(a, Login.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);
	a.finish();
    }

    /**
     * Callback called when the account connection failed.
     *
     */
    private void onAccountConnectionFailed() {
	mAccountPassword.setText("");
	mErrorLabel.setVisibility(View.VISIBLE);
    }

    /**
     * Callback called when the user select an account from the device (Android Account api).
     *
     * @param accountName the account name
     * @param accountType the account type
     *
     */
    private void onDeviceAccountSelected(String accountName, String accountType) {
	Activity a = getActivity();
	saveCredentialAccount(accountName, accountType);
	// launch login
	Intent i = new Intent(a, Login.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);
	a.finish();
    }

    /**
     * Save the user credentials.
     *
     * @param jid the jid of the user
     * @param pass the password of the user
     *
     */
    private void saveCredential(String jid, String pass) {
	SharedPreferences.Editor edit = settings.edit();
	edit.putString(BeemApplication.ACCOUNT_USERNAME_KEY, jid);
	edit.putString(BeemApplication.ACCOUNT_PASSWORD_KEY, pass);
	edit.putBoolean(BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
	edit.commit();
    }

    /**
     * Save the user credentials.
     *
     * @param accountName the account name of the user
     * @param accountType the account type of the user
     *
     */
    private void saveCredentialAccount(String accountName, String accountType) {
	SharedPreferences.Editor edit = settings.edit();
	edit.putString(BeemApplication.ACCOUNT_USERNAME_KEY, accountName);
	edit.putString(BeemApplication.ACCOUNT_SYSTEM_TYPE_KEY, accountType);
	edit.putBoolean(BeemApplication.USE_SYSTEM_ACCOUNT_KEY, true);
	edit.commit();
    }

    /**
     * Check that the username is really a JID.
     * @param username the username to check.
     */
    private void checkUsername(String username) {
	String name = StringUtils.parseName(username);
	String server = StringUtils.parseServer(username);
	if (TextUtils.isEmpty(name) || TextUtils.isEmpty(server)) {
	    mValidJid = false;
	} else {
	    mValidJid = true;
	}
    }

    /**
     * Check password.
     * @param password the password to check.
     */
    private void checkPassword(String password) {
	if (password.length() > 0)
	    mValidPassword = true;
	else
	    mValidPassword = false;
    }

    /**
     * Text watcher to test the existence of a password.
     */
    private class PasswordTextWatcher implements TextWatcher {

	/**
	 * Constructor.
	 */
	public PasswordTextWatcher() {
	}

	@Override
	public void afterTextChanged(Editable s) {
	    checkPassword(s.toString());
	    mNextButton.setEnabled(mValidJid && mValidPassword);
	}

	@Override
	public void beforeTextChanged(CharSequence  s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence  s, int start, int before, int count) {
	}
    }

    /**
     * TextWatcher to check the validity of a JID.
     */
    private class JidTextWatcher implements TextWatcher {

	/**
	 * Constructor.
	 */
	public JidTextWatcher() {
	}

	@Override
	public void afterTextChanged(Editable s) {
	    checkUsername(s.toString());
	    mNextButton.setEnabled(mValidJid && mValidPassword);
	    if (useSystemAccount) {
		mAccountPassword.setEnabled(true);
	    	mAccountPassword.setText("");
	    }
	    useSystemAccount = false;
	}

	@Override
	public void beforeTextChanged(CharSequence  s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence  s, int start, int before, int count) {
	}
    }

    /**
     * AsyncTask use to test the credentials.
     */
    class ConnectionTestTask extends AsyncTask<String, Void, Boolean> {

	private ProgressFragment progress;
	private ConnectionConfiguration config;
	private XMPPException exception;
	private String jid;
	private String password;
	private String server;

	@Override
	protected void onPreExecute() {
	    mErrorLabel.setVisibility(View.INVISIBLE);
	    progress = ProgressFragment.newInstance();
	    progress.show(getFragmentManager(), "progressFragment");
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    if (result) {
		onAccountConnectionSuccess(jid, password);
	    } else {
		onAccountConnectionFailed();
	    }
	    ProgressFragment pf = (ProgressFragment) getFragmentManager().findFragmentByTag("progressFragment");
	    if (pf != null)
		pf.dismiss();
	}

	@Override
	protected Boolean doInBackground(String... params) {
	    Log.d(TAG, "Xmpp login task");
	    jid = params[0];
	    password = params[1];

	    int port = -1;
	    if (params.length > 2) {
		server = params[2];
	    }
	    if (params.length > 3) {
		if (!TextUtils.isEmpty(params[3])) {
		    port = Integer.parseInt(params[3]);
		}
	    }
	    Log.d(TAG, "jid " + jid + " server  " + server + " port " + port);
	    String login = StringUtils.parseName(jid);
	    String serviceName = StringUtils.parseServer(jid);
	    Connection connection = prepareConnection(jid, server, port);
	    if (settings.getBoolean(BeemApplication.FULL_JID_LOGIN_KEY, false)
		|| "gmail.com".equals(serviceName)
		|| "googlemail.com".equals(serviceName)) {
		login = jid;
	    }
	    try {
		connection.connect();
		connection.login(login, password);
	    } catch (XMPPException e) {
		Log.e(TAG, "Unable to connect to Xmpp server", e);
		exception = e;
		return false;
	    } finally {
		connection.disconnect();
	    }
	    return true;
	}

	/**
	 * Initialize the XMPP connection.
	 *
	 * @param jid the jid to use
	 * @param server the server to use (not using dns srv)  may be null
	 * @param port the port
	 *
	 * @return the XMPPConnection prepared to connect
	 */
	private Connection prepareConnection(String jid, String server, int port) {
	    boolean useProxy = settings.getBoolean(BeemApplication.PROXY_USE_KEY, false);
	    ProxyInfo proxyinfo = ProxyInfo.forNoProxy();
	    if (useProxy) {
		String stype = settings.getString(BeemApplication.PROXY_TYPE_KEY, "HTTP");
		String phost = settings.getString(BeemApplication.PROXY_SERVER_KEY, "");
		String puser = settings.getString(BeemApplication.PROXY_USERNAME_KEY, "");
		String ppass = settings.getString(BeemApplication.PROXY_PASSWORD_KEY, "");
		int pport = Integer.parseInt(settings.getString(BeemApplication.PROXY_PORT_KEY, "1080"));
		ProxyInfo.ProxyType type = ProxyType.valueOf(stype);
		proxyinfo = new ProxyInfo(type, phost, pport, puser, ppass);
	    }
	    String serviceName = StringUtils.parseServer(jid);
	    if (port != -1 || !TextUtils.isEmpty(server)) {
		if (port == -1)
		    port  = 5222;
		if (TextUtils.isEmpty(server))
		    server = serviceName;
		config = new ConnectionConfiguration(server, port, serviceName, proxyinfo);
	    } else {
		config = new ConnectionConfiguration(serviceName, proxyinfo);
	    }
	    if (settings.getBoolean(BeemApplication.SMACK_DEBUG_KEY, false))
		config.setDebuggerEnabled(true);
	    config.setSendPresence(false);
	    config.setRosterLoadedAtLogin(false);
	    return new XMPPConnection(config);
	}
    }

    /**
     * A progress Fragment.
     */
    public static class ProgressFragment extends DialogFragment	 {

	/**
	 * Create a new ProgressFragment.
	 *
	 * @return a ProgressFragment
	 */
	public static ProgressFragment newInstance() {
	    return new ProgressFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setCancelable(false);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    Log.d(TAG, "create progress dialog");
	    ProgressDialog p = new ProgressDialog(getActivity());
	    p.setTitle(getString(R.string.login_login_progress));
	    p.setMessage(getString(R.string.create_account_progress_message));
	    return p;
	}
    }
}
