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
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.server.core.IServer;
import org.kie.navigator.KieNavigatorContentRoot;
import org.kie.navigator.view.server.IKieOrganization;
import org.kie.navigator.view.server.IKieResourceHandler;
import org.kie.navigator.view.server.KieServer;

/**
 *
 */
public class ServerNode extends ContainerNode {

	/**
	 * @param server
	 * @param name
	 */
	public ServerNode(IServer server) {
		super(server);
	}
	
	protected List<? extends IContentNode<?>> createChildren() {
		List<OrganizationNode> children = new ArrayList<OrganizationNode>();
		Iterator<? extends IKieResourceHandler> iter = handlerChildren.iterator();
		while (iter.hasNext()) {
			IKieResourceHandler h = iter.next();
			if (h instanceof IKieOrganization)
				children.add(new OrganizationNode(this,(IKieOrganization)h));
		}
		return children;
	}
	
    protected IKieResourceHandler getHandler() {
    	 if (handler==null) {
    		 handler = new KieServer(server);
    	 }
    	 return handler;
    }

	@Override
	public boolean isResolved() {
		return getServer().getServerState() == IServer.STATE_STARTED;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContentNode#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			ServerNode other = (ServerNode)obj;
			return other.getServer().getId().equals(this.getServer().getId());
		}
		catch (Exception ex) {
		}
		return false;
	}
}
