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

package org.kie.navigator.view.server.jbpm60;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.kie.navigator.view.server.IKieOrganization;
import org.kie.navigator.view.server.IKieProject;
import org.kie.navigator.view.server.IKieRepository;
import org.kie.navigator.view.server.KieOrganization;
import org.kie.navigator.view.server.KieProject;
import org.kie.navigator.view.server.KieRepository;
import org.kie.navigator.view.server.KieServiceImpl;

import com.eclipsesource.json.*;

/**
 *
 */
public class Kie60ServiceImpl extends KieServiceImpl {

	private final static String KIE_VERSION = "org.jboss.kie.60";
	
	/**
	 *
	 */
	public Kie60ServiceImpl() {
	}

	String getVersion() {
		return KIE_VERSION;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieServiceImpl#getOrganizations()
	 */
	@Override
	public List<IKieOrganization> getOrganizations()  throws IOException {
		List<IKieOrganization> result = new ArrayList<IKieOrganization>();
		result.add(new KieOrganization(getServer(), "Organization 1"));
		result.add(new KieOrganization(getServer(), "Organization 2"));
		
		String response = httpGet("organizationalunits");
		JsonArray ja = JsonArray.readFrom(response);
		for (int i=0; i<ja.size(); ++i) {
			JsonObject jo = ja.get(i).asObject();
			result.add(new KieOrganization(getServer(), jo.get("name").asString()));
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieServiceImpl#getRepositories(org.kie.navigator.view.server.IKieOrganization)
	 */
	@Override
	public List<IKieRepository> getRepositories(IKieOrganization organization) throws IOException {
		List<IKieRepository> result = new ArrayList<IKieRepository>();
		result.add(new KieRepository(organization, "Repository 1"));
		result.add(new KieRepository(organization, "Repository 2"));

		String response = httpGet("organizationalunits");
		JsonArray ja = JsonArray.readFrom(response);
		for (int i=0; i<ja.size(); ++i) {
			JsonObject jo = ja.get(i).asObject();
			if (organization.getName().equals(jo.get("name").asString())) {
				JsonArray jar = jo.get("repositories").asArray();
				for (int j=0; j<jar.size(); ++j) {
					JsonValue jv = jar.get(j);
					KieRepository kr = new KieRepository(organization, jv.asString());
					kr.setResolved(true);
					result.add(kr);
				}
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieServiceImpl#getProjects(org.kie.navigator.view.server.IKieOrganization, org.kie.navigator.view.server.IKieRepository)
	 */
	@Override
	public List<IKieProject> getProjects(IKieRepository repository) throws IOException {
		List<IKieProject> result = new ArrayList<IKieProject>();
		if (repository.isResolved()) {
			result.add(new KieProject(repository, "Project 1"));
			result.add(new KieProject(repository, "Project 2"));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.KieServiceImpl#getHttpUrl()
	 */
	@Override
	protected String getKieRESTUrl() {
		return "http://localhost:8080/jbpm-console/rest";
	}
}
