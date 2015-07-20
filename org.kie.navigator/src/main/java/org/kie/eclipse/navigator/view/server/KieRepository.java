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

package org.kie.eclipse.navigator.view.server;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.egit.core.RepositoryCache;
import org.eclipse.egit.ui.UIUtils;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jgit.events.ConfigChangedEvent;
import org.eclipse.jgit.events.ConfigChangedListener;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.util.FS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.kie.eclipse.navigator.IKieNavigatorConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 *
 */
@SuppressWarnings("restriction")
public class KieRepository extends KieResourceHandler implements IKieRepository, ConfigChangedListener, IKieNavigatorConstants {

	static RepositoryCache repositoryCache = org.eclipse.egit.core.Activator.getDefault().getRepositoryCache();
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
	
	private String getRepoPath() {
        boolean useDefaultGitPath = getRoot().getPreference(PREF_USE_DEFAULT_GIT_PATH, false);
		String defaultRepoPath = UIUtils.getDefaultRepositoryDir();
		String repoPath;
		if (useDefaultGitPath) {
			repoPath = defaultRepoPath;
		}
		else
		{
			defaultRepoPath += File.separator + parent.getPreferenceName(null).replace(PREF_PATH_SEPARATOR.charAt(0), File.separator.charAt(0));
			repoPath = parent.getPreference(PREF_GIT_REPO_PATH, defaultRepoPath);
		}
		return repoPath;
	}
	
	public Object load() {
		if (repository == null) {
			String repoPath = getRepoPath();
			
			final File repoRoot = new File(repoPath);
			final Set<File> gitDirs = new HashSet<File>();
			final IRunnableWithProgress runnable = new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Searching for Repositories",IProgressMonitor.UNKNOWN);
					try {
						findGitDirsRecursive(repoRoot, gitDirs, monitor, false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}
				}
			};
			IProgressService ps = PlatformUI.getWorkbench().getProgressService();
			try {
				ps.busyCursorWhile(runnable);
			}
			catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
			
			for (File dir : gitDirs) {
				if (getName().equals(dir.getParentFile().getName())) {
					try {
						Repository repository = repositoryCache.lookupRepository(dir);
						StoredConfig storedConfig = repository.getConfig();
						Set<String> remotes = storedConfig.getSubsections("remote");
						for (String remoteName : remotes) {
							String url = storedConfig.getString("remote", remoteName, "url");
							System.out.println(repository.getDirectory());
							System.out.println(url);
							try {
								URI u = new URI(url);
								int port = u.getPort();
								String host = u.getHost();
								String scheme = u.getScheme();
								String path[] = u.getPath().split("/");
								String repoName = path[path.length-1];
								if (	name.equals(repoName) &&
										host.equals(getServer().getHost()) &&
										port == getDelegate().getGitPort() &&
										"ssh".equals(scheme)) {
									this.repository = repository;
									break;
								}
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if (repository!=null)
				// TODO: why doesn't this work?
				repository.getListenerList().addListener(ConfigChangedListener.class, this);

		}
		return repository;
	}
	
	
	private void findGitDirsRecursive(File repoRoot, Set<File> gitDirs,
			IProgressMonitor monitor, boolean lookForNestedRepositories) {

		if (!repoRoot.exists() || !repoRoot.isDirectory()) {
			return;
		}
		File[] children = repoRoot.listFiles();
		
		// simply ignore null
		if (children == null)
			return;

		for (File child : children) {
			if (monitor.isCanceled())
				return;
			if (!child.isDirectory())
				continue;

			if (FileKey.isGitRepository(child, FS.DETECTED)) {
				gitDirs.add(child);
			}
			else if (FileKey.isGitRepository(new File(child,
					Constants.DOT_GIT), FS.DETECTED)) {
				gitDirs.add(new File(child, Constants.DOT_GIT));
			}
			else if (lookForNestedRepositories) {
				monitor.subTask(child.getPath());
				findGitDirsRecursive(child, gitDirs, monitor, lookForNestedRepositories);
			}
		}
	}

	@Override
	public boolean isLoaded() {
		return repository != null;
	}
	
	public Repository getRepository() {
		return repository;
	}

	@Override
	public void dispose() {
		if (repository!=null) {
//			repository.getListenerList()
		}
		super.dispose();
	}

	@Override
	public void onConfigChanged(ConfigChangedEvent event) {
		// TODO: why doesn't this work?
		System.out.println("onConfigChanged: repository="+repository.getDirectory().getName());
	}
}
