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

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public class KieOrganization implements IKieOrganization {

    private final IServer server;
	private final String name;
	/**
	 * 
	 */
	public KieOrganization(IServer server, String name) {
		this.server = server;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public List<IKieRepository> getRepositories() {
		return new ArrayList<IKieRepository>();
	}
}
