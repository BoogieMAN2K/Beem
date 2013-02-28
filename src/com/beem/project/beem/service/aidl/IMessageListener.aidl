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
package com.beem.project.beem.service.aidl;

import com.beem.project.beem.service.Message;
import com.beem.project.beem.service.aidl.IChat;

interface IMessageListener {

	/**
	 * This method is executed when a chat receive a message.
	 * @param chat the chat receiving the message.
	 * @param msg the message received in the chat.
	 */
	void processMessage(in IChat chat, in Message msg);

	/**
	 * This method is executed when a new ChatState is received by the chat.
	 * You can use IChat.getState() in order to get the new state.
	 * @param chat the chat changed.
	 */
	void stateChanged(in IChat chat);
	/**
	 * This method is executed when the otr session status change.
	 * @param otrState the new state of otr session.
	 */
	void otrStateChanged(in String otrState);
}
