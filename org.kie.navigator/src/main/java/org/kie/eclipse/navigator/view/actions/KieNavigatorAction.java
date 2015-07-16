package org.kie.eclipse.navigator.view.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.actions.SelectionProviderAction;

public class KieNavigatorAction extends SelectionProviderAction implements IKieNavigatorAction {

	public KieNavigatorAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}

	public void calculateEnabled() {
		setEnabled(isEnabled());
	}
}
