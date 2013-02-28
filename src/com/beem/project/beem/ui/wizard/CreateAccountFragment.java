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
package com.beem.project.beem.ui.wizard;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beem.project.beem.BeemApplication;
import com.beem.project.beem.R;
import com.beem.project.beem.ui.Login;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.util.StringUtils;


/**
 * Fragment used to create an account on an XMPP server.
 */
public class CreateAccountFragment extends Fragment implements android.view.View.OnClickListener {
    private static final String TAG = CreateAccountFragment.class.getSimpleName();
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private TextView errorText;
    private TextView mSettingsWarningLabel;
    private AutoCompleteTextView serverEdit;
    private Button createButton;
    private CreateAccountTask task;
    private final NotEmptyTextWatcher mTextWatcher = new NotEmptyTextWatcher();
    private SharedPreferences settings;

    /**
     * Create a CreateAccountFragment.
     *
     */
    public CreateAccountFragment() {
    }

    /**
     * Create a CreateAccountFragment.
     * @return a new CreateAccountFragment
     */
    public static CreateAccountFragment newInstance() {
	return new CreateAccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRetainInstance(true);
	settings =  PreferenceManager.getDefaultSharedPreferences(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.create_account, container, false);
	username = (EditText) v.findViewById(R.id.create_account_username);
	username.addTextChangedListener(mTextWatcher);
	password = (EditText) v.findViewById(R.id.create_account_password);
	password.addTextChangedListener(mTextWatcher);
	confirmPassword = (EditText) v.findViewById(R.id.create_account_confirm_password);
	confirmPassword.addTextChangedListener(mTextWatcher);
	errorText = (TextView) v.findViewById(R.id.error_label);
    	mSettingsWarningLabel = (TextView) v.findViewById(R.id.settings_warn_label);
	createButton = (Button) v.findViewById(R.id.next);
	createButton.setOnClickListener(this);
	serverEdit = (AutoCompleteTextView) v.findViewById(R.id.xmpp_server);
	ArrayAdapter<CharSequence> completeAdapter =  ArrayAdapter.createFromResource(
		getActivity(), R.array.xmpp_server_list, R.layout.simple_combobox_item);

	serverEdit.setAdapter(completeAdapter);
	serverEdit.addTextChangedListener(mTextWatcher);
	// show the list on second click on the text view
	serverEdit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		serverEdit.showDropDown();
	    }
	});
	return v;
    }

    @Override
    public void onStart() {
	super.onStart();
	if (settings.getBoolean(BeemApplication.PROXY_USE_KEY, false)) {
	    mSettingsWarningLabel.setVisibility(View.VISIBLE);
	} else
	    mSettingsWarningLabel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
	if (v == createButton) {
	    boolean create = true;
	    if (!checkUserName()) {
		username.setError(getString(R.string.create_account_err_username));
		create = false;
	    }
	    if (TextUtils.isEmpty(serverEdit.getText())) {
		serverEdit.setError("Choose a server");
		create = false;
	    }
	    if (!checkPasswords()) {
		password.setError(getString(R.string.create_account_err_passwords));
		confirmPassword.setError(getString(R.string.create_account_err_passwords));
		create = false;
	    }
	    if (create) {
		String jid = String.format("%s@%s", username.getText(), serverEdit.getText());
		jid = StringUtils.parseBareAddress(jid);
		String pass = password.getText().toString();
		task = new CreateAccountTask();
		task.execute(jid, pass);
	    }
	}
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
	edit.commit();
    }

    /**
     * Callback called when the account is successfully created.
     *
     * @param jid the jid of the account.
     * @param pass the password of the account.
     *
     */
    private void onAccountCreationSuccess(String jid, String pass) {
	Activity a = getActivity();
	saveCredential(jid, pass);
	// launch login
	Intent i = new Intent(a, Login.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);
	a.finish();
    }

    /**
     * Callback called when the account failed to create.
     *
     * @param e the exception which occurs
     *
     */
    private void onAccountCreationFailed(XMPPException e) {
	XMPPError error = e.getXMPPError();
	if (error != null && XMPPError.Condition.conflict.equals(error.getCondition()))
	    errorText.setText(R.string.create_account_err_conflict);
	else
	    errorText.setText(R.string.create_account_err_connection);
	Log.v(TAG, "Unable to create an account on xmpp server", e);
    }

    /**
     * Check the format of the email.
     *
     * @return true if the email is valid.
     */
    private boolean checkUserName() {
	String email = username.getText().toString();
	return Pattern.matches("[a-zA-Z0-9._%+-]+", email);
    }

    /**
     * Check if the fields password and confirm password match.
     *
     * @return return true if password & confirm password fields match, else
     *         false
     */
    private boolean checkPasswords() {
	CharSequence pass = password.getText();
	return !TextUtils.isEmpty(pass) && TextUtils.equals(pass, confirmPassword.getText());
    }

    /**
     * AsyncTask use to create an XMPP account on a server.
     */
    private class CreateAccountTask extends AsyncTask<String, Void, Boolean> {

	private ProgressFragment progress;
	private ConnectionConfiguration config;
	private XMPPException exception;
	private String jid;
	private String password;
	private String server;

	@Override
	protected void onPreExecute() {
	    progress = ProgressFragment.newInstance();
	    progress.show(getFragmentManager(), "progressFragment");
	}

	@Override
	protected void onPostExecute(Boolean result) {
	    if (result) {
		onAccountCreationSuccess(jid, password);
	    } else {
		onAccountCreationFailed(exception);
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
	    Log.d(TAG, "jid " + jid + " server  " + server);

	    int port = -1;
	    if (params.length > 2) {
		server = params[2];
	    }
	    if (params.length > 3) {
		if (!TextUtils.isEmpty(params[3])) {
		    port = Integer.parseInt(params[3]);
		}
	    }
	    Connection connection = prepareConnection(jid, server, port);
	    try {
		connection.connect();
		AccountManager accountManager = new AccountManager(connection);
		accountManager.createAccount(StringUtils.parseName(jid), password);
	    } catch (XMPPException e) {
		Log.e(TAG, "Unable to create account", e);
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
	 * @param server the server to use (not using dns srv) may be null
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
		    port = 5222;
		if (TextUtils.isEmpty(server))
		    server = serviceName;
		config = new ConnectionConfiguration(server, port, serviceName, proxyinfo);
	    } else {
		config = new ConnectionConfiguration(serviceName, proxyinfo);
	    }
	    if (settings.getBoolean(BeemApplication.SMACK_DEBUG_KEY, false))
		config.setDebuggerEnabled(true);
	    return new XMPPConnection(config);
	}
    }

    /**
     * A progress Fragment.
     */
    public static class ProgressFragment extends DialogFragment {

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
	    p.setTitle(getString(R.string.create_account_progress_title));
	    p.setMessage(getString(R.string.create_account_progress_message));
	    return p;
	}
    }

    /**
     * Text watcher to test if all fields are field.
     */
    private class NotEmptyTextWatcher implements TextWatcher {

	/**
	 * Constructor.
	 */
	public NotEmptyTextWatcher() {
	}

	@Override
	public void afterTextChanged(Editable s) {
	    boolean enable = !(TextUtils.isEmpty(username.getText())
		    || TextUtils.isEmpty(serverEdit.getText())
		    || TextUtils.isEmpty(password.getText())
		    || TextUtils.isEmpty(confirmPassword.getText()));
	    createButton.setEnabled(enable);
	}

	@Override
	public void beforeTextChanged(CharSequence  s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence  s, int start, int before, int count) {
	}
    }

}
