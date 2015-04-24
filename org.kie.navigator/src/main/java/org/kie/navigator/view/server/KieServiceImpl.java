/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2014 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.kie.navigator.view.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public abstract class KieServiceImpl implements IKieServiceImpl {

	protected IServer server;
	
	/**
	 * @param server
	 */
	public KieServiceImpl() {
		
	}
	
	public void setServer(IServer server) {
		this.server = server;
	}

	public IServer getServer() {
		return server;
	}
	
	protected String httpGet(String request) throws IOException {
		String host = getKieRESTUrl();
		URL url = new URL(host + "/" + request);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content", "application/json");
		String creds = getUsername() + ":" + getPassword();
		conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(creds));
		String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		return response;
	}

	protected String httpDelete(String request) {
		return "";
	}

	protected String httpPost(String request) {
		return "";
	}

	protected void httpClose() {
		
	}
	
	protected String getUsername() {
		return "admin";
	}
	
	protected String getPassword() {
		return "admin";
	}
	
	protected abstract String getKieRESTUrl();
}
