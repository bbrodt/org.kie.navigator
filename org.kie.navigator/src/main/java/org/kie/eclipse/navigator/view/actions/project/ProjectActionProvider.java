package org.kie.eclipse.navigator.view.actions.project;

import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.kie.eclipse.navigator.view.actions.KieNavigatorActionProvider;

public class ProjectActionProvider extends KieNavigatorActionProvider {

	public ProjectActionProvider() {
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        addAction(new ImportProjectAction(aSite.getStructuredViewer()));
    }
}
