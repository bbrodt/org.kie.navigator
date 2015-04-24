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

import org.kie.navigator.view.server.IKieOrganization;
import org.kie.navigator.view.server.IKieRepository;

/**
 *
 */
public class OrganizationNode extends ContainerNode<ServerNode> {
	private final IKieOrganization organization;
	private List<IKieRepository> repositories = null;
	
	/**
	 * @param container
	 * @param name
	 */
	protected OrganizationNode(ServerNode container, IKieOrganization organization) {
		super(container, organization.getName());
		this.organization = organization;
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateGetChildren()
	 */
	@Override
	protected List<? extends Object> delegateGetChildren() {
		if (repositories!=null) {
			List<RepositoryNode> result = new ArrayList<RepositoryNode>();
			for (IKieRepository repo : repositories) {
				result.add(new RepositoryNode(this,repo));
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
		if (repositories!=null) {
			repositories.clear();
			repositories = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.ContainerNode#delegateLoad()
	 */
	@Override
	protected void delegateLoad() throws Exception {
		repositories = getKieService().getRepositories(organization);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.kie.navigator.view.content.IContainerNode#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return true;
	}
}
