package org.kie.eclipse.navigator.preferences;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.egit.ui.UIUtils;
import org.eclipse.jgit.transport.URIish;
import org.kie.eclipse.navigator.IKieNavigatorConstants;
import org.kie.eclipse.navigator.view.server.IKieRepository;
import org.kie.eclipse.navigator.view.server.IKieResourceHandler;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;

public class PreferencesUtils implements IKieNavigatorConstants {

	public PreferencesUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String getRepoPath(IKieRepository repo) {
	    boolean useDefaultGitPath = repo.getRoot().getPreference(PREF_USE_DEFAULT_GIT_PATH, false);
		String defaultRepoPath = UIUtils.getDefaultRepositoryDir();
		String repoPath;
		if (useDefaultGitPath) {
			repoPath = defaultRepoPath;
		}
		else
		{
			IKieResourceHandler parent = repo.getParent();
			defaultRepoPath += File.separator + parent.getPreferenceName(null).replace(PREF_PATH_SEPARATOR.charAt(0), File.separator.charAt(0));
			repoPath = parent.getPreference(PREF_GIT_REPO_PATH, defaultRepoPath);
		}
		return repoPath;
	}

	public static URIish getRepoURI(String host, int port, String username, String repoName) {
        // URI is in the form:
        // ssh://admin@localhost:8001/jbpm-playground
		URIish uri = null;
		try {
			uri = new URIish(
					"ssh://"+
			(username==null || username.isEmpty() ? "" : username+"@") +
					host+":"+
					port+"/"+
					repoName
			);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
	}
}
