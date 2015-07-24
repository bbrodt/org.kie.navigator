package org.kie.eclipse.navigator.view.actions.organization;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieOrganization;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;

public class DeleteOrganizationAction extends KieNavigatorAction {

	public DeleteOrganizationAction(ISelectionProvider provider) {
		super(provider, "Delete Organization...");
	}

	public void run() {
        IStructuredSelection selection = getStructuredSelection();
        if (selection == null || selection.isEmpty()) {
            return;
        }
        IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
        IKieOrganization organization = (IKieOrganization) container.getHandler();
        IKieServiceDelegate delegate = container.getHandler().getDelegate();

        boolean doit = MessageDialog.openConfirm(
				getShell(), "Delete Organizational Unit",
				"Are you sure you want to delete the Organizational Unit " + container.getName() + "?");
		if (doit) {
            try {
            	delegate.deleteOrganization(organization);
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
