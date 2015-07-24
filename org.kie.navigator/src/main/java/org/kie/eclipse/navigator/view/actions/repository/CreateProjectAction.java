package org.kie.eclipse.navigator.view.actions.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.kie.eclipse.navigator.view.actions.KieNavigatorAction;
import org.kie.eclipse.navigator.view.actions.dialogs.ProjectRequestDialog;
import org.kie.eclipse.navigator.view.content.ContentNode;
import org.kie.eclipse.navigator.view.content.IContainerNode;
import org.kie.eclipse.navigator.view.server.IKieProject;
import org.kie.eclipse.navigator.view.server.IKieRepository;
import org.kie.eclipse.navigator.view.server.IKieServiceDelegate;
import org.kie.eclipse.navigator.view.server.KieProject;
import org.kie.eclipse.navigator.view.server.KieRepository;

import com.eclipsesource.json.JsonObject;

public class CreateProjectAction extends KieNavigatorAction {

	protected CreateProjectAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}

	public CreateProjectAction(ISelectionProvider selectionProvider) {
		this(selectionProvider, "Create Project...");
	}

	@Override
	public String getToolTipText() {
		return "Create a new Project with the New Drools Project Wizard";
	}

	@Override
	public boolean isEnabled() {
		IStructuredSelection selection = getStructuredSelection();
		if (selection != null && !selection.isEmpty()) {
			IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
			if (container instanceof ContentNode) {
				KieRepository handler = (KieRepository) ((ContentNode) container).getHandler();
				if (handler == null || !handler.isLoaded())
					return false;
			}
		}
		return true;
	}

	public void run() {
		IStructuredSelection selection = getStructuredSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}
		IContainerNode<?> container = (IContainerNode<?>) ((IStructuredSelection) selection).getFirstElement();
		IKieRepository repository = (IKieRepository) container.getHandler();
        IKieServiceDelegate delegate = container.getHandler().getDelegate();
        ProjectRequestDialog dlg = new ProjectRequestDialog(getShell(), repository);
        
        if (dlg.open()==Window.OK) {
        	JsonObject properties = dlg.getResult();
        	String name = properties.get("name").asString();
            IKieProject project = new KieProject(repository, name);
            project.setProperties(properties);
	        try {
	        	delegate.createProject(project);
	        	container = container.getParent();
	        	container.clearChildren();
	        	container.getNavigator().getCommonViewer().refresh(container);
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        	MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", e.getMessage());
	        }
        }
        
//		BasicNewResourceWizard wizard = (BasicNewResourceWizard) createWizard("org.drools.eclipse.wizards.new.project");
//		wizard.init(PlatformUI.getWorkbench(), selection);
//		WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
//		wd.setTitle(wizard.getWindowTitle());
//		int rtn = wd.open();
//		System.out.println(rtn);
	}

	public IWizard createWizard(String id) {
		// First see if this is a "new wizard".
		IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
		// If not check if it is an "import wizard".
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
		}
		// Or maybe an export wizard
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
		}
		try {
			// Then if we have a wizard, open it.
			if (descriptor != null) {
				IWizard wizard = descriptor.createWizard();
				return wizard;
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
}