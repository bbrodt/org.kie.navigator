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

import org.eclipse.wst.server.core.IServer;

/**
 *
 */
public class KieProject implements IKieProject {

	private final IKieRepository repository;
	private final String name;
	
	// debug/test only
	private boolean resolved;
	private static boolean resolvedToggle;
	
	/**
	 * @param repository
	 * @param string
	 */
	public KieProject(IKieRepository repository, String name) {
		this.repository = repository;
		this.name = name;
		resolvedToggle = resolved = !resolvedToggle;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieProject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieProject#getServer()
	 */
	@Override
	public IServer getServer() {
		return repository.getServer();
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieProject#isResolved()
	 */
	@Override
	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
}
