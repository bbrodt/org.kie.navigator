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
public class KieRepository extends KieResourceHandler implements IKieRepository {

	// debug/test only
	private boolean resolved;
	private static boolean resolvedToggle;
	
	/**
	 * @param organization
	 * @param string
	 */
	public KieRepository(IKieOrganization organization, String name) {
		super(organization, name);
		resolvedToggle = resolved = !resolvedToggle;
	}

	@Override
	public List<IKieProject> getProjects() throws Exception {
		return getDelegate().getProjects(this);
	}
	
	@Override
	public boolean isResolved() {
		return resolved;
	}
}
