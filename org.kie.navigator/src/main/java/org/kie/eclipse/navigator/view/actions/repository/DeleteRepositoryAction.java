package org.kie.eclipse.navigator.view.actions.repository;

import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieRepository;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;

public class DeleteRepositoryAction extends KieNavigatorAction {

	protected DeleteRepositoryAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}

	public DeleteRepositoryAction(ISelectionProvider selectionProvider) {
		this(selectionProvider, "Remove Repository");
	}

	@Override
	public String getToolTipText() {
		return "Remove this Git Repository from the Organizational Unit";
	}

	public void run() {
		IStructuredSelection selection = getStructuredSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}
		IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
		MessageDialogWithToggle dlg = MessageDialogWithToggle.openYesNoQuestion(getShell(), "Remove Repository",
				"Are you sure you want to remove the Repository " + container.getName() + " from the Organizational Unit "
						+ container.getParent().getName() + "?", "Also delete the Repository completely from the Server.",
				false, null, null);
		if (dlg.getReturnCode() == IDialogConstants.YES_ID) {
			IKieServiceDelegate delegate = container.getHandler().getDelegate();
			try {
				delegate.deleteRepository((IKieRepository) container.getHandler(), !dlg.getToggleState());
				container.getParent().clearChildren();
				container.getNavigator().getCommonViewer().refresh(container.getParent());
			}
			catch (IOException e) {
				e.printStackTrace();
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e.getMessage());
			}
		}
	}
}