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
public interface IKieServiceImpl {

	public final static String KIE_SERVICE_IMPL_ID = "org.kie.navigator.serviceImpl";

	/**
	 * @param server
	 */
	void setServer(IServer server);

	/**
	 * @return server
	 */
	IServer getServer();
	
	/**
	 * @return
	 */
	List<IKieOrganization> getOrganizations() throws RuntimeException;

	List<IKieRepository> getRepositories(IKieOrganization organization) throws RuntimeException;
	
	List<IKieProject> getProjects(IKieRepository repository) throws RuntimeException;
}
