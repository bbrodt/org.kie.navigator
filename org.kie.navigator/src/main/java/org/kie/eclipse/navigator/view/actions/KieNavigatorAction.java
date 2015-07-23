package org.kie.eclipse.navigator.view.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.SelectionProviderAction;

public class KieNavigatorAction extends SelectionProviderAction implements IKieNavigatorAction {

	public KieNavigatorAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}

	public void calculateEnabled() {
		setEnabled(isEnabled());
	}
	
	protected Shell getShell() {
		return Display.getDefault().getActiveShell();
	}
}
