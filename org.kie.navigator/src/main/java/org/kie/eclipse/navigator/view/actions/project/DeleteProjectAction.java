package org.kie.eclipse.navigator.view.actions.project;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieProject;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;

public class DeleteProjectAction extends KieNavigatorAction {

	public DeleteProjectAction(ISelectionProvider provider) {
		super(provider, "Delete Project...");
	}

	public void run() {
        IStructuredSelection selection = getStructuredSelection();
        if (selection == null || selection.isEmpty()) {
            return;
        }
        IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
        IKieProject project = (IKieProject) container.getHandler();
        IKieServiceDelegate delegate = container.getHandler().getDelegate();

        boolean doit = MessageDialog.openConfirm(
				getShell(), "Delete Project",
				"Are you sure you want to delete the Project " + container.getName() + "?");
		if (doit) {
            try {
            	delegate.deleteProject(project);
            	container = container.getParent();
            	container.clearChildren();
            	container.getNavigator().getCommonViewer().refresh(container);
            }
            catch (Exception e) {
            	e.printStackTrace();
            	MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e.getMessage());
            }
        }
	}
}
