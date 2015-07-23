package org.kie.eclipse.navigator.view.actions.organization;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.actions.dialogs.RepositoryRequestDialog;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieOrganization;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;
import org.kie.eclipse.navigator.view.server.KieRepository;

import com.eclipsesource.json.JsonObject;

public class CreateRepositoryAction extends KieNavigatorAction {

	protected CreateRepositoryAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}
	
	public CreateRepositoryAction(ISelectionProvider selectionProvider) {
		this(selectionProvider, "Create Repository...");
	}

	public void run() {
        IStructuredSelection selection = getStructuredSelection();
        if (selection == null || selection.isEmpty()) {
            return;
        }
        IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
        IKieOrganization organization = (IKieOrganization) container.getHandler();
        IKieServiceDelegate delegate = container.getHandler().getDelegate();

        RepositoryRequestDialog dlg = new RepositoryRequestDialog(Display.getDefault().getActiveShell(), organization);
        
        if (dlg.open()== Window.OK){
        	JsonObject properties = dlg.getResult();
        	String name = properties.get("name").asString();
        	KieRepository repository = new KieRepository(organization, name);
        	repository.setProperties(properties);
            
            try {
            	delegate.createRepository(repository);
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