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

import org.kie.navigator.view.server.IKieProject;
import org.kie.navigator.view.server.IKieRepository;

/**
 *
 */
public class RepositoryNode extends ContainerNode<OrganizationNode> {
	private final IKieRepository repository;
	private List<IKieProject> projects;
	
	/**
	 * @param container
	 * @param name
	 */
	protected RepositoryNode(OrganizationNode container, IKieRepository repository) {
		super(container, repository.getName());
		this.repository = repository;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateGetChildren()
	 */
	@Override
	protected List<? extends Object> delegateGetChildren() {
		if (projects!=null) {
			List<ProjectNode> result = new ArrayList<ProjectNode>();
			ProjectNode node;
			for (IKieProject project : projects) {
				node = new ProjectNode(this,project);
				result.add(node);
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
		if (projects!=null) {
			projects.clear();
			projects = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateLoad()
	 */
	@Override
	protected void delegateLoad() throws Exception {
		projects = getHandler().getProjects(repository);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
    
    public boolean isResolved() {
    	return repository.isResolved();
    }

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.IContainerNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return isResolved();
	}
}
