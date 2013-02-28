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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beem.project.beem.R;
import com.beem.project.beem.service.Contact;

/**
 * A Fragment which display a list of contacts.
 */
public class ContactListFragment extends ListFragment {
    private String group;
    private ContactList hostActivity;
    private Contact mSelectedContact;

    /**
     * Create a ContactListFragment.
     * @param group the group name
     * @return the ContactListFragment
     */
    public static ContactListFragment newInstance(String group) {
	ContactListFragment f = new ContactListFragment();
	Bundle b = new Bundle();
	b.putString("group", group);
	f.setArguments(b);
	return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	parseArguments();
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	hostActivity = (ContactList) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	ListAdapter adapter = hostActivity.getContactListAdapter(group);
	setListAdapter(adapter);
	registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	ContactListAdapter a  = (ContactListAdapter) getListAdapter();
	Contact c = (Contact) a.getItem(position);
	Intent i = new Intent(getActivity(), Chat.class);
	i.setData(c.toUri());
	startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	MenuInflater inflater =  hostActivity.getMenuInflater();
	inflater.inflate(R.menu.contactlist_context, menu);
	AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	mSelectedContact = (Contact) getListAdapter().getItem(info.position);
	menu.setHeaderTitle(mSelectedContact.getJID());
    }

    /**
     * Parse the arguments submit to the Fragment.
     */
    private void parseArguments() {
	Bundle b = getArguments();
	if (b == null)
	    return;
	group = b.getString("group");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	Intent in;
	boolean result = false;
	if (mSelectedContact != null) {
	    switch (item.getItemId()) {
		case R.id.contact_list_context_menu_chat_item:
		    List<String> res = mSelectedContact.getMRes();
		    if (res.isEmpty()) {
			break;
		    }
		    for (String resv : res) {
			in = new Intent(hostActivity, Chat.class);
			in.setData(mSelectedContact.toUri(resv));
			item.getSubMenu().add(resv).setIntent(in);
		    }
		    result = true;
		    break;
		case R.id.contact_list_context_menu_call_item:
		    hostActivity.doContextMenuAction(item.getItemId(), mSelectedContact);
		    break;
		case R.id.contact_list_context_menu_user_info:
		    item.getSubMenu().setHeaderTitle(mSelectedContact.getJID());
		    result = true;
		    break;
		case R.id.contact_list_context_menu_userinfo_alias:
		    hostActivity.doContextMenuAction(item.getItemId(), mSelectedContact);
		    result = true;
		    break;
		case R.id.contact_list_context_menu_userinfo_group:
		    in = new Intent(hostActivity, GroupList.class);
		    in.putExtra("contact", mSelectedContact);
		    startActivity(in);
		    result = true;
		    break;
		case R.id.contact_list_context_menu_userinfo_subscription:
		    hostActivity.doContextMenuAction(item.getItemId(), mSelectedContact);
		    result = true;
		    break;
		case R.id.contact_list_context_menu_userinfo_block:
		    result = true;
		    break;
		case R.id.contact_list_context_menu_userinfo_delete:
		    hostActivity.doContextMenuAction(item.getItemId(), mSelectedContact);
		    result = true;
		    break;
		default:
		    result = super.onContextItemSelected(item);
		    break;
	    }
	    return result;
	}
	return super.onContextItemSelected(item);
    }

}
