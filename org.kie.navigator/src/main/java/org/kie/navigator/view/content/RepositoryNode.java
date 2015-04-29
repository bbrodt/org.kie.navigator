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

import org.kie.navigator.view.server.IKieOrganization;
import org.kie.navigator.view.server.IKieProject;
import org.kie.navigator.view.server.IKieRepository;
import org.kie.navigator.view.server.IKieResourceHandler;

/**
 *
 */
public class RepositoryNode extends ContainerNode<OrganizationNode> {
	
	/**
	 * @param container
	 * @param name
	 */
	protected RepositoryNode(OrganizationNode container, IKieRepository repository) {
		super(container, repository);
	}

	protected List<? extends IContentNode<?>> createChildren() {
		List<ProjectNode> result = new ArrayList<ProjectNode>();
		Iterator<? extends IKieResourceHandler> iter = handlerChildren.iterator();
		while (iter.hasNext()) {
			IKieResourceHandler h = iter.next();
			if (h instanceof IKieProject)
				result.add(new ProjectNode(this,(IKieProject)h));
		}
		return result;

	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContentNode#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			RepositoryNode other = (RepositoryNode) obj;
			return other.getName().equals(this.getName());
		}
		catch (Exception ex) {
		}
		return false;
	}
}
