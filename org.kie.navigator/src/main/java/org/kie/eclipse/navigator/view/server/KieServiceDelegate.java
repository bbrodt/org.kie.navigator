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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerPort;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.kie.eclipse.navigator.IKieNavigatorConstants;

import com.eclipsesource.json.JsonObject;

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

	private static int STATUS_REQUEST_DELAY = 1000;
	private static int STATUS_REQUEST_TIMEOUT = 60000;
	
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
		System.out.println("[GET] "+url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content", "application/json");
		String creds = getUsername() + ":" + getPassword();
		conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(creds));
		String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		System.out.println("[GET] response: "+response);
		return response;
	}

	/**
	 * Send an HTTP DELETE request to the KIE console.
	 * @param request - the request URL fragment; see the Drools REST API
	 *            documentation for details
	 * @return the Job ID. This can be used in calls to getJobStatus() to fetch
	 *         the completion status of the request.
	 * @throws IOException
	 */
	protected String httpDelete(String request) throws IOException {
		String host = getKieRESTUrl();
		URL url = new URL(host + "/" + request);
		System.out.println("[DELETE] "+url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Content", "application/json");
		String creds = getUsername() + ":" + getPassword();
		conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(creds));
		String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		System.out.println("[DELETE] response: "+response);

		if (conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
			throw new IOException("HTTP DELETE failed : HTTP error code : " + conn.getResponseCode());
		}

		JsonObject jo = JsonObject.readFrom(response);
		String status = jo.get("status").asString();
		if (status != null && !status.isEmpty()) {
			if (!"APPROVED".equals(status))
				throw new IOException("HTTP DELETE failed : Request status code : " + status);
		}
		String jobId = jo.get("jobId").asString();
		if (jobId != null && !jobId.isEmpty())
			return jobId;

		return response;
	}

	/**
	 * Send an HTTP POST request to the KIE console.
	 * 
	 * @param request - the request URL fragment; see the Drools REST API
	 *            documentation for details
	 * @param body - the JSON object required by the request
	 * @return the Job ID. This can be used in calls to getJobStatus() to fetch
	 *         the completion status of the request.
	 * @throws IOException
	 * @throws RuntimeException
	 */
	protected String httpPost(String request, JsonObject body) throws IOException, RuntimeException {
		String host = getKieRESTUrl();
		URL url = new URL(host + "/" + request);
		System.out.println("[POST] "+url.toString());
		System.out.println("[POST] body: "+body);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(body!=null);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		String creds = getUsername() + ":" + getPassword();
		conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(creds));

		if (body!=null) {
			java.io.OutputStream os = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(os, "UTF-8");
			body.writeTo(writer);
			writer.close();
			os.flush();
		}
		
		String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		System.out.println("[POST] response: "+response);

		if (conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
			throw new IOException("HTTP POST failed : HTTP error code : " + conn.getResponseCode());
		}

		JsonObject jo = JsonObject.readFrom(response);
		String status = jo.get("status").asString();
		if (status != null && !status.isEmpty()) {
			if (!"APPROVED".equals(status))
				throw new IOException("HTTP POST failed : Request status code : " + status);
		}
		String jobId = jo.get("jobId").asString();
		if (jobId != null && !jobId.isEmpty())
			return jobId;

		return response;
	}

	/**
	 * Sends a job status request to the KIE Server.
	 * 
	 * @param jobId - the job ID from a previous HTTP POST or DELETE request
	 * @return a string composed of a status word (e.g. "SUCCESS" or
	 *         "BAD_REQUEST") followed by a colon delimiter and a detailed
	 *         message.
	 * @throws IOException
	 */
	public String getJobStatus(String jobId, String title) throws IOException, InterruptedException {
		final AtomicReference<String> ar = new AtomicReference<String>();
		
		IWorkbench wb = PlatformUI.getWorkbench();
		IProgressService ps = wb.getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				public void run(IProgressMonitor pm) throws InterruptedException {
					pm.beginTask("Waiting for Job "+jobId+":\n\n"+title, STATUS_REQUEST_TIMEOUT);
					long startTime = System.currentTimeMillis();
					long stopTime = startTime;
					do {
						try {
							// send a Job Status request every STATUS_REQUEST_DELAY milliseconds
							Thread.sleep(STATUS_REQUEST_DELAY);
							String response = httpGet("jobs/" + jobId);
							JsonObject jo = JsonObject.readFrom(response);
							String status = jo.get("status").asString();
							String result = jo.get("result").asString();
							if ("null".equals(result)) {
								if (!"SUCCESS".equals(status))
									result = null;
							}
							if (status!=null && result!=null)
								ar.set(status + ":" + result);
							stopTime = System.currentTimeMillis();
							pm.worked(STATUS_REQUEST_DELAY);
							
							System.out.println("status="+status);
							System.out.println("result="+result);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
						if (pm.isCanceled())
							throw new InterruptedException("Operation canceled");
					}
					while (ar.get()==null && stopTime - startTime < STATUS_REQUEST_TIMEOUT);
					pm.done();
					System.out.println(
							"\n----------------------------------\n"+
							"Job "+jobId+" completed in "+(stopTime - startTime) / 1000.0+" sec\n"+
							"Status: " + ar.get()+
							"\n----------------------------------\n");
				}
			});
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		return ar.get();
	}
	
	public void deleteJob(String jobId) {
		try {
			httpDelete("jobs/" + jobId);
		}
		catch (Exception e) {
			// ignore
		}
	}
	
	public String getUsername() {
		return handler.getPreference(PREF_SERVER_USERNAME, "admin");
	}

	public String getPassword() {
		return handler.getPreference(PREF_SERVER_PASSWORD, "admin");
	}

	public int getGitPort() {
		return handler.getPreference(PREF_SERVER_GIT_PORT, 8001);
	}

	public String getKieApplication() {
		if (kieApplication == null) {
			String app = handler.getPreference(PREF_SERVER_KIE_APPLICATION_NAME, null);
			if (app != null) {
				try {
					kieApplication = app;
					System.out.print("Trying " + getKieRESTUrl() + "...");
					httpGet("organizationalunits");
					System.out.println("success!");
				}
				catch (Exception e) {
					System.out.println("not found");
					kieApplication = null;
				}
			}

			if (kieApplication == null) {
				// try to determine the HTTP URL from standard application names
				for (String s : kieApplicationNames) {
					try {
						kieApplication = s;
						System.out.print("Trying " + getKieRESTUrl() + "...");
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
		}
		return kieApplication;
	}

	public void setKieApplication(String kieApplication) {
		this.kieApplication = kieApplication;
	}

	public int getHttpPort() {
		if (httpPort == -1) {
			// find the HTTP port for this server. Note that the JBossServer
			// implementation of Server does not support getServerPorts()!
			Object o = getServer().getAdapter(JBossServer.class);
			if (o instanceof JBossServer) {
				httpPort = ((JBossServer) o).getJBossWebPort();
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
			if (httpPort == -1) {
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
		return "http://" + getServer().getHost() + ":" + getHttpPort() + "/" + getKieApplication() + "/rest";
	}
}
