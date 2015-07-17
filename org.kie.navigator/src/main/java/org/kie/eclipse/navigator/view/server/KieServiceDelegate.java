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

package org.kie.eclipse.navigator.view.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerPort;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.kie.eclipse.navigator.IKieNavigatorConstants;

/**
 *
 */
public abstract class KieServiceDelegate implements IKieServiceDelegate, IKieNavigatorConstants {

	protected IKieResourceHandler handler;
	protected IServer server;
	// TODO: fetch from Preferences
	protected static List<String> kieApplicationNames = new ArrayList<String>();
	static {
		kieApplicationNames.add("kie-wb");
		kieApplicationNames.add("kie-drools-wb");
		kieApplicationNames.add("kie-jbpm-wb");
		kieApplicationNames.add("business-central");
		kieApplicationNames.add("drools-console");
		kieApplicationNames.add("jbpm-console");
	}
	private String kieApplication;
	private int httpPort = -1;

	/**
	 * @param server
	 */
	public KieServiceDelegate() {
		
	}
	
	public void setServer(IServer server) {
		this.server = server;
	}

	public IServer getServer() {
		return server;
	}
	
	public void setHandler(IKieResourceHandler handler) {
		this.handler = handler;
	}
	
	public IKieResourceHandler getHandler() {
		return handler;
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
	
	// TODO: fetch from Preferences
	public String getUsername() {
		return handler.getPreference(PREF_SERVER_USERNAME, "admin");
	}
	
	// TODO: fetch from Preferences
	public String getPassword() {
		return handler.getPreference(PREF_SERVER_PASSWORD, "admin");
	}
	
	// TODO: fetch from Preferences
	public int getGitPort() {
		return handler.getPreference(PREF_SERVER_GIT_PORT, 8001);
	}
	
	public String getKieApplication() {
		if (kieApplication==null) {
			String app = handler.getPreference(PREF_SERVER_KIE_APPLICATION_NAME, null);
			if (app!=null) {
				try {
					kieApplication = app;
					System.out.print("Trying "+getKieRESTUrl()+"...");
					httpGet("organizationalunits");
					System.out.println("success!");
				}
				catch (Exception e) {
					System.out.println("not found");
					kieApplication = null;
				}
			}
			
			if (kieApplication==null) {
				// try to determine the HTTP URL from standard application names
				for (String s : kieApplicationNames) {
					try {
						kieApplication = s;
						System.out.print("Trying "+getKieRESTUrl()+"...");
						httpGet("organizationalunits");
						handler.putPreference(PREF_SERVER_KIE_APPLICATION_NAME, s);
						System.out.println("success!");
						break;
					}
					catch (Exception e) {
						System.out.println("not found");
						kieApplication = null;
					}
				}
			}
			if (kieApplication==null) {
				kieApplication = "kie-wb";
			}
		}
		return kieApplication;
	}

	public void setKieApplication(String kieApplication) {
		this.kieApplication = kieApplication;
	}

	public int getHttpPort() {
		if (httpPort==-1) {
			// find the HTTP port for this server. Note that the JBossServer
			// implementation of Server does not support getServerPorts()!
			Object o = getServer().getAdapter(JBossServer.class);
			if (o instanceof JBossServer) {
				httpPort = ((JBossServer)o).getJBossWebPort();
			}
			else {
				// assume that Server.getServerPorts() actually works!
				ServerPort[] ports = getServer().getServerPorts(null);
				for (ServerPort port : ports) {
					if ("HTTP".equals(port.getProtocol())) {
						httpPort = port.getPort();
						break;
					}
				}
			}
			if (httpPort==-1) {
				// assume that it's 8080
				return handler.getPreference(PREF_SERVER_HTTP_PORT, 8080);
			}
		}
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
	
	protected String getKieRESTUrl() {
		return "http://" + 
				getServer().getHost() + ":" + 
				getHttpPort() + "/" + 
				getKieApplication() + 
				"/rest";
	}
}
