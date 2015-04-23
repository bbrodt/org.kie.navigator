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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.Activator;

/**
 *
 */
public class KieService implements IKieService {

    private final IServer server;
	private IKieServiceImpl delegate;
	
	/**
	 * 
	 */
	public KieService(IServer server) {
		this.server = server;
		delegate = new KieServiceImpl();
		delegate.setServer(server);
	}

	protected IKieServiceImpl loadDelegate() {
		IKieServiceImpl result = null;
		try {
			IConfigurationElement[] config = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(IKieServiceImpl.KIE_SERVICE_IMPL_ID);
			for (IConfigurationElement serviceImplElement : config) {
				for (IConfigurationElement e : serviceImplElement.getChildren()) {
					if ("containerBinding".equals(e.getName())) {
						String serverId = e.getAttribute("serverId");
						if (server.getServerType().getId().equals(serverId)) {
							String kieVersion = e.getAttribute("kieVersion");
							if ( getDeployedKieVersion(server).equals(kieVersion) ) {
								Object o = e.createExecutableExtension("class");
								if (o instanceof IKieServiceImpl) {
									result = (IKieServiceImpl)o;
									result.setServer(server);
									return result;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return result;
	}
	
	/**
	 * @param server2
	 * @return
	 */
	protected Object getDeployedKieVersion(IServer server) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieService#getOrganizations()
	 */
	@Override
	public List<IKieOrganization> getOrganizations() {
		List<IKieOrganization> orgs = new ArrayList<IKieOrganization>();
		if (isServerRunning()) {
			// fetch from server and synch if needed
		}
		else {
			// fetch from preference store
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String value =  store.getString(server.getId()+"/organizations");
			for (String v : value.split("|")) {
				IKieOrganization org = new KieOrganization(server, v);
				orgs.add(org);
			}
		}
		return orgs;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieService#getRepositories(org.kie.navigator.view.server.IKieOrganization)
	 */
	@Override
	public List<IKieRepository> getRepositories(IKieOrganization org) {
		return new ArrayList<IKieRepository>();
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieService#getProjects(org.kie.navigator.view.server.IKieRepository)
	 */
	@Override
	public List<IKieProject> getProjects(IKieRepository repo) {
		return new ArrayList<IKieProject>();
	}

	protected boolean isServerRunning() {
		return server.getServerState() == IServer.STATE_STARTED;
	}
}
