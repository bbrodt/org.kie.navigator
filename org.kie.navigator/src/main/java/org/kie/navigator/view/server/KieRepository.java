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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import org.eclipse.egit.core.RepositoryCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;

/**
 *
 */
public class KieRepository extends KieResourceHandler implements IKieRepository {

	// debug/test only
	Repository repository;
	
	/**
	 * @param organization
	 * @param string
	 */
	public KieRepository(IKieOrganization organization, String name) {
		super(organization, name);
	}
	
	@Override
	public List<? extends IKieResourceHandler> getChildren() throws Exception {
		return getDelegate().getProjects(this);
	}
	
	public Object load() {
		if (repository == null) {
			RepositoryCache repositoryCache = org.eclipse.egit.core.Activator.getDefault().getRepositoryCache();
			
			for (Repository repository : repositoryCache.getAllRepositories()) {
				StoredConfig storedConfig = repository.getConfig();
				Set<String> remotes = storedConfig.getSubsections("remote");
				for (String remoteName : remotes) {
					String url = storedConfig.getString("remote", remoteName, "url");
					System.out.println(repository.getDirectory());
					System.out.println(url);
					try {
						URI u = new URI(url);
						String user = u.getUserInfo();
						String host = u.getHost();
						String path[] = u.getPath().split("/");
						String repoName = path[path.length-1];
						if (	name.equals(repoName) &&
								host.equals(getServer().getHost()) &&
								user.equals(getDelegate().getUsername())) {
							this.repository = repository;
							break;
						}
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return repository;
	}
	
	@Override
	public boolean isLoaded() {
		return repository != null;
	}
	
	public Repository getRepository() {
		return repository;
	}
}
