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
package com.beem.project.beem;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.beem.project.beem.service.XmppConnectionAdapter;
import com.beem.project.beem.service.XmppFacade;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.service.auth.AccountAuthenticator;
import com.beem.project.beem.service.auth.PreferenceAuthenticator;
import com.beem.project.beem.smack.avatar.AvatarMetadataProvider;
import com.beem.project.beem.smack.avatar.AvatarProvider;
import com.beem.project.beem.smack.ping.PingExtension;
import com.beem.project.beem.smack.sasl.SASLGoogleOAuth2Mechanism;
import com.beem.project.beem.smack.sasl.ScramSaslMechanism;
import com.beem.project.beem.utils.BeemBroadcastReceiver;
import com.beem.project.beem.utils.BeemConnectivity;
import com.beem.project.beem.utils.Status;

import de.duenndns.ssl.MemorizingTrustManager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.entitycaps.SimpleDirectoryPersistentCache;
import org.jivesoftware.smackx.entitycaps.packet.CapsExtension;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.provider.CapsExtensionProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;

/**
 * This class is for the Beem service.
 * It must contains every global informations needed to maintain the background service.
 * The connection to the xmpp server will be made asynchronously when the service
 * will start.
 * @author darisk
 */
public class BeemService extends Service {

    /** The id to use for status notification. */
    public static final int NOTIFICATION_STATUS_ID = 100;

    private static final String TAG = "BeemService";
    private static final int DEFAULT_XMPP_PORT = 5222;

    private NotificationManager mNotificationManager;
    private XmppConnectionAdapter mConnection;
    private SharedPreferences mSettings;
    private String mLogin;
    private String mHost;
    private String mService;
    private int mPort;
    private ConnectionConfiguration mConnectionConfiguration;
    private IXmppFacade.Stub mBind;

    private BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver();
    private BeemServiceBroadcastReceiver mOnOffReceiver = new BeemServiceBroadcastReceiver();
    private BeemServicePreferenceListener mPreferenceListener = new BeemServicePreferenceListener();

    private boolean mOnOffReceiverIsRegistered;

    private SSLContext sslContext;

    /**
     * Constructor.
     */
    public BeemService() {
    }

