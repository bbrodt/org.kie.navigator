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

import java.util.List;

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public class KieRepository implements IKieRepository {

	private final IKieOrganization organization;
	private final String name;
	
	// debug/test only
	private boolean resolved;
	private static boolean resolvedToggle;
	
	/**
	 * @param organization
	 * @param string
	 */
	public KieRepository(IKieOrganization organization, String name) {
		this.organization = organization;
		this.name = name;
		resolvedToggle = resolved = !resolvedToggle;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieRepository#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieRepository#getProjects()
	 */
	@Override
	public List<IKieProject> getProjects() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieRepository#getServer()
	 */
	@Override
	public IServer getServer() {
		return organization.getServer();
	}
	
	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieRepository#isResolved()
	 */
	@Override
	public boolean isResolved() {
		return resolved;
	}

}
