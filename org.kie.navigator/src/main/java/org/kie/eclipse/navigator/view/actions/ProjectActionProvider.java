package org.kie.eclipse.navigator.view.actions;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.project.RepositoryFinder;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.content.ProjectNode;
import org.kie.eclipse.navigator.view.content.RepositoryNode;
import org.kie.eclipse.navigator.view.server.KieRepository;

public class ProjectActionProvider extends KieNavigatorActionProvider {

	public ProjectActionProvider() {
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        addAction(new ImportProjectAction(aSite.getStructuredViewer()));
    }

	private class ImportProjectAction extends KieNavigatorAction {

		protected ImportProjectAction(ISelectionProvider provider, String text) {
			super(provider, text);
		}
		
		public ImportProjectAction(ISelectionProvider selectionProvider) {
			this(selectionProvider, "Import Project");
		}

		public void run() {
			IStructuredSelection selection = getStructuredSelection();
			if (selection == null || selection.isEmpty()) {
				return;
			}
			IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
			if (!(container instanceof ProjectNode)) {
				return;
			}
			final ProjectNode projectNode = (ProjectNode) container;
			final String projectName = projectNode.getName();
			final RepositoryNode repoNode = (RepositoryNode) projectNode.getContainer();
			IWorkspaceRunnable wsr = new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						Repository repository = ((KieRepository) repoNode.getHandler()).getRepository();
						IProject project = createOrOpenProject(repository, projectName, monitor);
						if (project != null) {
							Map<IProject, File> projectsToConnect = new HashMap<IProject, File>();
							RepositoryFinder finder = new RepositoryFinder(project);
							finder.setFindInChildren(false);
							Collection<RepositoryMapping> mappings = finder.find(new SubProgressMonitor(monitor, 1));
							if (!mappings.isEmpty()) {
								RepositoryMapping mapping = mappings.iterator().next();
								projectsToConnect.put(project, mapping.getGitDirAbsolutePath().toFile());
								ConnectProviderOperation connect = new ConnectProviderOperation(projectsToConnect);
								connect.execute(monitor);
							}
						}
					}
					catch (CoreException e1) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e1.getMessage());
							}
						});
					}
					catch (IllegalArgumentException e2) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e2.getMessage());
							}
						});
					}
				}
			};
			try {
				ResourcesPlugin.getWorkspace().run(wsr, null);
			}
			catch (OperationCanceledException e) {
				e.printStackTrace();
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
        }
		
		private IProject createOrOpenProject(Repository repository, String projectName, IProgressMonitor monitor) throws CoreException, IllegalArgumentException {
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IProject project = workspace.getRoot().getProject(projectName);
    		IPath location = new Path(repository.getWorkTree().toString());
    		location = location.append(projectName);
			if (project.exists()) {
				if (!project.isOpen()) {
					IPath oldLocation = project.getFile(IProjectDescription.DESCRIPTION_FILE_NAME).getLocation();
					if (oldLocation != null && oldLocation.equals(location)) {
						project.open(monitor);
						project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
						return null;
					}
				}
				throw new IllegalArgumentException("The Project "+projectName+" already exists");
			}
			IProjectDescription pd = workspace.newProjectDescription(projectName);
    		pd.setLocation(location);
			project.create(pd, new SubProgressMonitor(monitor, 30));
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 50));
			return project;

		}
	}
}