    /**
     * Initialize the connection.
     */
    private void initConnectionConfig() {
	// TODO add an option for this ?
//         SmackConfiguration.setPacketReplyTimeout(30000);
	ProxyInfo proxyInfo = getProxyConfiguration();
	boolean useSystemAccount = mSettings.getBoolean(BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
	if (useSystemAccount) {
	    // when using system account, using SPECIFIC_SERVER settings is not a supported configuration.
	    SASLAuthentication.supportSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME);
	    String accountType = mSettings.getString(BeemApplication.ACCOUNT_SYSTEM_TYPE_KEY, "");
	    String accountName = mSettings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, "");
	    Account account = getAccount(accountName, accountType);
	    if (account == null) {
		mSettings.edit().putString(BeemApplication.ACCOUNT_USERNAME_KEY, "").commit();
	    } else
		mConnectionConfiguration = getConnectionConfigurationForAccount(account, proxyInfo);
	}
	if (!useSystemAccount || mConnectionConfiguration == null) {
	    SASLAuthentication.unsupportSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME);
	    if (mSettings.getBoolean(BeemApplication.ACCOUNT_SPECIFIC_SERVER_KEY, false))
		mConnectionConfiguration = new ConnectionConfiguration(mHost, mPort, mService, proxyInfo);
	    else
		mConnectionConfiguration = new ConnectionConfiguration(mService, proxyInfo);
	    mConnectionConfiguration.setCallbackHandler(new PreferenceAuthenticator(this));
	}

	if (mSettings.getBoolean("settings_key_xmpp_tls_use", false)
	    || mSettings.getBoolean("settings_key_gmail", false)) {
	    mConnectionConfiguration.setSecurityMode(SecurityMode.required);
	}
	if (mSettings.getBoolean(BeemApplication.SMACK_DEBUG_KEY, false))
	    mConnectionConfiguration.setDebuggerEnabled(true);
	mConnectionConfiguration.setSendPresence(false);
	mConnectionConfiguration.setRosterLoadedAtLogin(false);
	// maybe not the universal path, but it works on most devices (Samsung Galaxy, Google Nexus One)
	mConnectionConfiguration.setTruststoreType("BKS");
	mConnectionConfiguration.setTruststorePath("/system/etc/security/cacerts.bks");
	if (sslContext != null)
	    mConnectionConfiguration.setCustomSSLContext(sslContext);
    }

    /**
     * Get the save proxy configuration.
     *
     * @return the proxy configuration
     */
    private ProxyInfo getProxyConfiguration() {
	boolean useProxy = mSettings.getBoolean(BeemApplication.PROXY_USE_KEY, false);
	if (useProxy) {
	    String stype = mSettings.getString(BeemApplication.PROXY_TYPE_KEY, "HTTP");
	    String phost = mSettings.getString(BeemApplication.PROXY_SERVER_KEY, "");
	    String puser = mSettings.getString(BeemApplication.PROXY_USERNAME_KEY, "");
	    String ppass = mSettings.getString(BeemApplication.PROXY_PASSWORD_KEY, "");
	    int pport = Integer.parseInt(mSettings.getString(BeemApplication.PROXY_PORT_KEY, "1080"));
	    ProxyInfo.ProxyType type = ProxyType.valueOf(stype);
	    return new ProxyInfo(type, phost, pport, puser, ppass);
	} else {
	    return ProxyInfo.forNoProxy();
	}
    }

    /**
     * Get the connection configuration for an Android system account.
     *
     * @param account the Android account
     * @param proxy the proxy to use
     * @return the ConnectionConfiguration or null if the account is not supported
     */
    private ConnectionConfiguration getConnectionConfigurationForAccount(Account account, ProxyInfo proxy) {
	ConnectionConfiguration result = null;
	if ("com.google".equals(account.type)) {
	    result = new ConnectionConfiguration("talk.google.com", DEFAULT_XMPP_PORT, proxy);
	    result.setServiceName(StringUtils.parseServer(account.name));
	    result.setCallbackHandler(new AccountAuthenticator(this, account));
	    result.setSecurityMode(SecurityMode.required);
	}
	return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
	Log.d(TAG, "ONBIND()");
	return mBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
	Log.d(TAG, "ONUNBIND()");
	if (mConnection != null  && !mConnection.getAdaptee().isConnected()) {
	    this.stopSelf();
	}
	return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
	super.onCreate();
	registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	mSettings = PreferenceManager.getDefaultSharedPreferences(this);
	mSettings.registerOnSharedPreferenceChangeListener(mPreferenceListener);
	if (mSettings.getBoolean(BeemApplication.USE_AUTO_AWAY_KEY, false)) {
	    mOnOffReceiverIsRegistered = true;
	    registerReceiver(mOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	    registerReceiver(mOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
	}
	String tmpJid = mSettings.getString(BeemApplication.ACCOUNT_USERNAME_KEY, "").trim();
	mLogin = StringUtils.parseName(tmpJid);
	boolean useSystemAccount = mSettings.getBoolean(BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
	mPort = DEFAULT_XMPP_PORT;
	mService = StringUtils.parseServer(tmpJid);
	mHost = mService;
	initMemorizingTrustManager();

	if (mSettings.getBoolean(BeemApplication.ACCOUNT_SPECIFIC_SERVER_KEY, false)) {
	    mHost = mSettings.getString(BeemApplication.ACCOUNT_SPECIFIC_SERVER_HOST_KEY, "").trim();
	    if ("".equals(mHost))
		mHost = mService;
	    String tmpPort = mSettings.getString(BeemApplication.ACCOUNT_SPECIFIC_SERVER_PORT_KEY, "5222");
	    if (!"".equals(tmpPort))
		mPort = Integer.parseInt(tmpPort);
	}
	if (mSettings.getBoolean(BeemApplication.FULL_JID_LOGIN_KEY, false)
		|| "gmail.com".equals(mService) || "googlemail.com".equals(mService)
		|| useSystemAccount)  {
	    mLogin = tmpJid;
	}

	configure(ProviderManager.getInstance());

	mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
	mBind = new XmppFacade(this);
	Log.d(TAG, "Create BeemService");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	mNotificationManager.cancelAll();
	unregisterReceiver(mReceiver);
	mSettings.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);
	if (mOnOffReceiverIsRegistered)
	    unregisterReceiver(mOnOffReceiver);
	if (mConnection.isAuthentificated() && BeemConnectivity.isConnected(this))
	    mConnection.disconnect();
	Log.i(TAG, "Stopping the service");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(Intent intent, int startId) {
	super.onStart(intent, startId);
	Log.d(TAG, "onStart");
	createConnectAsync();
    }

    /**
     * Create the XmppConnectionAdapter.
     * This method makes a network request so it must not be called on the main thread.
     * @return the connection
     */
    public XmppConnectionAdapter createConnection() {
	if (mConnection == null) {
	    initConnectionConfig();
	    mConnection = new XmppConnectionAdapter(mConnectionConfiguration, mLogin, null, this);
	}
	return mConnection;
    }

    /**
     * Show a notification using the preference of the user.
     * @param id the id of the notification.
     * @param notif the notification to show
     */
    public void sendNotification(int id, Notification notif) {
	if (mSettings.getBoolean(BeemApplication.NOTIFICATION_VIBRATE_KEY, true))
	    notif.defaults |= Notification.DEFAULT_VIBRATE;
	notif.ledARGB = 0xff0000ff; // Blue color
	notif.ledOnMS = 1000;
	notif.ledOffMS = 1000;
	notif.flags |= Notification.FLAG_SHOW_LIGHTS;
	String ringtoneStr = mSettings.getString(BeemApplication.NOTIFICATION_SOUND_KEY,
		Settings.System.DEFAULT_NOTIFICATION_URI.toString());
	notif.sound = Uri.parse(ringtoneStr);
	mNotificationManager.notify(id, notif);
    }

    /**
     * Delete a notification.
     * @param id the id of the notification
     */
    public void deleteNotification(int id) {
	mNotificationManager.cancel(id);
    }

    /**
     * Reset the status to online after a disconnect.
     */
    public void resetStatus() {
	Editor edit = mSettings.edit();
	edit.putInt(BeemApplication.STATUS_KEY, 1);
	edit.commit();
    }

    /**
     * Initialize Jingle from an XmppConnectionAdapter.
     * @param adaptee XmppConnection used for jingle.
     */
    public void initJingle(XMPPConnection adaptee) {
    }

    /**
     * Return a bind to an XmppFacade instance.
     * @return IXmppFacade a bind to an XmppFacade instance
     */
    public IXmppFacade getBind() {
	return mBind;
    }

    /**
     * Get the preference of the service.
     * @return the preference
     */
    public SharedPreferences getServicePreference() {
	return mSettings;
    }

    /**
     * Get the notification manager system service.
     * @return the notification manager service.
     */
    public NotificationManager getNotificationManager() {
	return mNotificationManager;
    }

    /**
     * Utility method to create and make a connection asynchronously.
     */
    private synchronized void createConnectAsync() {
	if (mConnection == null) {
	    new Thread(new Runnable() {

		@Override
		public void run() {
		    createConnection();
		    connectAsync();
		}
	    }).start();
	} else
	    connectAsync();
    }

    /**
     * Utility method to connect asynchronously.
     */
    private void connectAsync() {
	try {
	    mConnection.connectAsync();
	} catch (RemoteException e) {
	    Log.w(TAG, "unable to connect", e);
	}
    }

    /**
     * Get the specified Android account.
     *
     * @param accountName the account name
     * @param accountType the account type
     *
     * @return the account or null if it does not exist
     */
    private Account getAccount(String accountName, String accountType) {
	AccountManager am = AccountManager.get(this);
	for (Account a : am.getAccountsByType(accountType)) {
	    if (a.name.equals(accountName)) {
		return a;
	    }
	}
	return null;
    }

    /**
     * Install the MemorizingTrustManager in the ConnectionConfiguration of Smack.
     */
    private void initMemorizingTrustManager() {
	try {
	    sslContext = SSLContext.getInstance("TLS");
	    sslContext.init(null, MemorizingTrustManager.getInstanceList(this),
		    new java.security.SecureRandom());
	} catch (GeneralSecurityException e) {
	    Log.w(TAG, "Unable to use MemorizingTrustManager", e);
	}
    }

    /**
     * A sort of patch from this thread: http://www.igniterealtime.org/community/thread/31118. Avoid ClassCastException
     * by bypassing the classloading shit of Smack.
     * @param pm The ProviderManager.
     */
    private void configure(ProviderManager pm) {
	Log.d(TAG, "configure");
	// Service Discovery # Items
	pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
	// Service Discovery # Info
	pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

	// Privacy
	//pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
	// Delayed Delivery only the new version
	pm.addExtensionProvider("delay", "urn:xmpp:delay", new DelayInfoProvider());

	// Service Discovery # Items
	pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
	// Service Discovery # Info
	pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

	// Chat State
	ChatStateExtension.Provider chatState = new ChatStateExtension.Provider();
	pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", chatState);
	pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates",
	    chatState);
	pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", chatState);
	pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", chatState);
	pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", chatState);
	// capabilities
	pm.addExtensionProvider(CapsExtension.NODE_NAME, CapsExtension.XMLNS, new CapsExtensionProvider());

	//Pubsub
	pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub", new PubSubProvider());
	pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub", new ItemsProvider());
	pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub", new ItemsProvider());
	pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub", new ItemProvider());

	pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub#event", new ItemsProvider());
	pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub#event", new ItemProvider());
	pm.addExtensionProvider("event", "http://jabber.org/protocol/pubsub#event", new EventProvider());
	//TODO rajouter les manquants pour du full pubsub


	//PEP avatar
	pm.addExtensionProvider("metadata", "urn:xmpp:avatar:metadata", new AvatarMetadataProvider());
	pm.addExtensionProvider("data", "urn:xmpp:avatar:data", new AvatarProvider());

//         PEPProvider pep  = new PEPProvider();
//         AvatarMetadataProvider avaMeta  = new AvatarMetadataProvider();
//         pep.registerPEPParserExtension("urn:xmpp:avatar:metadata", avaMeta);
//         pm.addExtensionProvider("event", "http://jabber.org/protocol/pubsub#event", pep);

	// ping
	pm.addIQProvider(PingExtension.ELEMENT, PingExtension.NAMESPACE, PingExtension.class);

	/*
	// Private Data Storage
	pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
	// Time
	try {
	    pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
	} catch (ClassNotFoundException e) {
	    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
	}
	// Roster Exchange
	pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());
	// Message Events
	pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());
	// XHTML
	pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
	// Group Chat Invitations
	pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());
	// Data Forms
	pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
	// MUC User
	pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
	// MUC Admin
	pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
	// MUC Owner
	pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
	// Version
	try {
	    pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
	} catch (ClassNotFoundException e) {
	    // Not sure what's happening here.
	    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Version");
	}
	// VCard
	pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
	// Offline Message Requests
	pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
	// Offline Message Indicator
	pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
	// Last Activity
	pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
	// User Search
	pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
	// SharedGroupsInfo
	pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup",
	    new SharedGroupsInfo.Provider());
	// JEP-33: Extended Stanza Addressing
	pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());
	// FileTransfer
	pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
	pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
	pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new IBBProviders.Open());
	pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new IBBProviders.Close());
	pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb", new IBBProviders.Data());

	pm.addIQProvider("command", COMMAND_NAMESPACE, new AdHocCommandDataProvider());
	pm.addExtensionProvider("malformed-action", COMMAND_NAMESPACE,
	    new AdHocCommandDataProvider.MalformedActionError());
	pm.addExtensionProvider("bad-locale", COMMAND_NAMESPACE,
	    new AdHocCommandDataProvider.BadLocaleError());
	pm.addExtensionProvider("bad-payload", COMMAND_NAMESPACE,
	    new AdHocCommandDataProvider.BadPayloadError());
	pm.addExtensionProvider("bad-sessionid", COMMAND_NAMESPACE,
	    new AdHocCommandDataProvider.BadSessionIDError());
	pm.addExtensionProvider("session-expired", COMMAND_NAMESPACE,
	    new AdHocCommandDataProvider.SessionExpiredError());
	 */

	/* register additionnals sasl mechanisms */
	SASLAuthentication.registerSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME,
	    SASLGoogleOAuth2Mechanism.class);
	SASLAuthentication.registerSASLMechanism(ScramSaslMechanism.MECHANISM_NAME,
	    ScramSaslMechanism.class);

	SASLAuthentication.supportSASLMechanism(ScramSaslMechanism.MECHANISM_NAME);
	// Configure entity caps manager. This must be done only once
	File f = new File(getCacheDir(), "entityCaps");
	f.mkdirs();
	try {
	    EntityCapsManager.setPersistentCache(new SimpleDirectoryPersistentCache(f));
	} catch (IllegalStateException e) {
	    Log.v(TAG, "EntityCapsManager already initialized", e);
	} catch (IOException e) {
	    Log.w(TAG, "EntityCapsManager not able to reuse persistent cache");
	}
    }

    /**
     * Listen on preference changes.
     */
    private class BeemServicePreferenceListener implements SharedPreferences.OnSharedPreferenceChangeListener {

	/**
	 * ctor.
	 */
	public BeemServicePreferenceListener() {
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	    if (BeemApplication.USE_AUTO_AWAY_KEY.equals(key)) {
		if (sharedPreferences.getBoolean(BeemApplication.USE_AUTO_AWAY_KEY, false)) {
		    mOnOffReceiverIsRegistered = true;
		    registerReceiver(mOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		    registerReceiver(mOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		} else {
		    mOnOffReceiverIsRegistered = false;
		    unregisterReceiver(mOnOffReceiver);
		}
	    }
	}
    }

    /**
     * Listen on some Intent broadcast, ScreenOn and ScreenOff.
     */
    private class BeemServiceBroadcastReceiver extends BroadcastReceiver {

	private String mOldStatus;
	private int mOldMode;

	/**
	 * Constructor.
	 */
	public BeemServiceBroadcastReceiver() {
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
	    String intentAction = intent.getAction();
	    if (intentAction.equals(Intent.ACTION_SCREEN_OFF)) {
		mOldMode = mConnection.getPreviousMode();
		mOldStatus = mConnection.getPreviousStatus();
		if (mConnection.isAuthentificated())
		    mConnection.changeStatus(Status.CONTACT_STATUS_AWAY,
			    mSettings.getString(BeemApplication.AUTO_AWAY_MSG_KEY, "Away"));
	    } else if (intentAction.equals(Intent.ACTION_SCREEN_ON)) {
		if (mConnection.isAuthentificated())
		    mConnection.changeStatus(mOldMode, mOldStatus);
	    }
	}
    }
}

