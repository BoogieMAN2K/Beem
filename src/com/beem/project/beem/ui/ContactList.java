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

*/
package com.beem.project.beem.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.beem.project.beem.BeemApplication;
import com.beem.project.beem.R;
import com.beem.project.beem.service.Contact;
import com.beem.project.beem.service.PresenceAdapter;
import com.beem.project.beem.service.aidl.IBeemRosterListener;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IRoster;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.ui.dialogs.builders.Alias;
import com.beem.project.beem.ui.dialogs.builders.ChatList;
import com.beem.project.beem.ui.dialogs.builders.DeleteContact;
import com.beem.project.beem.ui.dialogs.builders.ResendSubscription;
import com.beem.project.beem.utils.BeemBroadcastReceiver;

import org.jivesoftware.smack.util.StringUtils;

import static com.google.android.apps.iosched.util.LogUtils.*;

/**
 * The contact list activity displays the roster of the user.
 */
public class ContactList extends FragmentActivity {

    private static final Intent SERVICE_INTENT = new Intent();
    static {
	SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.beem", "com.beem.project.beem.BeemService"));
    }

    private static final String TAG = makeLogTag(ContactList.class);
    private static final float PAGER_TAB_SECONDARY_ALPHA = 0.5f;
    private final List<String> mListGroup = new ArrayList<String>();

    /** Map containing a list of the different contacts of a given group.
     * Each list is a @{link SortedList} so there is no need to sort it again.
     * */
    private final Map<String, List<Contact>> mContactOnGroup = new HashMap<String, List<Contact>>();
    private final ServiceConnection mServConn = new BeemServiceConnection();
    private final BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver();
    private final Map<String, ContactListAdapter> contactListAdapters = new HashMap<String, ContactListAdapter>();

    private final BeemRosterListener mBeemRosterListener = new BeemRosterListener();
    private IRoster mRoster;
    private IXmppFacade mXmppFacade;
    private IChatManager mChatManager;
    private SharedPreferences mSettings;
    private boolean mBinded;
    private ViewPager viewPager;
    private ListPagerAdapter groupsPagesAdapter;
    private PagerTabStrip pagerTabs;

    /**
     * Constructor.
     */
    public ContactList() {
    }

