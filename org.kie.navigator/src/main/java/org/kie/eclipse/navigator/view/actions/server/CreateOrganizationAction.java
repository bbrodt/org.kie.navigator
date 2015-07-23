package org.kie.eclipse.navigator.view.actions.server;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.actions.dialogs.OrganizationRequestDialog;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieServer;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;
import org.kie.eclipse.navigator.view.server.KieOrganization;

import com.eclipsesource.json.JsonObject;

public class CreateOrganizationAction extends KieNavigatorAction {

	protected CreateOrganizationAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}
	
	public CreateOrganizationAction(ISelectionProvider selectionProvider) {
		this(selectionProvider, "Create Organization...");
	}

	public void run() {
        IStructuredSelection selection = getStructuredSelection();
        if (selection == null || selection.isEmpty()) {
            return;
        }
        IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
        IKieServer server = (IKieServer) container.getHandler();
        IKieServiceDelegate delegate = container.getHandler().getDelegate();
        
        OrganizationRequestDialog dlg = new OrganizationRequestDialog(Display.getDefault().getActiveShell(), server);
        
        if (dlg.open()==Window.OK) {
        	JsonObject properties = dlg.getResult();
        	String name = properties.get("name").asString();
            KieOrganization organization = new KieOrganization(server, name);
            organization.setProperties(properties);
            
            try {
            	delegate.createOrganization(organization);
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