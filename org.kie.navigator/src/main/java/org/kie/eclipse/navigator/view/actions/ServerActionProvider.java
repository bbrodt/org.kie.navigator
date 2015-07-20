package org.kie.eclipse.navigator.view.actions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.kie.eclipse.navigator.IKieNavigatorConstants;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieServer;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;
import org.kie.eclipse.navigator.view.server.KieOrganization;

import com.eclipsesource.json.JsonObject;

public class ServerActionProvider extends KieNavigatorActionProvider implements IKieNavigatorConstants {

	public ServerActionProvider() {
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        addAction(new CreateOrganizationAction(aSite.getStructuredViewer()));
        addAction(new ShowPropertiesAction(aSite));
    }

	private class CreateOrganizationAction extends KieNavigatorAction {

		protected CreateOrganizationAction(ISelectionProvider provider, String text) {
			super(provider, text);
		}
		
		public CreateOrganizationAction(ISelectionProvider selectionProvider) {
			this(selectionProvider, "Create Organization");
		}

		public void run() {
            IStructuredSelection selection = getStructuredSelection();
            if (selection == null || selection.isEmpty()) {
                return;
            }
            IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
            System.out.println("Create Organization: "+container.getName());
            IKieServer server = (IKieServer) container.getHandler();
            IKieServiceDelegate delegate = container.getHandler().getDelegate();
            
            
            
            KieOrganization organization = new KieOrganization(server, "test");
            JsonObject properties = JsonObject.readFrom(
            		"{" +
           			"\"name\":\"test3\"," +
           			"\"description\":\"This is a test organization\"," +
           			"\"owner\":\"tester\""+
					"}");
            organization.setProperties(properties);
            
            try {
            	delegate.createOrganization(server, organization);
            	container.clearChildren();
            	container.getNavigator().getCommonViewer().refresh(container);
            }
            catch (Exception e) {
            	e.printStackTrace();
            	MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e.getMessage());
            }
        }
	}

	private class ShowPropertiesAction extends PropertyDialogAction implements IKieNavigatorAction {
		
		public ShowPropertiesAction(ICommonActionExtensionSite aSite) {
			super(aSite.getViewSite().getShell(), aSite.getStructuredViewer());
		}
		public void calculateEnabled() {
			setEnabled(isEnabled());
		}
	}
}