    /**
     * Callback for menu creation.
     * @param menu the menu created
     * @return true on success, false otherwise
     */
    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.contact_list, menu);
	return true;
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case R.id.contact_list_menu_settings:
		startActivity(new Intent(this, Settings.class));
		return true;
	    case R.id.contact_list_menu_add_contact:
		startActivity(new Intent(ContactList.this, AddContact.class));
		return true;
	    case R.id.menu_change_status:
		startActivity(new Intent(ContactList.this, ChangeStatus.class));
		return true;
	    case R.id.contact_list_menu_chatlist:
		List<Contact> openedChats;
		try {
		    openedChats = mChatManager.getOpenedChatList();
		    LOGD(TAG, "opened chats = " + openedChats);
		    Dialog chatList = new ChatList(ContactList.this, openedChats).create();
		    chatList.show();
		} catch (RemoteException e) {
		    e.printStackTrace();
		}
		return true;
	    case R.id.menu_disconnect:
		stopService(SERVICE_INTENT);
		finish();
		return true;
	    default:
		return false;
	}
    }


    @Override
    protected void onCreate(Bundle saveBundle) {
	super.onCreate(saveBundle);
	mSettings = PreferenceManager.getDefaultSharedPreferences(this);
	setContentView(R.layout.contactlist);

	this.registerReceiver(mReceiver, new IntentFilter(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED));

	viewPager = (ViewPager) findViewById(R.id.pager);
	groupsPagesAdapter = new ListPagerAdapter(getSupportFragmentManager(), viewPager);
	pagerTabs = (PagerTabStrip) findViewById(R.id.tabstrip);
	pagerTabs.setTabIndicatorColorResource(R.color.vert_manu);
	pagerTabs.setNonPrimaryAlpha(PAGER_TAB_SECONDARY_ALPHA);

	mListGroup.add(getString(R.string.contact_list_all_contact));
	mListGroup.add(getString(R.string.contact_list_no_group));
	groupsPagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
	super.onStart();
	if (!mSettings.getBoolean(BeemApplication.HIDE_GROUPS_KEY, false))
	    showGroups();
	else
	    hideGroups();

	if (!mBinded)
	    mBinded = bindService(SERVICE_INTENT, mServConn, BIND_AUTO_CREATE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
	super.onPause();
	try {
	    if (mRoster != null) {
		mRoster.removeRosterListener(mBeemRosterListener);
		mRoster = null;
	    }
	} catch (RemoteException e) {
	    LOGD("ContactList", "Remote exception", e);
	}
	if (mBinded) {
	    unbindService(mServConn);
	    mBinded = false;
	}
	mXmppFacade = null;
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	this.unregisterReceiver(mReceiver);
	LOGV(TAG, "onDestroy activity");
    }

    /**
     * Get a {@link ContactListAdapter} for a group.
     * The {@link ContactListAdapter} will be created if it is not exist.
     * @param group the group
     * @return the adapter
     */
    ContactListAdapter getContactListAdapter(String group) {
	synchronized (contactListAdapters) {
	    ContactListAdapter contactListAdapter = contactListAdapters.get(group);
	    if (contactListAdapter == null) {
		contactListAdapter = new ContactListAdapter(ContactList.this);
		contactListAdapters.put(group, contactListAdapter);
		List<String> realGroups = mListGroup.subList(1, mListGroup.size() - 1);
		if (!mListGroup.contains(group)) {
		    boolean added = false;
		    // insert group in sorted list
		    for (ListIterator<String> iterator = realGroups.listIterator(); iterator.hasNext();) {
			String currentGroup = (String) iterator.next();
			if (currentGroup.compareTo(group) > 0) {
			    iterator.previous();
			    iterator.add(group);
			    added = true;
			    break;
			}
		    }
		    if (!added)
			realGroups.add(group);
		    groupsPagesAdapter.notifyDataSetChanged();
		}
	    }
	    boolean hideDisconnected = mSettings.getBoolean(BeemApplication.SHOW_OFFLINE_CONTACTS_KEY, false);
	    contactListAdapter.setOnlineOnly(hideDisconnected);
	    return contactListAdapter;
	}
    }

    /**
     * Exectute a context menu action on a specified contact.
     * @param itemId the id of the menu action
     * @param contact the contact
     */
    void doContextMenuAction(int itemId, Contact contact) {
	switch (itemId) {
	    case R.id.contact_list_context_menu_call_item:
		try {
		    mXmppFacade.call(contact.getJID() + "/psi");
		} catch (RemoteException e) {
		    e.printStackTrace();
		}
		break;
	    case R.id.contact_list_context_menu_userinfo_alias:
		Dialog alias = new Alias(ContactList.this, mRoster, contact).create();
		alias.show();
		break;
	    case R.id.contact_list_context_menu_userinfo_subscription:
		Dialog subscription = new ResendSubscription(ContactList.this,
			mXmppFacade, contact).create();
		subscription.show();
		break;
	    case R.id.contact_list_context_menu_userinfo_delete:
		Dialog delete = new DeleteContact(ContactList.this, mRoster, contact).create();
		delete.show();
		break;
	    default:
		LOGW(TAG, "Context menu action not supported" + itemId);
		break;
	}

    }

    /**
     * Show the groups view.
     */
    private void showGroups() {
	pagerTabs.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the groups view.
     */
    private void hideGroups() {
	pagerTabs.setVisibility(View.GONE);
    }

    /**
     * Remove old groups on the banner.
     * @throws RemoteException if an error occur when communicating with the service
     */
    private void cleanBannerGroup() throws RemoteException {
	if (mListGroup.size() <= 2)
	    return;
	List<String> rosterGroups = mRoster.getGroupsNames();
	Collections.sort(rosterGroups);
	List<String> realGroups = mListGroup.subList(1, mListGroup.size() - 1);
	realGroups.clear();
	realGroups.addAll(rosterGroups);
	groupsPagesAdapter.notifyDataSetChanged();
    }

    /**
     * Add a contact to the special list No Group and All contacts.
     * The contact will be added if the list is not the current list otherwise
     * the list must be modified in a Handler.
     *
     * @param contact the contact to add.
     */
    private void addToSpecialList(Contact contact) {
	List<String> groups = contact.getGroups();

	ContactListAdapter adapter = getContactListAdapter(getString(R.string.contact_list_all_contact));
	adapter.put(contact);
	if (groups.isEmpty()) {
	    adapter = getContactListAdapter(getString(R.string.contact_list_no_group));
	    adapter.put(contact);
	}
    }

	/**
     * Listener on service event.
     */
    private class BeemRosterListener extends IBeemRosterListener.Stub {
	/**
	 * Constructor.
	 */
	public BeemRosterListener() {
	}

	/**
	 * {@inheritDoc}
	 * Simple stategy to handle the onEntriesAdded event.
	 * if contact has to be shown :
	 * <ul>
	 * <li> add him to his groups</li>
	 * <li> add him to the specials groups</>
	 * </ul>
	 */
	@Override
	public void onEntriesAdded(final List<String> addresses) throws RemoteException {
	    for (String newName : addresses) {
	    	final Contact contact = mRoster.getContact(StringUtils.parseBareAddress(newName));
	    	putContactInList(contact);
	    }
	}

	/**
	 * {@inheritDoc}
	 * Simple stategy to handle the onEntriesDeleted event.
	 * <ul>
	 * <li> Remove the contact from all groups</li>
	 * </ul>
	 */
	@Override
	public void onEntriesDeleted(final List<String> addresses) throws RemoteException {
	    LOGD(TAG, "onEntries deleted " + addresses);
	    for (String cToDelete : addresses) {
		final Contact contact = new Contact(cToDelete);
		for (final ContactListAdapter adapter : contactListAdapters.values()) {
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    adapter.remove(contact);
			}
		    });
		}
	    }
	    cleanBannerGroup();

	}

	/**
	 * {@inheritDoc}
	 * Simple stategy to handle the onEntriesUpdated event.
	 * <ul>
	 * <li> Remove the contact from all groups</li>
	 * <li> if contact has to be shown add it to his groups</li>
	 * <li> if contact has to be shown add it to the specials groups</li>
	 * </ul>
	 */
	@Override
	public void onEntriesUpdated(final List<String> addresses) throws RemoteException {
	    LOGD(TAG, "onEntries updated " + addresses);
	    for (String cToDelete : addresses) {
		Contact contact = new Contact(cToDelete);
		for (ContactListAdapter adapter : contactListAdapters.values()) {
		    adapter.remove(contact);
		}
	    }
	    for (String newName : addresses) {
		final Contact contact = mRoster.getContact(StringUtils.parseBareAddress(newName));
		putContactInList(contact);
	    }
	    cleanBannerGroup();
	}

	/**
	 * {@inheritDoc}
	 * Simple stategy to handle the onPresenceChanged event.
	 * <ul>
	 * <li> Remove the contact from all groups</li>
	 * <li> if contact has to be shown add it to his groups</li>
	 * <li> if contact has to be shown add it to the specials groups</li>
	 * </ul>
	 */
	@Override
	public void onPresenceChanged(PresenceAdapter presence) throws RemoteException {
	    String from = presence.getFrom();
	    final Contact contact = mRoster.getContact(StringUtils.parseBareAddress(from));
	    putContactInList(contact);
	}

	/**
	 * Put a contact in the different group list.
	 * @param contact the contact
	 */
	private void putContactInList(final Contact contact) {
	    List<String> groups = contact.getGroups();
	    for (final String group : groups) {
		runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
			ContactListAdapter contactListAdapter = getContactListAdapter(group);
			contactListAdapter.put(contact);
		    }
		});
	    }

	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    addToSpecialList(contact);
		}
	    });
	}
    }

    /**
     * The service connection used to connect to the Beem service.
     */
    private class BeemServiceConnection implements ServiceConnection {

	/**
	 * Constructor.
	 */
	public BeemServiceConnection() {
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
	    mXmppFacade = IXmppFacade.Stub.asInterface(service);
	    try {
		mRoster = mXmppFacade.getRoster();
		if (mRoster != null) {
		    List<String> tmpGroupList = mRoster.getGroupsNames();
		    cleanBannerGroup();
		    synchronized (contactListAdapters) {
			for (ContactListAdapter ca : contactListAdapters.values()) {
			    ca.clear();
			}
		    }
		    assignContactToGroups(mRoster.getContactList(), tmpGroupList);

		    mRoster.addRosterListener(mBeemRosterListener);
		    LOGD(TAG, "add roster listener");
		    mChatManager = mXmppFacade.getChatManager();
		}
	    } catch (RemoteException e) {
		e.printStackTrace();
	    }
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
	    try {
		mRoster.removeRosterListener(mBeemRosterListener);
	    } catch (RemoteException e) {
		e.printStackTrace();
	    }
	    mXmppFacade = null;
	    mChatManager = null;
	    mRoster = null;
	    mListGroup.clear();
	    mContactOnGroup.clear();
	    mBinded = false;

	}

	/**
	 * Assign the differents contact to their groups.
	 * This methods will fill the mContactOnGroup map.
	 *
	 * @param contacts list of contacts
	 * @param groupNames list of existing groups
	 */
	private void assignContactToGroups(List<Contact> contacts, List<String> groupNames) {
	    for (Contact c : contacts) {
		addToSpecialList(c);

		List<String> groups = c.getGroups();

		for (String currentGroup : groups) {
		    ContactListAdapter cl = getContactListAdapter(currentGroup);
		    cl.put(c);
		}
	    }
	}

    }

    /**
     * PagerAdapter for the contact list.
     */
    private class ListPagerAdapter extends FragmentPagerAdapter {

	/**
	 * Create a {@link ListPagerAdapter}.
	 * @param fm the {@link FragmentManager}
	 * @param viewPager the {@link ViewPager} associate with this adapter
	 */
	public ListPagerAdapter(final FragmentManager fm, final ViewPager viewPager) {
	    super(fm);
	    viewPager.setAdapter(this);
	}

	@Override
	public Fragment getItem(int position) {
	    String group = mListGroup.get(position);
	    ContactListFragment f = ContactListFragment.newInstance(group);
	    f.setListAdapter(getContactListAdapter(group));
	    return f;
	}

	@Override
	public int getCount() {
	    return mListGroup.size();
	}

	@Override
	public String getPageTitle(int position) {
	    return mListGroup.get(position);
	}

    }

}
