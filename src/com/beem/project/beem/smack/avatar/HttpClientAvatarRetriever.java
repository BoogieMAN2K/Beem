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
package com.beem.project.beem.smack.avatar;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;

/**
 * An AvatarRetriever which retrieve the avatar over HTTP using the Apache HttpClient.
 */
public  class HttpClientAvatarRetriever implements AvatarRetriever {

    private String mUrl;
    private HttpClient mClient;

    /**
     * Create a HttpAvatarRetriever.
     *
     * @param client the custom HttpClient to use to downlowad
     * @param url the url of the avatar to download.
     */
    public HttpClientAvatarRetriever(final HttpClient client, final String url) {
	mUrl = url;
	mClient = client;
    }

    /**
     * Create a HttpAvatarRetriever.
     *
     * @param url the url of the avatar to download.
     */
    public HttpClientAvatarRetriever(final String url) {
	mUrl = url;
	mClient = new DefaultHttpClient();
    }

    @Override
    public byte[] getAvatar() throws IOException {
	HttpUriRequest request;
	try {
	    request = new HttpGet(mUrl);
	} catch (IllegalArgumentException e) {
	    IOException ioe = new IOException("Invalid url " + mUrl);
	    ioe.initCause(e);
	    throw ioe;
	}
	HttpResponse response = mClient.execute(request);
	HttpEntity entity = response.getEntity();
	InputStream in = entity.getContent();
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	try {
	    byte[] data = new byte[1024];
	    int nbread;
	    while ((nbread = in.read(data)) != -1) {
		os.write(data, 0, nbread);
	    }
	} finally {
	    in.close();
	    os.close();
	}
	return os.toByteArray();
    }

}
