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

package org.kie.navigator.view.content;

import java.util.List;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.view.server.IKieOrganization;

/**
 *
 */
public class ServerNode extends ContainerNode {

	List<IKieOrganization> organizations = null;
	
	/**
	 * @param server
	 * @param name
	 */
	public ServerNode(IServer server, String name) {
		super(server, name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateGetChildren()
	 */
	@Override
	protected List delegateGetChildren() {
		return organizations;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateClearChildren()
	 */
	@Override
	protected void delegateClearChildren() {
		organizations.clear();
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateLoad()
	 */
	@Override
	protected void delegateLoad() throws Exception {
		organizations = getKieService().getOrganizations();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
