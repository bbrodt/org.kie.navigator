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
			result.add(new KieOrganization(service, jo.get("name").asString()));
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
		JsonArray ja = JsonArray.readFrom(response);
		for (int i=0; i<ja.size(); ++i) {
			JsonObject jo = ja.get(i).asObject();
			if (organization.getName().equals(jo.get("name").asString())) {
				JsonArray jar = jo.get("repositories").asArray();
				for (int j=0; j<jar.size(); ++j) {
					JsonValue jv = jar.get(j);
					KieRepository kr = new KieRepository(organization, jv.asString());
					result.add(kr);
				}
			}
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
}
