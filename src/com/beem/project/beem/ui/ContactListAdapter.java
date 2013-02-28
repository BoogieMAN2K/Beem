/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009-2011 by Frederic-Charles Barthelery,
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
package com.beem.project.beem.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.beem.project.beem.BeemApplication;
import com.beem.project.beem.R;
import com.beem.project.beem.providers.AvatarProvider;
import com.beem.project.beem.service.Contact;
import com.beem.project.beem.utils.SortedList;
import com.beem.project.beem.utils.Status;

/**
 * An Adapter for the contact list.
 * It displays a list of contact in a particular group.
 *
 */
public class ContactListAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = ContactListAdapter.class.getSimpleName();
    private final ComparatorContactListByStatusAndName<Contact> mComparator =
	new ComparatorContactListByStatusAndName<Contact>();
    private List<Contact> mCurrentList;
    private final List<Contact> allContacts = new SortedList<Contact>(new LinkedList<Contact>(), mComparator);
    private final List<Contact> onlineContacts = new SortedList<Contact>(new LinkedList<Contact>(), mComparator);
    private final Filter mFilter = new ContactFilter();
    private final Context context;
    private LayoutInflater mInflater;

    private boolean showOnlineOnly;

    /**
     * Create a ContactListAdapter.
     * @param c the android context
     */
    public ContactListAdapter(final Context c) {
    	mCurrentList = allContacts;
    	context = c;
    	mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
	return mCurrentList.size();
    }

    @Override
    public Object getItem(int position) {
	return mCurrentList.get(position);
    }

    @Override
    public long getItemId(int position) {
	return mCurrentList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View v = convertView;
	if (convertView == null) {
	    v = mInflater.inflate(R.layout.contactlistcontact, null);
	}
	Contact c = mCurrentList.get(position);
	bindView(v, c);
	return v;
    }

    /**
     * Put a contact in the list.
     * @param c the contact
     */
    public void put(Contact c) {
	put(c, allContacts);
	if (Status.statusOnline(c.getStatus()))
	    put(c, onlineContacts);
	notifyDataSetChanged();
    }

    /**
     * Remove a contact from the list.
     *
     * @param c the contact
     */
    public void remove(Contact c) {
	allContacts.remove(c);
	onlineContacts.remove(c);
	notifyDataSetChanged();
    }

    /**
     * Clear the contact list.
     */
    public void clear() {
	allContacts.clear();
	onlineContacts.clear();
	notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
	return mFilter;
    }

    /**
     * Bind a contact to the view.
     * @param view the row view.
     * @param curContact the contact.
     */
    private void bindView(View view, Contact curContact) {
	if (curContact != null) {
	    TextView v = (TextView) view.findViewById(R.id.contactlistpseudo);
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	    if (settings.getBoolean(BeemApplication.SHOW_JID, false))
	    	v.setText(curContact.getJID());
	    else
		v.setText(curContact.getName());
	    v = (TextView) view.findViewById(R.id.contactlistmsgperso);
	    v.setText(curContact.getMsgState());
	    ImageView img = (ImageView) view.findViewById(R.id.avatar);
	    String avatarId = curContact.getAvatarId();
	    int contactStatus = curContact.getStatus();
	    Drawable avatar = getAvatarStatusDrawable(avatarId);
	    img.setImageDrawable(avatar);
	    img.setImageLevel(contactStatus);
	}
    }

    /**
     * Get a LayerDrawable containing the avatar and the status icon.
     * The status icon will change with the level of the drawable.
     * @param avatarId the avatar id to retrieve or null to get default
     * @return a LayerDrawable
     */
    private Drawable getAvatarStatusDrawable(String avatarId) {
	Drawable avatarDrawable = null;
	if (avatarId != null) {
	    Uri uri = AvatarProvider.CONTENT_URI.buildUpon().appendPath(avatarId).build();
	    InputStream in = null;
	    try {
		try {
		    in = context.getContentResolver().openInputStream(uri);
		    avatarDrawable = Drawable.createFromStream(in, avatarId);
		} finally {
		    if (in != null)
			in.close();
		}
	    } catch (IOException e) {
		Log.w(TAG, "Error while setting the avatar " + avatarId, e);
	    }
	}
	if (avatarDrawable == null)
	    avatarDrawable = context.getResources().getDrawable(R.drawable.beem_launcher_icon_silver);
	LayerDrawable ld = (LayerDrawable) context.getResources().getDrawable(R.drawable.avatar_status);
	ld.setLayerInset(1, 36, 36, 0, 0);
	ld.setDrawableByLayerId(R.id.avatar, avatarDrawable);
	return ld;
    }

    /**
     * Put a contact in a list.
     * Helper method.
     *
     * @param c the contact
     * @param list the list
     */
    private void put(Contact c, List<Contact> list) {
	list.remove(c);
	list.add(c);
    }

    /**
     * Tell if the list display only online contacts.
     *
     * @return true if only online contacts are shown
     */
    public boolean isOnlineOnly() {
	return showOnlineOnly;
    }

    /**
     * Set the list to display only the online contacts.
     *
     * @param online true to display only online contacts
     */
    public void setOnlineOnly(boolean online) {
	if (online != showOnlineOnly) {
	    showOnlineOnly = online;
	    mCurrentList = showOnlineOnly ? onlineContacts : allContacts;
	    notifyDataSetChanged();
	}
    }

    /**
     * A Filter which select Contact to display by searching in ther Jid.
     */
    private class ContactFilter extends Filter {

	/**
	 * Create a ContactFilter.
	 */
	public ContactFilter() { }

	@Override
	protected Filter.FilterResults performFiltering(CharSequence constraint) {
	    Log.d(TAG, "performFiltering");
	    List<Contact> result = mCurrentList;
	    if (constraint.length() > 0) {
		result = new LinkedList<Contact>();
		for (Contact c : mCurrentList) {
		    if (c.getJID().contains(constraint))
			result.add(c);
		}
	    }
	    Filter.FilterResults fr = new Filter.FilterResults();
	    fr.values = result;
	    fr.count = result.size();
	    return fr;
	}

	@Override
	protected void publishResults(CharSequence constraint, Filter.FilterResults  results) {
	    Log.d(TAG, "publishResults");
	    List<Contact> contacts = (List<Contact>) results.values;
	    mCurrentList = contacts;
	    notifyDataSetChanged();
	}
    }

    /**
     * Comparator Contact by status and name.
     */
    private static class ComparatorContactListByStatusAndName<T> implements Comparator<T> {
	/**
	 * Constructor.
	 */
	public ComparatorContactListByStatusAndName() {
	}

	@Override
	public int compare(T c1, T c2) {
	    if (((Contact) c1).getStatus() < ((Contact) c2).getStatus()) {
		return 1;
	    } else if (((Contact) c1).getStatus() > ((Contact) c2).getStatus()) {
		return -1;
	    } else
		return ((Contact) c1).getName().compareToIgnoreCase(((Contact) c2).getName());
	}
    }

}
