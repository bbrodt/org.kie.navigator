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

package org.kie.eclipse.navigator.view.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.wst.server.core.IServer;
import org.kie.eclipse.navigator.Activator;
import org.kie.eclipse.navigator.IKieNavigatorConstants;
import org.kie.eclipse.navigator.view.server.IKieOrganization;
import org.kie.eclipse.navigator.view.server.IKieResourceHandler;
import org.kie.eclipse.navigator.view.server.KieServer;

/**
 *
 */
public class ServerNode extends ContainerNode implements IPropertyChangeListener, IKieNavigatorConstants {

	protected CommonNavigator navigator;
    protected final IServer server;

	/**
	 * @param server
	 * @param name
	 */
	public ServerNode(IServer server, CommonNavigator navigator) {
		super(server==null ? "root" : server.getName());
		this.server = server;
		this.navigator = navigator;
        handler = server==null ? null : new KieServer(server);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
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

    public IServer getServer() {
        return server;
    }

	public CommonNavigator getNavigator() {
		return navigator;
	}
	
	public IKieResourceHandler getHandler() {
    	 if (handler==null) {
    		 handler = new KieServer(server);
    	 }
    	 return handler;
    }
    public String getRuntimeId() {
    	return getHandler().getRuntimeId();
    }
    
	@Override
	public boolean isResolved() {
		return getServer().getServerState() == IServer.STATE_STARTED;
	}

	@Override
	public void dispose() {
		super.dispose();
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.kie.eclipse.navigator.view.content.ContentNode#equals(java.lang.Object)
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getProperty();
		if (name.endsWith(PREF_GIT_REPO_PATH) || name.endsWith(PREF_USE_DEFAULT_GIT_PATH)) {
	        clearChildren();
	        CommonViewer viewer = navigator.getCommonViewer();
	        viewer.refresh(this);
		}
	}
}
