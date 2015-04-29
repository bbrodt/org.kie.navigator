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
public abstract class KieResourceHandler implements IKieResourceHandler {

	protected IKieResourceHandler parent;
	protected String name;

	public KieResourceHandler(IKieResourceHandler parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieResourceHandler#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.server.IKieResourceHandler#getServer()
	 */
	@Override
	public IServer getServer() {
		if (parent!=null)
			return parent.getServer();
		return null;
	}
	
	@Override
	public IKieServiceDelegate getDelegate() {
		if (parent!=null)
			return parent.getDelegate();
		return null;
	}

	public void dispose() {
	}
	
	public Object load() {
		return null;
	}
	
	public boolean isLoaded() {
		return false;
	}
}
