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

package org.kie.eclipse.navigator.view.server.jbpm60;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.kie.eclipse.navigator.view.server.IKieOrganization;
import org.kie.eclipse.navigator.view.server.IKieProject;
import org.kie.eclipse.navigator.view.server.IKieRepository;
import org.kie.eclipse.navigator.view.server.IKieServer;
import org.kie.eclipse.navigator.view.server.KieOrganization;
import org.kie.eclipse.navigator.view.server.KieProject;
import org.kie.eclipse.navigator.view.server.KieRepository;
import org.kie.eclipse.navigator.view.server.KieServiceDelegate;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 */
public class Kie60Service extends KieServiceDelegate {

	/**
	 *
	 */
	public Kie60Service() {
	}

	/* (non-Javadoc)
	 * @see org.kie.eclipse.navigator.view.server.IKieServiceImpl#getOrganizations()
	 */
	@Override
	public List<IKieOrganization> getOrganizations(IKieServer service)  throws IOException {
		List<IKieOrganization> result = new ArrayList<IKieOrganization>();
		
		String response = httpGet("organizationalunits");
		JsonArray ja = JsonArray.readFrom(response);
		for (int i=0; i<ja.size(); ++i) {
			JsonObject jo = ja.get(i).asObject();
			KieOrganization ko = new KieOrganization(service, jo.get("name").asString());
			ko.setProperties(jo);
			result.add(ko);
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kie.eclipse.navigator.view.server.IKieServiceImpl#getRepositories(org.kie.eclipse.navigator.view.server.IKieOrganization)
	 */
	@Override
	public List<IKieRepository> getRepositories(IKieOrganization organization) throws IOException {
		List<IKieRepository> result = new ArrayList<IKieRepository>();

		String response = httpGet("organizationalunits");
		JsonArray ja1 = JsonArray.readFrom(response);
		for (int i=0; i<ja1.size(); ++i) {
			JsonObject jo = ja1.get(i).asObject();
			if (organization.getName().equals(jo.get("name").asString())) {
				JsonArray ja2 = jo.get("repositories").asArray();
				for (int j=0; j<ja2.size(); ++j) {
					JsonValue jv = ja2.get(j);
					KieRepository kr = new KieRepository(organization, jv.asString());
					result.add(kr);
				}
			}
		}
		return result;
	}
	@Override
	public List<IKieRepository> getRepositories(IKieServer server) throws IOException {
		List<IKieRepository> result = new ArrayList<IKieRepository>();

		String response = httpGet("repositories");
		JsonArray ja1 = JsonArray.readFrom(response);
		for (int i=0; i<ja1.size(); ++i) {
			JsonObject jo = ja1.get(i).asObject();
			KieRepository kr = new KieRepository(server, jo.get("name").asString());
			kr.setProperties(jo);
			result.add(kr);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kie.eclipse.navigator.view.server.IKieServiceImpl#getProjects(org.kie.eclipse.navigator.view.server.IKieOrganization, org.kie.eclipse.navigator.view.server.IKieRepository)
	 */
	@Override
	public List<IKieProject> getProjects(IKieRepository repository) throws IOException {
		List<IKieProject> result = new ArrayList<IKieProject>();
		Object o = repository.load();
		if (o instanceof Repository) {
			Repository git = (Repository) o;
			try {
				String gitDir = git.getWorkTree().getAbsolutePath();
				for (String f : git.getWorkTree().list()) {
					File file = new File(gitDir + File.separator + f);
					if (file.isDirectory() && !file.getName().startsWith(".")) {
						result.add(new KieProject(repository, file.getName()));
					}
				}
			}
			finally {
				git.close();
			}
		}
		return result;
	}
	
	@Override
	public void createOrganization(IKieOrganization organization) throws IOException {
		final String jobId = httpPost("organizationalunits", organization.getProperties());
		try {
			String status = getJobStatus(jobId);
			
			if (status==null) {
				throw new IOException("Request to create Organization '"+organization.getName()+"' has timed out");
			}
			if (!status.startsWith(JOB_STATUS_SUCCESS))
				throw new IOException("Request to create Organization '"+organization.getName()+"' has failed with status "+status);
		}
		catch (InterruptedException e) {
			throw new IOException("Request to create Organization '"+organization.getName()+"' was canceled");
		}
	}
	
	@Override
	public void createRepository(IKieRepository repository) throws IOException {
		final String jobId = httpPost("repositories", repository.getProperties());
		try {
			String status = getJobStatus(jobId);
			
			if (status==null) {
				throw new IOException("Request to create Repository '"+repository.getName()+"' has timed out");
			}
			if (!status.startsWith(JOB_STATUS_SUCCESS))
				throw new IOException("Request to create Repository '"+repository.getName()+"' has failed with status "+status);
		}
		catch (InterruptedException e) {
			throw new IOException("Request to create Repository '"+repository.getName()+"' was canceled");
		}
	}
	
	public void addRepository(IKieRepository repository, IKieOrganization organization) throws IOException {
		String jobId = httpPost("/organizationalunits/" + organization.getName() + "/repositories/"+repository.getName(), null);
		try {
			String status = getJobStatus(jobId);
			
			if (status==null) {
				throw new IOException("Request to add Repository '"+repository.getName()+"' has timed out");
			}
			if (!status.startsWith(JOB_STATUS_SUCCESS))
				throw new IOException("Request to add Repository '"+repository.getName()+"' has failed with status "+status);
		}
		catch (InterruptedException e) {
			throw new IOException("Request to add Repository '"+repository.getName()+"' was canceled");
		}
	}

	@Override
	public void createProject(IKieProject project) throws IOException {
	}

	@Override
	public void deleteOrganization(IKieOrganization organization) throws IOException {
	}

	@Override
	public void deleteRepository(IKieRepository repository, boolean removeOnly) throws IOException {
		String jobId;
		if (removeOnly) {
			// only remove the repo from its organizational unit
			String organization = repository.getParent().getName();
			jobId = httpDelete("/organizationalunits/" + organization + "/repositories/"+repository.getName());
		}
		else {
			// completely obliterate the repository
			jobId = httpDelete("repositories/"+repository.getName());
		}
		try {
			String status = getJobStatus(jobId);
			
			if (status==null) {
				throw new IOException("Request to delete Repository '"+repository.getName()+"' has timed out");
			}
			if (!status.startsWith(JOB_STATUS_SUCCESS))
				throw new IOException("Request to delete Repository '"+repository.getName()+"' has failed with status "+status);
		}
		catch (InterruptedException e) {
			throw new IOException("Request to delete Repository '"+repository.getName()+"' was canceled");
		}
	}

	@Override
	public void deleteProject(IKieProject project) throws IOException {
	}

}
