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

/**
 * Interface to listen for connection events
 * @author Da Risk <barthe_f@epitech.eu>
 */
interface IBeemConnectionListener {

    /**
     *  Callback to call when the connection is closed
     */
    void connectionClosed();

    /**
     *  Callback to call when the connection occurs
     *  @Deprecated
     */
    //void onConnect();

    //void connectionClosedOnError(in Exception e);
    /**
     *  Callback to call when the connection is closed on error
     */
    void connectionClosedOnError();

    /**
     * Callback to call when trying to reconnecting
     */
    void reconnectingIn(in int seconds);

    /**
     *  Callback to call when the reconnection has failed
     */
    void reconnectionFailed();

    /**
     *  Callback to call when the reconnection is successfull
     */
    void reconnectionSuccessful();

    /**
     *  Callback to call when the connection Failed
     */
    void connectionFailed(in String errorMsg);
}
