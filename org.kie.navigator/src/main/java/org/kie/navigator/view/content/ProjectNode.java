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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.kie.navigator.view.server.IKieProject;

/**
 *
 */
public class ProjectNode extends ContainerNode<RepositoryNode> {
	
	private final IKieProject kieProject;

	/**
	 * @param container
	 * @param name
	 */
	protected ProjectNode(RepositoryNode container, IKieProject project) {
		super(container, project.getName());
		this.kieProject = project;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateGetChildren()
	 */
	@Override
	protected List<? extends Object> delegateGetChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateClearChildren()
	 */
	@Override
	protected void delegateClearChildren() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateLoad()
	 */
	@Override
	protected void delegateLoad() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object resolveContent() {
		// test code to see if Project View works
		if (kieProject.isResolved()) {
			IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			if (projects.length>0) {
				return projects[0];
			}
		}
		return super.resolveContent();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public boolean isResolved() {
		return kieProject.isResolved();
	}
	
	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.IContainerNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return isResolved();
	}
}
