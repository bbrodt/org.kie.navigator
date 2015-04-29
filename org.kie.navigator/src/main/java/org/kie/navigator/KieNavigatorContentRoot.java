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

package org.kie.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.kie.navigator.view.content.IContentNode;
import org.kie.navigator.view.content.ServerNode;
import org.kie.navigator.view.server.KieServer;
import org.kie.navigator.view.server.ServerProxy;

/**
 * This is a placeholder class that replaces the default IWorkspaceRoot used as
 * initial input for CommonViewers. This class is part of the public API and
 * allows other CommonNavigator plugins to embed KIE Navigator content as child
 * or root nodes into their own content.
 */
public class KieNavigatorContentRoot {

	protected CommonNavigator viewer;
	protected List<? extends IContentNode<?>> children;

	/**
	 * @param kieNavigatorView
	 */
	public KieNavigatorContentRoot(CommonNavigator viewer) {
		this.viewer = viewer;
	}

    public List<? extends Object> getChildren() {
		updateChildren(createChildren());
		return children;
    }
    
	protected List<? extends IContentNode<?>> createChildren() {
		List<ServerNode> children = new ArrayList<ServerNode>();
		for (IServer s : ServerCore.getServers()) {
			if (KieServer.isSupportedServer(s)) {
				s = new ServerProxy(s);
				ServerNode node = new ServerNode(s);
				children.add(node);
			}
		}
		return ( List<? extends IContentNode<?>> )children;
	}

	protected void updateChildren(List<? extends IContentNode<?>> newChildren) {
		if (children==null)
			children = newChildren;
		else {
			List<IContentNode<?>> removed = new ArrayList<IContentNode<?>>();
			Iterator<? extends IContentNode<?>> newIter = newChildren.iterator();
			while (newIter.hasNext()) {
				IContentNode<?> newChild = newIter.next();
				boolean found = false;
				Iterator<? extends IContentNode<?>> oldIter = children.iterator();
				while (oldIter.hasNext()) {
					IContentNode<?> oldChild = oldIter.next();
					if (oldChild.equals(newChild)) {
						found = true;
						break;
					}
				}
				if (!found) {
					((List<IContentNode<?>>)children).add(newChild);
				}
			}

			Iterator<? extends IContentNode> oldIter = children.iterator();
			while (oldIter.hasNext()) {
				IContentNode oldChild = oldIter.next();
				boolean found = false;
				Iterator<? extends IContentNode> newIter2 = newChildren.iterator();
				while (newIter2.hasNext()) {
					IContentNode newChild = newIter2.next();
					if (oldChild.equals(newChild)) {
						found = true;
						break;
					}
				}
				if (!found) {
					removed.add(oldChild);
				}
			}
			children.removeAll(removed);
		}
		
	}
}
