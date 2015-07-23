package org.kie.eclipse.navigator.view.actions.organization;

import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.kie.eclipse.navigator.view.actions.KieNavigatorActionProvider;

public class OrganizationActionProvider extends KieNavigatorActionProvider {

	public OrganizationActionProvider() {
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
//        addAction(new RemoveOrganizationAction(aSite.getStructuredViewer()));
        addAction(new AddRepositoryAction(aSite.getStructuredViewer()));
        addAction(new CreateRepositoryAction(aSite.getStructuredViewer()));
    }
	
 
}
