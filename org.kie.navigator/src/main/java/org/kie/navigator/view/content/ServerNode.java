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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.view.server.IKieOrganization;
import org.kie.navigator.view.server.IKieResourceHandler;
import org.kie.navigator.view.server.KieServer;

/**
 *
 */
public class ServerNode extends ContainerNode {
	protected List<? extends IKieResourceHandler> childrenHandlers = null;

	/**
	 * @param server
	 * @param name
	 */
	public ServerNode(IServer server, String name) {
		super(server, name);
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateGetChildren()
	 */
	@Override
	protected List<? extends Object> delegateGetChildren() {
		if (childrenHandlers!=null) {
			List<OrganizationNode> result = new ArrayList<OrganizationNode>();
			for (IKieResourceHandler h : childrenHandlers) {
				if (h instanceof IKieOrganization)
					result.add(new OrganizationNode(this,(IKieOrganization)h));
			}
			return result;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateClearChildren()
	 */
	@Override
	protected void delegateClearChildren() {
		if (childrenHandlers!=null) {
			childrenHandlers.clear();
			childrenHandlers = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateLoad()
	 */
	@Override
	protected void delegateLoad() throws Exception {
		childrenHandlers = getHandler().getChildren();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

    protected KieServer getHandler() {
    	 if (handler==null) {
    		 handler = new KieServer(server);
    	 }
    	 return handler;
    }

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.IContainerNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return getServer().getServerState() == IServer.STATE_STARTED;
	}
}
